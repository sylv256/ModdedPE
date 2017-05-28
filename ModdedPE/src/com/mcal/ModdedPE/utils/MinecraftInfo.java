package com.mcal.ModdedPE.utils;
import android.content.*;
import com.mcal.ModdedPE.nmod.*;
import android.content.pm.PackageManager.*;
import android.content.pm.*;
import java.io.*;
import android.os.*;
import android.content.res.*;

public class MinecraftInfo
{
	public static String MC_PACKAGE_NAME = "com.mojang.minecraftpe";
	public static String MC_NATIVE_DIR = "/data/data/com.mojang.minecraftpe/lib";
	
	private Context thisContext;
	private Context mcPkgContext;
	
	private static MinecraftInfo instance;

	public boolean isSupportedMinecraftVersion(String[] versions)
	{
		String mcpeVersionName = getMinecraftVersionName();
		if (mcpeVersionName == null)
			return false;
		for (String nameItem : versions)
		{
			if (nameItem.equals(mcpeVersionName))
				return true;
		}
		return false;
	}

	public String getMinecraftVersionName()
	{
		if (getMinecraftPackageContext() == null)
			return null;
		try
		{
			return thisContext.getPackageManager().getPackageInfo(getMinecraftPackageContext().getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		return null;
	}

	public String getMinecraftNativeLibraryDir()
	{
		return mcPkgContext.getApplicationInfo().nativeLibraryDir;
	}

	public String getMinecraftPackageNativeLibraryDir()
	{
		return Environment.getDataDirectory() + "data" + File.separator + getMinecraftPackageContext().getPackageName() + File.separator + "lib";
	}

	public Context getMinecraftPackageContext()
	{
		return mcPkgContext;
	}
	
	public boolean isMinecraftInstalled()
	{
		return getMinecraftPackageContext() != null;
	}

	private MinecraftInfo(Context context)
	{
		this.thisContext = context;

		try
		{
			mcPkgContext = context.createPackageContext(MC_PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		
		AssetOverrideManager.newInstance();
		if (mcPkgContext != null)
			AssetOverrideManager.getInstance().addAssetOverride(mcPkgContext.getPackageResourcePath());
		AssetOverrideManager.getInstance().addAssetOverride(thisContext.getPackageResourcePath());
	}

	public static MinecraftInfo getInstance(Context context)
	{
		if (instance == null)
			instance = new MinecraftInfo(context);
		return instance;
	}

	public static void initInstance(Context context)
	{
		instance = new MinecraftInfo(context);
	}
	
	public AssetOverrideManager getAssetOverrideManager()
	{
		return AssetOverrideManager.getInstance();
	}
	
	public AssetManager getAssets()
	{
		return getAssetOverrideManager().getAssetManager();
	}
}
