package com.mcal.pesdk.utils;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.os.*;
import java.io.*;

public class MinecraftInfo
{
	public static String MC_PACKAGE_NAME = "com.mojang.minecraftpe";
	public static String MC_NATIVE_DIR = "/data/data/com.mojang.minecraftpe/lib";
	
	private Context mContext;
	private Context mMCContext;
	
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
			return mContext.getPackageManager().getPackageInfo(getMinecraftPackageContext().getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		return null;
	}

	public String getMinecraftPackageNativeLibraryDir()
	{
		return mMCContext.getApplicationInfo().nativeLibraryDir;
	}

	public Context getMinecraftPackageContext()
	{
		return mMCContext;
	}
	
	public boolean isMinecraftInstalled()
	{
		return getMinecraftPackageContext() != null;
	}

	public MinecraftInfo(Context context)
	{
		this.mContext = context;

		try
		{
			mMCContext = context.createPackageContext(MC_PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		
		AssetOverrideManager.newInstance();
		if (mMCContext != null)
			AssetOverrideManager.getInstance().addAssetOverride(mMCContext.getPackageResourcePath());
		AssetOverrideManager.getInstance().addAssetOverride(mContext.getPackageResourcePath());
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
