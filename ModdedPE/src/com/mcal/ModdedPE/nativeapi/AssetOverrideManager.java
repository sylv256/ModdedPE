package com.mcal.ModdedPE.nativeapi;
import android.content.res.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class AssetOverrideManager
{
	public static AssetOverrideManager instance;
	
	private Vector<AssetManager> assetManagers;
	private AssetManager localManager;
	
	public void init()
	{
		assetManagers=new Vector<AssetManager>();
	}
	
	public void addAssetOverride(AssetManager am,String packagePath)
	{
		assetManagers.add(am);
		if(localManager==null)
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
		addAssetOverride(packagePath);
	}

	private void addAssetOverride(String packageResourcePath)
	{
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
	}

	public final InputStream openFile(String name)
	{
		InputStream inputStream = null;
		for(int i=assetManagers.size()-1;i>=0;--i)
		{
			try
			{
				inputStream = assetManagers.get(i).open(name);
			}
			catch (IOException e)
			{}
			if(inputStream!=null)
				return inputStream;
		}
		return null;
	}
	
	public final InputStream openFile(String name,int mode)
	{
		InputStream inputStream = null;
		for(int i=assetManagers.size()-1;i>=0;--i)
		{
			try
			{
				inputStream = assetManagers.get(i).open(name,mode);
			}
			catch (IOException e)
			{}
			if(inputStream!=null)
				return inputStream;
		}
		return null;
	}
	
	public AssetOverrideManager()
	{
		init();
	}
	
	public AssetManager getLocalAssetManager()
	{
		return localManager;
	}
	
	static
	{
		instance=new AssetOverrideManager();
	}
}
