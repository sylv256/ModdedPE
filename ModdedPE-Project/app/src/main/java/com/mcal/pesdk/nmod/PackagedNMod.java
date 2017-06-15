package com.mcal.pesdk.nmod;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import java.io.*;
import java.util.*;

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
		ArrayList<String> nativeLibs = new ArrayList<String>();
		ArrayList<String> nativeLibsNeeded = new ArrayList<String>();
		for(NModLibInfo lib_item:mInfo.native_libs_info)
		{
			if(lib_item.mode == NModLibInfo.MODE_ALWAYS)
			{
				nativeLibs.add(lib_item.name);
			}
			else if(lib_item.mode == NModLibInfo.MODE_IF_NEEDED)
			{
				nativeLibsNeeded.add(lib_item.name);
			}
		}

		ret.native_libs = nativeLibs.toArray(new String[0]);
		ret.needed_libs = nativeLibsNeeded.toArray(new String[0]);
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
		return getPackageContext().getDataDir().getAbsolutePath() + File.separator + "lib";
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
