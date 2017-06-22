package org.mcal.pesdk.utils;

import android.content.res.AssetManager;

import java.lang.reflect.Method;

public class AssetOverrideManager
{
	private static AssetOverrideManager mInstance;
	private AssetManager mLocalAssetManager;

	public void addAssetOverride(String packageResourcePath)
	{
		try
		{
			Method method = AssetManager.class.getMethod("addAssetPath", String.class);
			method.invoke(mLocalAssetManager, packageResourcePath);
		}
		catch (Throwable t)
		{}
	}
	
	public static void addAssetOverride(AssetManager mgr,String packageResourcePath)
	{
		try
		{
			Method method = AssetManager.class.getMethod("addAssetPath", String.class);
			method.invoke(mgr, packageResourcePath);
		}
		catch (Throwable t)
		{}
	}

	public AssetManager getAssetManager()
	{
		return mLocalAssetManager;
	}

	public static AssetOverrideManager getInstance()
	{
		if (mInstance == null)
			return mInstance = new AssetOverrideManager();
		return mInstance;
	}
	
	public static void newInstance()
	{
		mInstance = new AssetOverrideManager();
	}
	
	private AssetOverrideManager()
	{
		try
		{
			mLocalAssetManager = AssetManager.class.newInstance();
		}
		catch (InstantiationException e)
		{}
		catch (IllegalAccessException e)
		{}
	}
}
