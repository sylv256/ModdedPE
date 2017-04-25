package com.mcal.ModdedPE.nmod;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import java.io.*;
import org.json.*;
import com.mcal.ModdedPE.*;
import java.util.*;
import com.google.gson.*;

public class NMod
{
	private Context packageContext;
	private Context thisContext;
	private NModDataBean dataBean;
	private boolean isActive;
	private NModLoader loader;
	private NModLoadException bugExpection = null ;
	private Bitmap icon;
	private Bitmap version_image;
	public static final String TAG_MANIFEST_NAME = "nmod_manifest.json";

	public String getDescription()
	{
		if(dataBean.description!=null)
			return dataBean.description;
		return thisContext.getResources().getString(R.string.nmod_description_unknow);
	}
	
	public String getAuthor()
	{
		if(dataBean.author!=null)
			return dataBean.author;
		return thisContext.getResources().getString(R.string.nmod_description_unknow);
	}
	
	public String getVersionName()
	{
		if(dataBean.version_info!=null&&dataBean.version_info.version_name!=null)
			return dataBean.version_info.version_name;
		return thisContext.getResources().getString(R.string.nmod_description_unknow);
	}
	
	public boolean isBugPack()
	{
		return bugExpection!=null;
	}
	
	public void setBugPack(NModLoadException e)
	{
		bugExpection=e;
	}
	
	public NModLoadException getLoadException()
	{
		return bugExpection;
	}
	
	private NModLoadException findLoadException()
	{
		NModLoadException ejson=checkJSONs();
		if(ejson!=null)
			return ejson;
		if(dataBean.languages!=null)
		{
			for(NModLanguageBean lang:dataBean.languages)
			{
				try
				{
					getAsset().open(lang.location);
				}
				catch (IOException e)
				{
					return NModLoadException.getFileNotFound(e,thisContext.getResources(),lang.location);
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
			this.mgr=mgr;
			this.exploredFiles=new Vector<String>();
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
						if(oldPath==null||oldPath.isEmpty())
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
	
	private static class EmptyClass{}
	
	private NModLoadException checkJSONs()
	{
		Vector<String> allFiles=new LoopFileSearcher(getAsset()).getAllFiles();
		if(allFiles==null)
			return null;
		
		for(String path:allFiles)
		{
			if(path.toLowerCase().endsWith(".json"))
			{
				try
				{
					InputStream is = getAsset().open(path);
					byte[] buffer=new byte[is.available()];
					is.read(buffer);
					String jsonStr=new String(buffer);
					try
					{
						new Gson().fromJson(jsonStr,EmptyClass.class);
					}
					catch (Throwable t)
					{
						return NModLoadException.getBadJsonGrammar(t, thisContext.getResources(), path);
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
	
	public NMod(Context packageContext,Context contextThiz)
	{
		this.packageContext=packageContext;
		this.thisContext=contextThiz;
		this.bugExpection=null;
		
		this.icon=createIcon();
		if(icon==null)
			icon=BitmapFactory.decodeResource(contextThiz.getResources(),R.drawable.mcd_null_pack);
		
		try
		{
			InputStream is=packageContext.getAssets().open(TAG_MANIFEST_NAME);
			byte[] buffer=new byte[is.available()];
			is.read(buffer);
			String jsonStr=new String(buffer);
			Gson gson=new Gson();
			NModDataBean theDataBean=gson.fromJson(jsonStr,NModDataBean.class);
			dataBean=theDataBean;
		}
		catch (Exception e)
		{
			dataBean=null;
			setBugPack(NModLoadException.getBadManifestGrammar(e,thisContext.getResources()));
			loader=new NModLoader(this);
			return;
		}
		
		NModLoadException loadE=findLoadException();
		if(loadE!=null)
		{
			dataBean=null;
			setBugPack(loadE);
			loader=new NModLoader(this);
			return;
		}
		
		try
		{
			this.version_image=createVersionImage();
		}
		catch(NModLoadException nmodle)
		{
			dataBean=null;
			setBugPack(nmodle);
			loader=new NModLoader(this);
			return;
		}
		
		NModOptions options=new NModOptions(contextThiz);
		isActive=options.isActive(this);
		loader=new NModLoader(this);
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void setActive(boolean isActive)
	{
		this.isActive=isActive;
	}
	
	public Context getPackageContext()
	{
		return packageContext;
	}
	
	public AssetManager getAsset()
	{
		return packageContext.getAssets();
	}
	
	public String getPackageName()
	{
		if(getPackageContext()==null)
			return toString();
		return getPackageContext().getPackageName();
	}
	
	public Bitmap createIcon()
	{
		try
		{
			PackageManager packageManager = getPackageContext().getPackageManager();
			PackageInfo packageInfo = null;
			packageInfo = packageManager.getPackageInfo(getPackageContext().getPackageName(), 0);
			int iconRes = packageInfo.applicationInfo.icon;
			return BitmapFactory.decodeResource(getPackageContext().getResources(),iconRes);
		}
		catch (PackageManager.NameNotFoundException e){}
		return null;
	}
	
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
		if(isBugPack())
			return getPackageName();
		return dataBean.name;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(getClass()==obj.getClass())
			return getPackageName().equals(((NMod)obj).getPackageName());
		return false;
	}
	
	public Bitmap createVersionImage() throws NModLoadException
	{
		Bitmap ret = null;
		try
		{
			if(dataBean.version_info==null||dataBean.version_info.version_description_image==null)
				return null;
			InputStream is = getAsset().open(dataBean.version_info.version_description_image);
			ret = BitmapFactory.decodeStream(is);
		}
		catch (IOException e)
		{
			throw NModLoadException.getFileNotFound(e,thisContext.getResources(),dataBean.version_info.version_description_image);
		}
		catch(Throwable t)
		{
			throw NModLoadException.getImageDecode(t,thisContext.getResources(),dataBean.version_info.version_description_image);
		}
		if(ret==null)
			throw NModLoadException.getImageDecode(null,thisContext.getResources(),dataBean.version_info.version_description_image);
		
		if(ret.getWidth()!=1024||ret.getHeight()!=500)
			throw NModLoadException.getBadImageSize(thisContext.getResources());
		return ret;
	}
	
	public Bitmap getVersionImage()
	{
		return version_image;
	}
	
	public String getNewsTitle()
	{
		if(dataBean.version_info!=null&&dataBean.version_info.version_description_short!=null)
			return dataBean.name+" : "+dataBean.version_info.version_description_short;
		return null;
	}
	
	public boolean isValidNews()
	{
		return getVersionImage()!=null&&getNewsTitle()!=null;
	}
	
	public static class NModLanguageBean
	{
		public String name;
		public String location;
		public boolean format_space;
	}
	
	public static class NModVersionBean
	{
		public String version_name;
		public int version_code;
		public String version_description_short;
		public String version_description;
		public String version_description_image;
	}
	
	public NModLanguageBean[] getLanguageBeans()
	{
		return dataBean.languages;
	}
	
	public static class NModDataBean
	{
		public String[] native_libs;
		public String name;
		public String description;
		public String author;
		public NModLanguageBean[] languages;
		public NModVersionBean version_info;
	}
}
