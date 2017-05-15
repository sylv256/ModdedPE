package com.mcal.ModdedPE.utils;
import android.content.*;
import com.mcal.ModdedPE.nmod.*;

public class MinecraftInfo
{
	public static String MC_PACKAGE_NAME = "com.mojang.minecraftpe";
	public static String MC_NATIVE_DIR = "/data/data/com.mojang.minecraftpe/lib";
	
	private Context thisContext;
	private Context mcPkgContext;
	private AssetOverrideManager assetOverrideManager;
	private NModManager nmodManager;
	private MinecraftInfo instance;
	
	public boolean isSupportedMinecraftVersion(String[] versions)
	{
		return false;
	}
	
	public String getMinecraftVersionName()
	{
		return null;
	}
	
	public Context getMinecraftPackageContext()
	{
		return null;
	}
	
	public MinecraftInfo getInstance(Context context)
	{
		if(instance == null)
			instance = new MinecraftInfo(context);
		return instance;
	}
	
	private MinecraftInfo(Context context)
	{
		this.thisContext = context;
	}
}
