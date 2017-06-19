package org.mcal.pesdk.nmod;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PackagedNMod extends NMod
{
	private Context mPackageContext;

	@Override
	public String getPackageResourcePath()
	{
		return getPackageContext().getPackageResourcePath();
	}

	@Override
	public NModPreloadBean copyNModFiles()
	{
		NModPreloadBean ret = new NModPreloadBean();
		ret.assets_path = getPackageResourcePath();
		ArrayList<NModLibInfo> nativeLibs = new ArrayList<>();
		for(NModLibInfo lib_item:mInfo.native_libs_info)
		{
			NModLibInfo newInfo = new NModLibInfo();
			newInfo.name = getNativeLibsPath() + File.separator + lib_item.name;
			newInfo.use_api = lib_item.use_api;
			nativeLibs.add(newInfo);
		}

		ret.native_libs = nativeLibs.toArray(new NModLibInfo[0]);
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
	
	public String getNativeLibsPath()
	{
		return getPackageContext().getFilesDir().getParentFile().getAbsolutePath() + File.separator + "lib";
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
		{
			e.printStackTrace();
		}
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
