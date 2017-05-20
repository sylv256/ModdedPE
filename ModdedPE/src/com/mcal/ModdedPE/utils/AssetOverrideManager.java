package com.mcal.ModdedPE.utils;

import android.content.res.*;
import java.lang.reflect.*;

public class AssetOverrideManager
{
	private static AssetOverrideManager instance;
	private AssetManager localManager;

	public void addAssetOverride(String packageResourcePath)
	{
		try
		{
			Method method = AssetManager.class.getMethod("addAssetPath", String.class);
			method.invoke(localManager, packageResourcePath);
		}
		catch (Throwable t)
		{}
	}

	public AssetManager getAssetManager()
	{
		return localManager;
	}

	public static AssetOverrideManager getInstance()
	{
		if (instance == null)
			return instance = new AssetOverrideManager();
		return instance;
	}
	
	public static void newInstance()
	{
		instance = new AssetOverrideManager();
	}
	
	private AssetOverrideManager()
	{
		try
		{
			localManager = AssetManager.class.newInstance();
		}
		catch (InstantiationException e)
		{}
		catch (IllegalAccessException e)
		{}
	}
}
