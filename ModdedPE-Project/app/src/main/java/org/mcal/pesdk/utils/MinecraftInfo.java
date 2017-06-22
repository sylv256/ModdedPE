package org.mcal.pesdk.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;

public class MinecraftInfo
{
	private static String MC_PACKAGE_NAME = "com.mojang.minecraftpe";

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

	public MinecraftInfo(Context context, LauncherOptions options)
	{
		this.mContext = context;

		String mMinecraftPackageName = MC_PACKAGE_NAME;
		if (!options.getMinecraftPEPackageName().equals(LauncherOptions.STRING_VALUE_DEFAULT))
			mMinecraftPackageName = options.getMinecraftPEPackageName();

		try
		{
			mMCContext = context.createPackageContext(mMinecraftPackageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
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
