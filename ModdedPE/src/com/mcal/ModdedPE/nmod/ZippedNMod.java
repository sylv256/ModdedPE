package com.mcal.ModdedPE.nmod;
import android.content.*;
import android.graphics.*;
import com.mcal.ModdedPE.nmod.NMod.*;
import java.io.*;
import android.content.res.*;
import java.util.zip.*;
import java.lang.reflect.*;
import android.os.*;
import java.util.*;
import com.mcal.ModdedPE.utils.*;

public class ZippedNMod extends NMod
{
	private ZipFile zipFile;
	private File filePath;
	private AssetManager assets;
	@Override
	public void load(String mcVer, String moddedpeVer) throws Exception
	{
		copyNativeLibs();
		getLoader().load(mcVer, moddedpeVer);
	}

	private void copyNativeLibs()
	{
		Enumeration<ZipEntry> zipfile_ents = (Enumeration<ZipEntry>) zipFile.entries();
		while (zipfile_ents.hasMoreElements())
		{
			ZipEntry entry=zipfile_ents.nextElement();

			if (!entry.isDirectory() && entry.getName().startsWith("libs/" + Build.CPU_ABI2 + File.separator))
			{
				try
				{
					InputStream libInputStream = zipFile.getInputStream(entry);
					int byteReaded = -1;
					byte[] buffer = new byte[1024];
					File dirFile = new File(getNativeLibsPath());
					dirFile.mkdirs();
					File outFile = new File(getNativeLibsPath() + File.separator + entry.getName().substring(entry.getName().lastIndexOf(File.separator) + 1));
					outFile.createNewFile();
					FileOutputStream writerStream = new FileOutputStream(outFile);
					while ((byteReaded = libInputStream.read(buffer)) != -1)
					{
						writerStream.write(buffer, 0, byteReaded);
					}
					libInputStream.close();
					writerStream.close();
				}
				catch (IOException e)
				{}
			}
			if (!entry.isDirectory() && entry.getName().startsWith("libs/" + Build.CPU_ABI + File.separator))
			{
				try
				{
					InputStream libInputStream = zipFile.getInputStream(entry);
					int byteReaded = -1;
					byte[] buffer = new byte[1024];
					File dirFile = new File(getNativeLibsPath());
					dirFile.mkdirs();
					File outFile = new File(getNativeLibsPath() + File.separator + entry.getName().substring(entry.getName().lastIndexOf(File.separator) + 1));
					outFile.createNewFile();
					FileOutputStream writerStream = new FileOutputStream(outFile);
					while ((byteReaded = libInputStream.read(buffer)) != -1)
					{
						writerStream.write(buffer, 0, byteReaded);
					}
					libInputStream.close();
					writerStream.close();
				}
				catch (IOException e)
				{}
			}

		}
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
		return new FilePathManager(thisContext).getNModLibsPath() + File
.separator + getPackageName();
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
