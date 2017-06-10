package com.mcal.pesdk.nmod;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import com.google.gson.*;
import java.io.*;
import java.util.*;

public abstract class NMod
{
	protected Context mContext;
	protected NModInfo mInfo;
	private LoadFailedException mBugExpection = null ;
	private ArrayList<NModWarning> mWarnings = new ArrayList<NModWarning>();
	private Bitmap mIcon;
	private Bitmap mBannerImage;
	private String mPackageName;
	
	public static final String MANIFEST_NAME = "nmod_manifest.json";
	public static final int NMOD_TYPE_ZIPPED = 1;
	public static final int NMOD_TYPE_PACKAGED = 2;

	public abstract NModPerloadBean copyNModFiles();
	public abstract AssetManager getAssets();
	public abstract String getPackageResourcePath();
	public abstract int getNModType();
	public abstract boolean isSupportedABI();
	protected abstract Bitmap createIcon();
	protected abstract InputStream createInfoInputStream();

	public final String getPackageName()
	{
		return mPackageName;
	}

	public final String getName()
	{
		if (isBugPack())
			return getPackageName();
		if (mInfo == null || mInfo.name == null)
			return getPackageName();
		return mInfo.name;
	}

	@Override
	public final boolean equals(Object obj)
	{
		if (getClass().equals(obj.getClass()))
			return getPackageName().equals(((NMod)obj).getPackageName());
		return false;
	}

	public final Bitmap createBannerImage() throws LoadFailedException
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

	public final Bitmap getBannerImage()
	{
		return mBannerImage;
	}

	public final String getBannerTitle()
	{
		if (mInfo != null && mInfo.banner_title != null)
			return getName() + " : " + mInfo.banner_title;
		return getName();
	}

	public final boolean isValidBanner()
	{
		return getBannerImage() != null;
	}

	public final NModLanguageBean[] getLanguageBeans()
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

	public final Bitmap getIcon()
	{
		return mIcon;
	}

	public final String getDescription()
	{
		if (mInfo != null && mInfo.description != null)
			return mInfo.description;
		return mContext.getResources().getString(android.R.string.unknownName);
	}

	public final String getAuthor()
	{
		if (mInfo != null && mInfo.author != null)
			return mInfo.author;
		return mContext.getResources().getString(android.R.string.unknownName);
	}

	public final String getVersionName()
	{
		if (mInfo != null && mInfo.version_name != null)
			return mInfo.version_name;
		return mContext.getResources().getString(android.R.string.unknownName);
	}

	public final boolean isBugPack()
	{
		return mBugExpection != null;
	}

	public final void setBugPack(LoadFailedException e)
	{
		mBugExpection = e;
	}

	public final LoadFailedException getLoadException()
	{
		return mBugExpection;
	}
	
	public final void addWarning(NModWarning warning)
	{
		mWarnings.add(warning);
	}
	
	public final ArrayList<NModWarning> getWarnings()
	{
		ArrayList<NModWarning> newArray = new ArrayList<NModWarning>();
		newArray.addAll(mWarnings);
		return newArray;
	}
	
	protected void checkWarnings()
	{
		
	}

	protected final void preload()
	{
		this.mBugExpection = null;

		this.mIcon = createIcon();
		
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
			this.mBannerImage = createBannerImage();
		}
		catch (LoadFailedException nmodle)
		{
			mInfo = null;
			setBugPack(nmodle);
			return;
		}
	}

	protected NMod(String packageName,Context context)
	{
		mContext = context;
		mPackageName = packageName;
	}

	public static class NModPerloadBean
	{
		public String[] native_libs;
		public String[] needed_libs;
		public String dex_path;
		public String assets_path;
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
		public String mode = MODE_REPLACE;
		
		public static final String MODE_REPLACE = "replace";
		public static final String MODE_MERGE = "merge";
	}
	
	public static class NModLibInfo
	{
		public boolean use_api = false;
		public String name = null;
		public String mode = MODE_ALWAYS;
		
		public static final String MODE_ALWAYS = "always";
		public static final String MODE_IF_NEEDED = "if_needed";
	}

	public static class NModInfo
	{
		public NModLibInfo[] native_libs_info = null;
		public NModLanguageBean[] languages = null;
		public NModJsonEditBean[] json_edit = null;
		public String[] parents_package_names = null;
		public String[] target_mcpe_versions = null;
		public int version_code = -1;
		public String name = null;
		public String package_name = null;
		public String icon_path = null;
		public String description = null;
		public String author = null;
		public String version_name = null;
		public String banner_title = null;
		public String banner_image_path = null;
		public String change_log = null;
		public String check_target_version_mode = "no_check";
		
		public static final String CHECK_MODE_NEVER = "never";
		public static final String CHECK_MODE_ALWAYS = "always";
		public static final String CHECK_MODE_SHOW_WARNING = "show_warning";
	}
}
