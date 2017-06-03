package com.mcal.ModdedPE.nmod;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.zip.*;

public class ZippedNMod extends NMod
{
	private ZipFile mZipFile = null;
	private File mFilePath = null;
	private AssetManager mAssets = null;

	@Override
	public NModPerloadBean copyNModFiles()
	{
		NModPerloadBean ret = new NModPerloadBean();
		Enumeration<ZipEntry> zipfile_ents = (Enumeration<ZipEntry>) mZipFile.entries();

		ArrayList<String> nativeLibs = new ArrayList<String>();
		while (zipfile_ents.hasMoreElements())
		{
			ZipEntry entry=zipfile_ents.nextElement();

			if (entry == null)
				continue;

			if (!entry.isDirectory() && entry.getName().startsWith("lib" + File.separator + Build.CPU_ABI + File.separator))
			{
				try
				{
					InputStream libInputStream = mZipFile.getInputStream(entry);
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
					nativeLibs.add(outFile.getPath());
				}
				catch (IOException e)
				{}
			}

		}
		ret.native_libs = (String[])nativeLibs.toArray();
		ret.assets_path = getPackageResourcePath();

		return ret;
	}

	@Override
	public int getNModType()
	{
		return NMOD_TYPE_ZIPPED;
	}

	@Override
	public boolean isSupportedABI()
	{

		return false;
	}

	@Override
	public AssetManager getAssets()
	{
		return mAssets;
	}

	@Override
	public String getPackageResourcePath()
	{
		return mFilePath.getPath();
	}

	@Override
	public String getNativeLibsPath()
	{
		return new NModFilePathManager(mContext).getNModLibsDir() + File.separator + getPackageName();
	}

	@Override
	public Bitmap createIcon()
	{
		InputStream imageStream = null;
		try
		{
			imageStream = mZipFile.getInputStream(mZipFile.getEntry("icon.png"));
			Bitmap ret = BitmapFactory.decodeStream(imageStream);
			return ret;
		}
		catch (IOException e)
		{
			return null;
		}
	}

	@Override
	protected InputStream createInfoInputStream()
	{
		try
		{
			return mZipFile.getInputStream(mZipFile.getEntry(MANIFEST_NAME));
		}
		catch (IOException e)
		{
			return null;
		}
	}

	public ZippedNMod(String packageName, Context thisContext, File file) throws IOException
	{
		super(packageName, thisContext);
		this.mZipFile = new ZipFile(file);
		this.mFilePath = file;

		mZipFile.getInputStream(mZipFile.getEntry(MANIFEST_NAME)).close();

		try
		{
			mAssets = AssetManager.class.newInstance();
		}
		catch (InstantiationException e)
		{}
		catch (IllegalAccessException e)
		{}

		try
		{
			Method method=AssetManager.class.getMethod("addAssetPath", String.class);
			method.invoke(mAssets, file.getPath());
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
