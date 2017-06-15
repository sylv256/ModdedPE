package com.mcal.pesdk.nmod;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.zip.*;
import com.mcal.pesdk.*;

public class ZippedNMod extends NMod
{
	private ZipFile mZipFile = null;
	private File mFilePath = null;
	private AssetManager mAssets = null;

	@Override
	public NModPreloadBean copyNModFiles()
	{
		NModPreloadBean ret = new NModPreloadBean();
		Enumeration<ZipEntry> zipfile_ents = (Enumeration<ZipEntry>) mZipFile.entries();

		new File(getNativeLibsPath()).mkdirs();
		while (zipfile_ents.hasMoreElements())
		{
			ZipEntry entry=zipfile_ents.nextElement();

			if (entry == null)
				continue;

			if (!entry.isDirectory() && entry.getName().startsWith("lib" + File.separator + ABIInfo.getTargetABIType() + File.separator))
			{
				try
				{
					InputStream libInputStream = mZipFile.getInputStream(entry);
					int byteReaded = -1;
					byte[] buffer = new byte[1024];
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

		ArrayList<String> nativeLibs = new ArrayList<String>();
		ArrayList<String> nativeLibsNeeded = new ArrayList<String>();
		if (mInfo != null && mInfo.native_libs_info != null)
		{
			for (NModLibInfo lib_item:mInfo.native_libs_info)
			{
				if (lib_item.mode.equals(NModLibInfo.MODE_ALWAYS))
				{
					nativeLibs.add(getNativeLibsPath() + File.separator + lib_item.name);
				}
				else if (lib_item.mode.equals(NModLibInfo.MODE_IF_NEEDED))
				{
					nativeLibsNeeded.add(getNativeLibsPath() + File.separator + lib_item.name);
				}
			}
		}

		ret.native_libs = nativeLibs.toArray(new String[0]);
		ret.needed_libs = nativeLibsNeeded.toArray(new String[0]);
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
