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
			if (localManager == null)
				localManager = AssetManager.class.newInstance();
		}
		catch (Throwable e)
		{}

		try
		{
			Method method = AssetManager.class.getMethod("addAssetPath", String.class);
			method.invoke(localManager, packageResourcePath);
		}
		catch (Throwable t)
		{}
	}

	public AssetManager getLocalAssetManager()
	{
		return localManager;
	}

	public static AssetOverrideManager getInstance()
	{
		if (instance == null)
			return instance = new AssetOverrideManager();
		return instance;
	}
}
