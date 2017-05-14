package com.mcal.ModdedPE.nmod;
import android.content.*;
import android.graphics.*;
import com.mcal.ModdedPE.nmod.NMod.*;
import java.io.*;
import android.content.res.*;
import java.util.zip.*;
import java.lang.reflect.*;
import android.os.*;

public class ZippedNMod extends NMod
{
	private ZipFile zipFile;
	private File filePath;
	private AssetManager assets;
	@Override
	public void load(String mcVer, String moddedpeVer) throws Exception
	{
		copyNativeLibs();
		getLoader().load(mcVer,moddedpeVer);
	}

	private void copyNativeLibs()
	{
		String cpu_abi = Build.CPU_ABI;
		String cpu_abi2 = Build.CPU_ABI2;
	}

	@Override
	public String getPackageName()
	{
		if (dataBean != null && dataBean.package_name != null)
			return dataBean.package_name;
		return toString();
	}

	@Override
	public AssetManager getAssets()
	{
		return assets;
	}

	@Override
	public String getPackageResourcePath()
	{
		return filePath.getPath();
	}

	@Override
	public String getNativeLibsPath()
	{
		return "/data/data/" + thisContext.getPackageName() + "/files/nmod_libs/" + getPackageName();
	}

	@Override
	public Bitmap createIcon()
	{
		InputStream imageStream = null;
		try
		{
			imageStream = zipFile.getInputStream(zipFile.getEntry("icon.png"));
			Bitmap ret = BitmapFactory.decodeStream(imageStream);
			return ret;
		}
		catch (IOException e)
		{
			return null;
		}
	}

	public ZippedNMod(Context thisContext, File file) throws IOException
	{
		super(thisContext);
		this.zipFile = new ZipFile(file);
		this.filePath = file;
		try
		{
			assets = AssetManager.class.newInstance();
		}
		catch (InstantiationException e)
		{}
		catch (IllegalAccessException e)
		{}
		
		try
		{
			Method method=AssetManager.class.getMethod("addAssetPath", String.class);
			method.invoke(assets, getPackageResourcePath());
		}
		catch (NoSuchMethodException e)
		{}
		catch (SecurityException e)
		{}
		catch (InvocationTargetException e)
		{}
		catch (IllegalAccessException e)
		{}
		catch (IllegalArgumentException e)
		{}
		preload();
	}
}
