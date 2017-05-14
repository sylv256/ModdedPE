package com.mcal.ModdedPE.nmod;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import com.google.gson.*;
import com.mcal.ModdedPE.*;
import java.io.*;
import java.util.*;

public abstract class NMod
{
	protected Context thisContext;
	protected NModLoadException bugExpection = null ;
	protected NModDataBean dataBean;
	protected boolean isActive;
	protected NModLoader loader;
	protected Bitmap icon;
	protected Bitmap version_image;
	public static final String TAG_MANIFEST_NAME = "nmod_manifest.json";

	public abstract void load(String mcVer, String moddedpeVer) throws Exception;
	public abstract String getPackageName();
	public abstract AssetManager getAssets();
	public abstract String getPackageResourcePath();
	public abstract String getNativeLibsPath();
	public abstract Bitmap createIcon();

	public String[] getNativeLibs()
	{
		return dataBean.native_libs;
	}

	public NModLoader getLoader()
	{
		return loader;
	}

	public String getName()
	{
		if (isBugPack())
			return getPackageName();
		return dataBean.name;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (getClass() == obj.getClass())
			return getPackageName().equals(((NMod)obj).getPackageName());
		return false;
	}

	public Bitmap createVersionImage() throws NModLoadException
	{
		Bitmap ret = null;
		try
		{
			if (dataBean.version_info == null || dataBean.version_info.version_description_image == null)
				return null;
			InputStream is = getAssets().open(dataBean.version_info.version_description_image);
			ret = BitmapFactory.decodeStream(is);
		}
		catch (IOException e)
		{
			throw NModLoadException.getFileNotFound(e, thisContext.getResources(), dataBean.version_info.version_description_image);
		}
		catch (Throwable t)
		{
			throw NModLoadException.getImageDecode(t, thisContext.getResources(), dataBean.version_info.version_description_image);
		}
		if (ret == null)
			throw NModLoadException.getImageDecode(null, thisContext.getResources(), dataBean.version_info.version_description_image);

		if (ret.getWidth() != 1024 || ret.getHeight() != 500)
			throw NModLoadException.getBadImageSize(thisContext.getResources());
		return ret;
	}

	public Bitmap getVersionImage()
	{
		return version_image;
	}

	public String getNewsTitle()
	{
		if (dataBean.version_info != null && dataBean.version_info.version_description_short != null)
			return dataBean.name + " : " + dataBean.version_info.version_description_short;
		return null;
	}

	public boolean isValidNews()
	{
		return getVersionImage() != null && getNewsTitle() != null;
	}

	public NModLanguageBean[] getLanguageBeans()
	{
		return dataBean.languages;
	}

	private NModLoadException findLoadException()
	{
		NModLoadException ejson=checkJSONs();
		if (ejson != null)
			return ejson;
		if (dataBean.languages != null)
		{
			for (NModLanguageBean lang:dataBean.languages)
			{
				try
				{
					getAssets().open(lang.location);
				}
				catch (IOException e)
				{
					return NModLoadException.getFileNotFound(e, thisContext.getResources(), lang.location);
				}
			}
		}

		return null;
	}

	private static class LoopFileSearcher
	{
		private AssetManager mgr;
		private Vector<String> exploredFiles;
		public LoopFileSearcher(AssetManager mgr)
		{
			this.mgr = mgr;
			this.exploredFiles = new Vector<String>();
		}

		public Vector<String> getAllFiles()
		{
			calculate("");
			return exploredFiles;
		}

		public void calculate(String oldPath)
		{
			try
			{
				if (!isFile(oldPath))
				{
					String fileNames[] = mgr.list(oldPath);
					for (String fileName : fileNames)
					{
						if (oldPath == null || oldPath.isEmpty())
							calculate(fileName);
						else
							calculate(oldPath + "/" + fileName);

					}
				}
				else
				{
					exploredFiles.add(oldPath);
				}
			}
			catch (IOException e)
			{  
				e.printStackTrace();  
			}                             
		}

		private boolean isFile(String path)
		{
			try
			{
				mgr.open(path);
			}
			catch (IOException e)
			{
				return false;
			}
			return true;
		}
	}

	private NModLoadException checkJSONs()
	{
		if (dataBean == null || !dataBean.check_json_syntax)
			return null;

		Vector<String> allFiles=new LoopFileSearcher(getAssets()).getAllFiles();
		if (allFiles == null)
			return null;

		for (String path:allFiles)
		{
			if (path.toLowerCase().endsWith(".json"))
			{
				try
				{
					InputStream is = getAssets().open(path);
					byte[] buffer=new byte[is.available()];
					is.read(buffer);
					String jsonStr=new String(buffer);
					try
					{
						new Gson().fromJson(jsonStr, Object.class);
					}
					catch (Throwable t)
					{
						return NModLoadException.getBadJsonSyntax(t, thisContext.getResources(), path);
					}
				}
				catch (IOException e)
				{

				}
			}
		}
		return null;
	}

	public Bitmap getIcon()
	{
		return icon;
	}

	public String getDescription()
	{
		if (dataBean.description != null)
			return dataBean.description;
		return thisContext.getResources().getString(R.string.nmod_description_unknow);
	}

	public String getAuthor()
	{
		if (dataBean.author != null)
			return dataBean.author;
		return thisContext.getResources().getString(R.string.nmod_description_unknow);
	}

	public String getVersionName()
	{
		if (dataBean.version_info != null && dataBean.version_info.version_name != null)
			return dataBean.version_info.version_name;
		return thisContext.getResources().getString(R.string.nmod_description_unknow);
	}

	public boolean isBugPack()
	{
		return bugExpection != null;
	}

	public void setBugPack(NModLoadException e)
	{
		bugExpection = e;
	}

	public boolean isActive()
	{
		return isActive;
	}

	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}

	public NModLoadException getLoadException()
	{
		return bugExpection;
	}

	protected void preload()
	{
		this.bugExpection = null;

		this.icon = createIcon();
		if (icon == null)
			icon = BitmapFactory.decodeResource(thisContext.getResources(), R.drawable.mcd_null_pack);

		try
		{
			InputStream is=getAssets().open(TAG_MANIFEST_NAME);
			byte[] buffer=new byte[is.available()];
			is.read(buffer);
			String jsonStr=new String(buffer);
			Gson gson=new Gson();
			NModDataBean theDataBean=gson.fromJson(jsonStr, NModDataBean.class);
			dataBean = theDataBean;
		}
		catch (Exception e)
		{
			dataBean = null;
			setBugPack(NModLoadException.getBadManifestSyntax(e, thisContext.getResources()));
			loader = new NModLoader(this);
			return;
		}

		NModLoadException loadE=findLoadException();
		if (loadE != null)
		{
			dataBean = null;
			setBugPack(loadE);
			loader = new NModLoader(this);
			return;
		}

		try
		{
			this.version_image = createVersionImage();
		}
		catch (NModLoadException nmodle)
		{
			dataBean = null;
			setBugPack(nmodle);
			loader = new NModLoader(this);
			return;
		}

		NModOptions options=new NModOptions(thisContext);
		isActive = options.isActive(this);
		loader = new NModLoader(this);
	}

	protected NMod(Context thisCon)
	{
		thisContext = thisCon;
	}

	public static class NModLanguageBean
	{
		public String name = null;
		public String location = null;
		public boolean format_space = false;
	}

	public static class NModJsonEditBean
	{
		public String path = null;
		public String edit_type = "replace";
		//edit_type = replace / mix
	}

	public static class NModVersionBean
	{
		public String version_name = null;
		public int version_code = -1;
		public String version_description_short = null;
		public String version_description = null;
		public String version_description_image = null;
	}

	public static class NModDataBean
	{
		public String[] native_libs = null;
		public String name = null;
		public String package_name = null;
		public String description = null;
		public String author = null;
		public NModLanguageBean[] languages = null;
		public NModVersionBean version_info = null;
		public boolean check_json_syntax = false;
	}
}
