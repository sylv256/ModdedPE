package com.mcal.ModdedPE.nmod;
import android.content.*;
import android.content.res.*;
import java.util.*;
import java.io.*;
import android.graphics.*;
import com.mcal.ModdedPE.nmod.NMod.*;
import com.mcal.ModdedPE.*;
import com.google.gson.*;
import android.content.pm.*;

public class PackagedNMod extends NMod
{
	private Context packageContext;

	@Override
	public String getPackageResourcePath()
	{
		return getPackageContext().getPackageResourcePath();
	}

	@Override
	public void load(String mcVer, String moddedpeVer) throws Exception
	{
		getLoader().load(mcVer, moddedpeVer);
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

	public PackagedNMod(Context contextThiz, Context packageContext)
	{
		super(contextThiz);
		this.packageContext = packageContext;
		preload();
	}

	public Context getPackageContext()
	{
		return packageContext;
	}

	public AssetManager getAssets()
	{
		return packageContext.getAssets();
	}

	public String getPackageName()
	{
		if (dataBean != null && dataBean.package_name != null)
			return dataBean.package_name;
		if (getPackageContext() == null)
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
			return BitmapFactory.decodeResource(getPackageContext().getResources(), iconRes);
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		return null;
	}

	@Override
	protected InputStream createDataBeanInputStream()
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
