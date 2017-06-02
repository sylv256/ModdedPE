package com.mcal.ModdedPE.nmod;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import com.google.gson.*;
import com.mcal.ModdedPE.*;
import java.io.*;

public abstract class NMod
{
	protected Context mContext;
	protected LoadFailedException mBugExpection = null ;
	protected NModInfo mInfo;
	private Bitmap mIcon;
	private Bitmap mBanner_image;
	private String mPackageName;
	
	public static final String MANIFEST_NAME = "nmod_manifest.json";
	public static final int NMOD_TYPE_ZIPPED = 1;
	public static final int NMOD_TYPE_PACKAGED = 2;

	public abstract NModPerloadBean copyNModFiles();
	public abstract AssetManager getAssets();
	public abstract String getPackageResourcePath();
	public abstract String getNativeLibsPath();
	public abstract int getNModType();
	public abstract boolean isSupportedABI();
	protected abstract Bitmap createIcon();
	protected abstract InputStream createInfoInputStream();

	public String getPackageName()
	{
		return mPackageName;
	}
	
	public String[] getNativeLibs()
	{
		return mInfo.native_libs;
	}

	public String getName()
	{
		if (isBugPack())
			return getPackageName();
		if (mInfo == null || mInfo.name == null)
			return getPackageName();
		return mInfo.name;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (getClass().equals(obj.getClass()))
			return getPackageName().equals(((NMod)obj).getPackageName());
		return false;
	}

	public Bitmap createBannerImage() throws LoadFailedException
	{
		Bitmap ret = null;
		try
		{
			if (mInfo == null || mInfo.banner_image_path == null)
				return null;
			InputStream is = getAssets().open(mInfo.banner_image_path);
			ret = BitmapFactory.decodeStream(is);
		}
		catch (IOException e)
		{
			throw new LoadFailedException("Cannot create nmod banner image.", e);
		}
		if (ret == null)
			throw new LoadFailedException("Cannot decode banner image:" + mInfo.banner_image_path + ".Please make sure it is a valid png or jpg format image file.");

		if (ret.getWidth() != 1024 || ret.getHeight() != 500)
			throw new LoadFailedException("Bad nmod banner image size.Banner image must be 1024(width)*500(height).");
		return ret;
	}

	public Bitmap getBannerImage()
	{
		return mBanner_image;
	}

	public String getBannerTitle()
	{
		if (mInfo != null && mInfo.banner_title != null)
			return getName() + " : " + mInfo.banner_title;
		return getName();
	}

	public boolean isValidBanner()
	{
		return getBannerImage() != null;
	}

	public NModLanguageBean[] getLanguageBeans()
	{
		return mInfo.languages;
	}

	private LoadFailedException findLoadException()
	{
		if (mInfo.languages != null)
		{
			for (NModLanguageBean lang:mInfo.languages)
			{
				if (lang.name == null || lang.name.isEmpty())
					return new LoadFailedException("Element \"name\" of one of the language data items is invalid.");
				if (lang.path == null || lang.path.isEmpty())
					return new LoadFailedException("Element \"path\" of language data item:\"" + lang.name + "\" is invalid.");

				try
				{
					getAssets().open(lang.path).close();
				}
				catch (IOException e)
				{
					return new LoadFailedException("Cannot find language file:" + lang.path, e);
				}
			}
		}

		return null;
	}

	public Bitmap getIcon()
	{
		if (mIcon == null)
		{
			return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mcd_null_pack);
		}
		return mIcon;
	}

	public String getDescription()
	{
		if (mInfo != null && mInfo.description != null)
			return mInfo.description;
		return mContext.getResources().getString(R.string.nmod_description_unknow);
	}

	public String getAuthor()
	{
		if (mInfo != null && mInfo.author != null)
			return mInfo.author;
		return mContext.getResources().getString(R.string.nmod_description_unknow);
	}

	public String getVersionName()
	{
		if (mInfo != null && mInfo.version_name != null)
			return mInfo.version_name;
		return mContext.getResources().getString(R.string.nmod_description_unknow);
	}

	public boolean isBugPack()
	{
		return mBugExpection != null;
	}

	public void setBugPack(LoadFailedException e)
	{
		mBugExpection = e;
	}

	public LoadFailedException getLoadException()
	{
		return mBugExpection;
	}

	protected void preload()
	{
		this.mBugExpection = null;

		this.mIcon = createIcon();
		if (mIcon == null)
			mIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mcd_null_pack);

		try
		{
			InputStream is = createInfoInputStream();
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String jsonStr = new String(buffer);
			Gson gson = new Gson();
			NModInfo theInfo = gson.fromJson(jsonStr, NModInfo.class);
			mInfo = theInfo;
		}
		catch (JsonSyntaxException e)
		{
			mInfo = null;
			setBugPack(new LoadFailedException("Read json " + MANIFEST_NAME + " failed.", e));
			return;
		}
		catch (IOException ioe)
		{
			mInfo = null;
			setBugPack(new LoadFailedException("IO failed: Cannot read " + MANIFEST_NAME, ioe));
			return;
		}

		LoadFailedException loadE = findLoadException();
		if (loadE != null)
		{
			mInfo = null;
			setBugPack(loadE);
			return;
		}

		try
		{
			this.mBanner_image = createBannerImage();
		}
		catch (LoadFailedException nmodle)
		{
			mInfo = null;
			setBugPack(nmodle);
			return;
		}
	}

	protected NMod(String packageName,Context thisCon)
	{
		mContext = thisCon;
		mPackageName = packageName;
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

	public static class NModInfo
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
