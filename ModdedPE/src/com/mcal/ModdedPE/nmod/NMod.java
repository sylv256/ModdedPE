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
	protected boolean mEnabled;
	protected Bitmap icon;
	protected Bitmap banner_image;

	public static final String MANIFEST_NAME = "nmod_manifest.json";
	public static final int NMOD_TYPE_ZIPPED = 1;
	public static final int NMOD_TYPE_PACKAGED = 2;

	public abstract NModPerloadBean copyNModFiles();
	public abstract String getPackageName();
	public abstract AssetManager getAssets();
	public abstract String getPackageResourcePath();
	public abstract String getNativeLibsPath();
	public abstract int getNModType();
	public abstract boolean isSupportedABI();
	protected abstract Bitmap createIcon();
	protected abstract InputStream createDataBeanInputStream();

	public String[] getNativeLibs()
	{
		return dataBean.native_libs;
	}

	public String getName()
	{
		if (isBugPack())
			return getPackageName();
		if (dataBean == null || dataBean.name == null)
			return getPackageName();
		return dataBean.name;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (getClass().equals(obj.getClass()))
			return getPackageName().equals(((NMod)obj).getPackageName());
		return false;
	}

	public Bitmap createBannerImage() throws NModLoadException
	{
		Bitmap ret = null;
		try
		{
			if (dataBean == null || dataBean.banner_image_path == null)
				return null;
			InputStream is = getAssets().open(dataBean.banner_image_path);
			ret = BitmapFactory.decodeStream(is);
		}
		catch (IOException e)
		{
			throw new NModLoadException("Cannot create nmod banner image.", e);
		}
		if (ret == null)
			throw new NModLoadException("Cannot decode banner image:" + dataBean.banner_image_path + ".Please make sure it is a valid png or jpg format image file.");

		if (ret.getWidth() != 1024 || ret.getHeight() != 500)
			throw new NModLoadException("Bad nmod banner image size.Banner image must be 1024(width)*500(height).");
		return ret;
	}

	public Bitmap getBannerImage()
	{
		return banner_image;
	}

	public String getBannerTitle()
	{
		if (dataBean != null && dataBean.banner_title != null)
			return getName() + " : " + dataBean.banner_title;
		return getName();
	}

	public boolean isValidBanner()
	{
		return getBannerImage() != null;
	}

	public NModLanguageBean[] getLanguageBeans()
	{
		return dataBean.languages;
	}

	private NModLoadException findLoadException()
	{
		if (dataBean.languages != null)
		{
			for (NModLanguageBean lang:dataBean.languages)
			{
				if (lang.name == null || lang.name.isEmpty())
					return new NModLoadException("Element \"name\" of one of the language data items is invalid.");
				if (lang.path == null || lang.path.isEmpty())
					return new NModLoadException("Element \"path\" of language data item:\"" + lang.name + "\" is invalid.");

				try
				{
					getAssets().open(lang.path).close();
				}
				catch (IOException e)
				{
					return new NModLoadException("Cannot find language file:" + lang.path, e);
				}
			}
		}

		return null;
	}

	public Bitmap getIcon()
	{
		if (icon == null)
		{
			return BitmapFactory.decodeResource(thisContext.getResources(), R.drawable.mcd_null_pack);
		}
		return icon;
	}

	public String getDescription()
	{
		if (dataBean != null && dataBean.description != null)
			return dataBean.description;
		return thisContext.getResources().getString(R.string.nmod_description_unknow);
	}

	public String getAuthor()
	{
		if (dataBean != null && dataBean.author != null)
			return dataBean.author;
		return thisContext.getResources().getString(R.string.nmod_description_unknow);
	}

	public String getVersionName()
	{
		if (dataBean != null && dataBean.version_name != null)
			return dataBean.version_name;
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

	public boolean isEnabled()
	{
		return mEnabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.mEnabled = enabled;
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
			InputStream is = createDataBeanInputStream();
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String jsonStr = new String(buffer);
			Gson gson = new Gson();
			NModDataBean theDataBean = gson.fromJson(jsonStr, NModDataBean.class);
			dataBean = theDataBean;
		}
		catch (JsonSyntaxException e)
		{
			dataBean = null;
			setBugPack(new NModLoadException("Read json " + MANIFEST_NAME + " failed.", e));
			return;
		}
		catch (IOException ioe)
		{
			dataBean = null;
			setBugPack(new NModLoadException("IO failed: Cannot read " + MANIFEST_NAME, ioe));
			return;
		}

		NModLoadException loadE = findLoadException();
		if (loadE != null)
		{
			dataBean = null;
			setBugPack(loadE);
			return;
		}

		try
		{
			this.banner_image = createBannerImage();
		}
		catch (NModLoadException nmodle)
		{
			dataBean = null;
			setBugPack(nmodle);
			return;
		}

		NModOptions options = new NModOptions(thisContext);
		mEnabled = options.isActive(this);
	}

	protected NMod(Context thisCon)
	{
		thisContext = thisCon;
	}

	public static class NModPerloadBean
	{
		String[] native_libs;
		String dex_path;
		String assets_path;
	}

	public static class NModLanguageBean
	{
		public String name = null;
		public String path = null;
		public boolean format_space = false;
	}

	public static class NModJsonEditBean
	{
		public String path = null;
		public String mode = "replace";
		//mode = replace / merge
	}

	public static class NModDataBean
	{
		public String[] native_libs = null;
		public String name = null;
		public String package_name = null;
		public String icon_path = null;
		public String description = null;
		public String author = null;
		public NModLanguageBean[] languages = null;
		public String version_name = null;
		public int version_code = -1;
		public String banner_title = null;
		public String banner_image_path = null;
		public String version_info = null;
		public NModJsonEditBean[] json_edit = null;
		public String[] parents_package_names = null;
		public String[] target_mcpe_versions = null;
		public String check_target_version_mode = "no_check";
		//check_target_version_mode = no_check / must_target / show_warning_if_not_target
	}
}
