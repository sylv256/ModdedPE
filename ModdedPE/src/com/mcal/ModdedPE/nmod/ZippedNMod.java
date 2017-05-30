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
	private ZipFile zipFile = null;
	private File filePath = null;
	private AssetManager assets = null;
	private String package_name = null;

	@Override
	public NModPerloadBean copyNModFiles()
	{
		NModPerloadBean ret = new NModPerloadBean();
		Enumeration<ZipEntry> zipfile_ents = (Enumeration<ZipEntry>) zipFile.entries();

		ArrayList<String> nativeLibs = new ArrayList<String>();
		while (zipfile_ents.hasMoreElements())
		{
			ZipEntry entry=zipfile_ents.nextElement();

			if (!entry.isDirectory() && entry.getName().startsWith("lib" + File.separator + Build.CPU_ABI + File.separator))
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
	public String getPackageName()
	{
		if (package_name != null)
			return package_name;
		if (dataBean != null && dataBean.package_name != null)
			return dataBean.package_name;
		String autoPkgName = filePath.toString().replaceAll("/", ".");
		if (autoPkgName.startsWith("."))
		{
			autoPkgName = autoPkgName.replaceFirst(".", "");
		}
		if (autoPkgName.endsWith("."))
		{
			autoPkgName = autoPkgName + "nmod";
		}
		if (autoPkgName.indexOf(".") == -1)
		{
			autoPkgName = autoPkgName + ".nmod";
		}
		return autoPkgName;
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
		return new FilePathManager(thisContext).getNModLibsDir() + File.separator + getPackageName();
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

	@Override
	protected InputStream createDataBeanInputStream()
	{
		try
		{
			return zipFile.getInputStream(zipFile.getEntry(MANIFEST_NAME));
		}
		catch (IOException e)
		{
			return null;
		}
	}

	void setPackageName(String pkgName)
	{
		package_name = pkgName;
	}

	public ZippedNMod(Context thisContext, File file) throws IOException
	{
		super(thisContext);
		this.zipFile = new ZipFile(file);
		this.filePath = file;

		zipFile.getInputStream(zipFile.getEntry(MANIFEST_NAME)).close();

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
			method.invoke(assets, file.getPath());
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

		if (dataBean != null && dataBean.package_name == null)
		{
			setBugPack(new NModLoadException("Unknown package name!Please define a \"package_name\" element in " + NMod.MANIFEST_NAME + "."));
		}
		else if (dataBean != null && !NModUtils.isValidPackageName(dataBean.package_name))
		{
			setBugPack(new NModLoadException("Invalid package name!Package name should be a java-style package name."));
		}
	}
}
