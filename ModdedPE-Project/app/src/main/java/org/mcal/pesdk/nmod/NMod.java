package org.mcal.pesdk.nmod;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

	public abstract NModPreloadBean copyNModFiles() throws IOException;
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
		return getClass().equals(obj.getClass()) && getPackageName().equals(((NMod)obj).getPackageName());
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
			throw new LoadFailedException(LoadFailedException.TYPE_FILE_NOT_FOUND, e);
		}
		if (ret == null)
			throw new LoadFailedException(LoadFailedException.TYPE_DECODE_FAILED,new RuntimeException("Cannot decode banner image file."));

		if (ret.getWidth() != 1024 || ret.getHeight() != 500)
			throw new LoadFailedException(LoadFailedException.TYPE_INVALID_SIZE,new RuntimeException("Invalid image size for banner: width must be 1024,height must be 500."));
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

	public File copyIconToData()
	{
		Bitmap icon = mIcon;
		if (icon == null)
			return null;
		new NModFilePathManager(mContext).getNModIconDir().mkdirs();
		File file = new NModFilePathManager(mContext).getNModIconPath(this);
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			icon.compress(Bitmap.CompressFormat.PNG, 100, baos);
			file.createNewFile();
			FileOutputStream outfile = new FileOutputStream(file);
			outfile.write(baos.toByteArray());
			outfile.close();
			return file;
		}
		catch (IOException ioe)
		{
			return null;
		}
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
		ArrayList<NModWarning> newArray = new ArrayList<>();
		newArray.addAll(mWarnings);
		return newArray;
	}

	protected void checkWarnings()
	{

	}

	public NMod.NModInfo getInfo()
	{
		return mInfo;
	}

	final void preload()
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
			mInfo = gson.fromJson(jsonStr, NModInfo.class);
		}
		catch (JsonSyntaxException e)
		{
			mInfo = null;
			setBugPack(new LoadFailedException(LoadFailedException.TYPE_JSON_SYNTAX, e));
			return;
		}
		catch (IOException ioe)
		{
			mInfo = null;
			setBugPack(new LoadFailedException(LoadFailedException.TYPE_IO_FAILED, ioe));
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
		}
	}

	protected NMod(String packageName, Context context)
	{
		mContext = context;
		mPackageName = packageName;
	}

	public static class NModPreloadBean
	{
		public NModLibInfo[] native_libs;
		public String assets_path;
	}

	public static class NModTextEditBean
	{
		public String path = null;
		public String mode = MODE_REPLACE;

		public static final String MODE_REPLACE = "replace";
		public static final String MODE_APPEND = "append";
		public static final String MODE_PREPEND = "prepend";
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
	}

	public static class NModInfo
	{
		public NModLibInfo[] native_libs_info = null;
		public NModTextEditBean[] text_edit = null;
		public NModJsonEditBean[] json_edit = null;
		public int version_code = -1;
		public String name = null;
		public String package_name = null;
		public String description = null;
		public String author = null;
		public String version_name = null;
		public String banner_title = null;
		public String banner_image_path = null;
		public String change_log = null;
	}
}
