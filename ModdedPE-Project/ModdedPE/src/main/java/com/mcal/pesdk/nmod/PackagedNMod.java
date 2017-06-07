package com.mcal.pesdk.nmod;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import java.io.*;

public class PackagedNMod extends NMod
{
	private Context mPackageContext;

	@Override
	public String getPackageResourcePath()
	{
		return getPackageContext().getPackageResourcePath();
	}

	@Override
	public NModPerloadBean copyNModFiles()
	{
		NModPerloadBean ret = new NModPerloadBean();
		ret.assets_path = getPackageResourcePath();
		return ret;
	}

	@Override
	public boolean isSupportedABI()
	{
		
		return false;
	}
	
	@Override
	public int getNModType()
	{
		return NMOD_TYPE_PACKAGED;
	}
	
	@Override
	public String getNativeLibsPath()
	{
		return getPackageContext().getApplicationInfo().nativeLibraryDir;
	}

	public PackagedNMod(String packageName,Context contextThiz, Context packageContext)
	{
		super(packageName,contextThiz);
		this.mPackageContext = packageContext;
		preload();
	}

	public Context getPackageContext()
	{
		return mPackageContext;
	}

	public AssetManager getAssets()
	{
		return mPackageContext.getAssets();
	}

	public Bitmap createIcon()
	{
		try
		{
			PackageManager packageManager = getPackageContext().getPackageManager();
			PackageInfo packageInfo = null;
			packageInfo = packageManager.getPackageInfo(getPackageContext().getPackageName(), 0);
			int iconRes = packageInfo.applicationInfo.icon;
			return BitmapFactory.decodeResource(getPackageContext().getResources(), iconRes);
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		return null;
	}

	@Override
	protected InputStream createInfoInputStream()
	{
		try
		{
			return getAssets().open(MANIFEST_NAME);
		}
		catch (IOException e)
		{
			return null;
		}
	}
}
