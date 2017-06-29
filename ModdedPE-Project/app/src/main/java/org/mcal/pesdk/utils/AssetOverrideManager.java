package org.mcal.pesdk.utils;

import android.content.res.AssetManager;

import java.lang.reflect.Method;

public class AssetOverrideManager
{
	private static AssetOverrideManager mInstance;
	private AssetManager mLocalAssetManager;

	void addAssetOverride(String packageResourcePath)
	{
		try
		{
			Method method = AssetManager.class.getMethod("addAssetPath", String.class);
			method.invoke(mLocalAssetManager, packageResourcePath);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	public static void addAssetOverride(AssetManager mgr,String packageResourcePath)
	{
		try
		{
			Method method = AssetManager.class.getMethod("addAssetPath", String.class);
			method.invoke(mgr, packageResourcePath);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	AssetManager getAssetManager()
	{
		return mLocalAssetManager;
	}

	static AssetOverrideManager getInstance()
	{
		if (mInstance == null)
			return mInstance = new AssetOverrideManager();
		return mInstance;
	}
	
	static void newInstance()
	{
		mInstance = new AssetOverrideManager();
	}
	
	private AssetOverrideManager()
	{
		try
		{
			mLocalAssetManager = AssetManager.class.newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
