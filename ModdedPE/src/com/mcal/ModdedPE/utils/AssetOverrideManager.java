package com.mcal.ModdedPE.utils;

import android.content.res.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

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
		catch (InstantiationException e)
		{}
		catch (IllegalAccessException e)
		{}
		
		try
		{
			Method method=AssetManager.class.getMethod("addAssetPath",String.class);
			method.invoke(localManager, packageResourcePath);
		}
		catch (NoSuchMethodException e)
		{}
		catch (SecurityException e)
		{}
		catch (IllegalAccessException e)
		{}
		catch (IllegalArgumentException e)
		{}
		catch (InvocationTargetException e)
		{}
		catch(Throwable t)
		{}
	}

	public AssetManager getLocalAssetManager()
	{
		return localManager;
	}

	public static AssetOverrideManager getInstance()
	{
		if(instance==null)
			return instance=new AssetOverrideManager();
		return instance;
	}
}
