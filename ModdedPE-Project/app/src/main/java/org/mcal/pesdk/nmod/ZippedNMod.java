package org.mcal.pesdk.nmod;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.mcal.pesdk.ABIInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.*;
import java.util.zip.ZipInputStream;

public class ZippedNMod extends NMod
{
	private ZipFile mZipFile = null;
	private File mFilePath = null;
	private AssetManager mAssets = null;

	@Override
	public NModPreloadBean copyNModFiles() throws IOException
	{
		NModPreloadBean ret = new NModPreloadBean();
		ZipInputStream zipInput = new ZipInputStream(new BufferedInputStream(new FileInputStream(mFilePath.getAbsolutePath())));
		ZipEntry entry = null;

		new File(getNativeLibsPath()).mkdirs();
		while ((entry = zipInput.getNextEntry()) != null)
		{
			if (!entry.isDirectory() && entry.getName().startsWith("lib" + File.separator + ABIInfo.getTargetABIType() + File.separator))
			{
				InputStream libInputStream = mZipFile.getInputStream(entry);
				int byteRead = -1;
				byte[] buffer = new byte[1024];
				File outFile = new File(getNativeLibsPath() + File.separator + entry.getName().substring(entry.getName().lastIndexOf(File.separator) + 1));
				outFile.createNewFile();
				FileOutputStream writerStream = new FileOutputStream(outFile);
				while ((byteRead = libInputStream.read(buffer)) != -1)
				{
					writerStream.write(buffer, 0, byteRead);
				}
				libInputStream.close();
				writerStream.close();
			}

		}

		zipInput.close();

		ArrayList<NModLibInfo> nativeLibs = new ArrayList<>();
		if (mInfo != null && mInfo.native_libs_info != null)
		{
			for (NModLibInfo lib_item:mInfo.native_libs_info)
			{
				NModLibInfo newInfo = new NModLibInfo();
				newInfo.name = getNativeLibsPath() + File.separator + lib_item.name;
				newInfo.use_api = lib_item.use_api;
				nativeLibs.add(newInfo);
			}
		}

		ret.native_libs = nativeLibs.toArray(new NModLibInfo[0]);
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

	private String getNativeLibsPath()
	{
		return new NModFilePathManager(mContext).getNModLibsDir() + File.separator + getPackageName();
	}

	@Override
	public Bitmap createIcon()
	{
		InputStream imageStream = null;
		try
		{
			ZipEntry iconEntry = mZipFile.getEntry("icon.png");
			if (iconEntry == null)
				return null;
			imageStream = mZipFile.getInputStream(iconEntry);
			return BitmapFactory.decodeStream(imageStream);
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
			ZipEntry entry = mZipFile.getEntry(MANIFEST_NAME);
			if (entry == null)
				return null;
			return mZipFile.getInputStream(entry);
		}
		catch (IOException e)
		{
			return null;
		}
	}

	ZippedNMod(String packageName, Context thisContext, File file) throws IOException
	{
		super(packageName, thisContext);
		this.mZipFile = new ZipFile(file);
		this.mFilePath = file;

		if (mZipFile.getEntry(MANIFEST_NAME) == null)
			throw new FileNotFoundException(MANIFEST_NAME);

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
