package com.mcal.ModdedPE.nmod;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import com.google.gson.*;
import com.mcal.ModdedPE.utils.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

public class NModUtils
{
	static private boolean isValidJavaIdentifier(String className)
	{
		if (className.length() == 0 || !Character.isJavaIdentifierStart(className.charAt(0)))
			return false;
		String name = className.substring(1);
		for (int i = 0; i < name.length(); i++)
			if (!Character.isJavaIdentifierPart(name.charAt(i)))
				return false;
		return true;
	} 

	static public boolean isValidPackageName(String fullName)
	{
		if (fullName == null)
			return false;

		if (fullName.indexOf(".") == -1)
			return false;

		boolean flag = true;
		try
		{
			if (!fullName.endsWith("."))
			{
				int index = fullName.indexOf("."); 
				if (index != -1)
				{
					String[] str = fullName.split("\\.");
					for (String name : str)
					{
						if (name.equals(""))
						{
							flag = false;
							break;
						}
						else if (!isValidJavaIdentifier(name))
						{
							flag = false;
							break;
						}
					}
				}
				else if (!isValidJavaIdentifier(fullName))
				{
					flag = false;
				}
			}
			else
			{
				flag = false;
			}
		}
		catch (Exception ex)
		{
			flag = false;
		}
		return flag;
	}
	
	public static PackagedNMod archivePackagedNMod(Context contextThis,String packageName)
	{
		try
		{
			Context contextPackage = contextThis.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
			contextPackage.getAssets().open(NMod.MANIFEST_NAME).close();
			return new PackagedNMod(contextThis,contextPackage);
		}
		catch (IOException e)
		{}
		catch(PackageManager.NameNotFoundException notFoundE)
		{}
		return null;
	}
	
	public static ZippedNMod apkToNModPack(Context context,File apkFile,File toFile)throws IOException,NModLoadException
	{
		PackageManager mgr = context.getPackageManager();
		try
		{
			toFile.createNewFile();
			ZipFile zipFile = new ZipFile(apkFile);
			PackageInfo info = mgr.getPackageArchiveInfo(apkFile.getPath(),PackageManager.GET_CONFIGURATIONS);
			if(info == null)
				return null;
			String packageName = info.packageName;
			String versionName = info.versionName;
			int versionCode = info.versionCode;
			info.applicationInfo.sourceDir = apkFile.getPath();    
			info.applicationInfo.publicSourceDir = apkFile.getPath(); 
			Drawable icon = mgr.getApplicationIcon(info.applicationInfo);
			ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(toFile));
			Enumeration<ZipEntry> zipfile_ents = (Enumeration<ZipEntry>) zipFile.entries();

			while (zipfile_ents.hasMoreElements())
			{
				ZipEntry entry=zipfile_ents.nextElement();
				
				if(!entry.isDirectory() && entry.getName() != ("assets" + File.separator + NMod.MANIFEST_NAME))
				{
					zipOutputStream.putNextEntry(entry);
					InputStream from = zipFile.getInputStream(entry);
					int byteReaded = -1;
					byte[] buffer = new byte[1024];
					while ((byteReaded = from.read(buffer)) != -1)
					{
						zipOutputStream.write(buffer, 0, byteReaded);
					}
					from.close();
					zipOutputStream.closeEntry();
				}
			}
			//Manifest
			ZipEntry entry=new ZipEntry("assets" + File.separator + NMod.MANIFEST_NAME);
			{
				InputStream from = zipFile.getInputStream(entry);
				byte[] buffer = new byte[from.available()];
				from.read(buffer);
				from.close();
				String jsonTmp = new String(buffer);
				zipOutputStream.closeEntry();
				try
				{
					NMod.NModDataBean dataBean = new Gson().fromJson(jsonTmp,NMod.NModDataBean.class);
					if(dataBean.package_name != null)
						if(!dataBean.package_name.equals(packageName))
							throw new NModLoadException("Error checking package name: packageName in AndroidManifest.xml didn't equal package_name in " + NMod.MANIFEST_NAME +"!");
					dataBean.package_name = packageName;
					dataBean.version_code = versionCode;
					dataBean.version_name = versionName;
					
					zipOutputStream.putNextEntry(new ZipEntry(NMod.MANIFEST_NAME));
					zipOutputStream.write(new Gson().toJson(dataBean).getBytes());
					zipOutputStream.closeEntry();
				}
				catch(JsonSyntaxException syntaxE)
				{
					
				}
			}
			//Icon
			Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), icon.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
			icon.draw(canvas);
			zipOutputStream.putNextEntry(new ZipEntry("icon.png"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
			zipOutputStream.write(baos.toByteArray());
			zipOutputStream.closeEntry();
			
			zipOutputStream.close();
			
			return new ZippedNMod(context,toFile);
		}
		catch(IOException ioe)
		{
			throw ioe;
		}
	}
	
	public static ZippedNMod archiveZippedNMod(Context context,String filePath) throws NModLoadException
	{
		try
		{
			ZippedNMod nmod = createNMod_zipped_apkMode(context,filePath);
			if(nmod == null)
			{
				ZippedNMod nmod2 = createNMod_zipped_zipMode(context,filePath);
				return nmod2;
			}
			return nmod;
		}
		catch (IOException e)
		{}
		return null;
	}

	private static ZippedNMod createNMod_zipped_apkMode(Context context,String filePath) throws IOException,NModLoadException
	{
		File dir = new File(new FilePathManager(context).getNModsDir());
		dir.mkdirs();
		File cacheFile = new File(new FilePathManager(context).getNModCachePath());
		ZippedNMod zippedNMod_cached = NModUtils.apkToNModPack(context, new File(filePath), cacheFile);
		if(zippedNMod_cached == null)
		{
			return null;
		}
		if(zippedNMod_cached.isBugPack())
			throw zippedNMod_cached.getLoadException();
		String pkgName = zippedNMod_cached.getPackageName();
		File finalFileDir = new File(new FilePathManager(context).getNModsDir());
		finalFileDir.mkdirs();
		File finalFile = new File(new FilePathManager(context).getNModsDir() + File.separator + pkgName);
		finalFile.createNewFile();
		FileOutputStream finalFileOutput = new FileOutputStream(finalFile);
		FileInputStream fileInput = new FileInputStream(cacheFile);
		int byteReaded = -1;
		byte[] buffer = new byte[1024];
		while ((byteReaded = fileInput.read(buffer)) != -1)
		{
			finalFileOutput.write(buffer, 0, byteReaded);
		}
		finalFileOutput.close();
		fileInput.close();
		return new ZippedNMod(context, finalFile);
	}

	private static ZippedNMod createNMod_zipped_zipMode(Context context,String filePath) throws IOException,NModLoadException
	{
		ZipFile zipFile = new ZipFile(filePath);
		File dir = new File(new FilePathManager(context).getNModsDir());
		dir.mkdirs();
		File cacheFile = new File(new FilePathManager(context).getNModCachePath());
		cacheFile.createNewFile();
		ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(cacheFile));
		Enumeration<ZipEntry> zipfile_ents = (Enumeration<ZipEntry>) zipFile.entries();

		while (zipfile_ents.hasMoreElements())
		{
			ZipEntry entry=zipfile_ents.nextElement();
			if (!entry.isDirectory())
			{
				zipOutputStream.putNextEntry(entry);
				InputStream from = zipFile.getInputStream(entry);
				int byteReaded = -1;
				byte[] buffer = new byte[1024];
				while ((byteReaded = from.read(buffer)) != -1)
				{
					zipOutputStream.write(buffer, 0, byteReaded);
				}
				from.close();
				zipOutputStream.closeEntry();
			}
		}
		ZipEntry entry=new ZipEntry("AndroidManifest.xml");
		zipOutputStream.putNextEntry(entry);
		zipOutputStream.closeEntry();
		zipOutputStream.close();
		ZippedNMod zippedNMod_cached = new ZippedNMod(context, cacheFile);
		if (zippedNMod_cached.isBugPack())
			throw zippedNMod_cached.getLoadException();
		String pkgName = zippedNMod_cached.getPackageName();
		File finalFileDir = new File(new FilePathManager(context).getNModsDir());
		finalFileDir.mkdirs();
		File finalFile = new File(new FilePathManager(context).getNModsDir() + File.separator + pkgName);
		finalFile.createNewFile();
		FileOutputStream finalFileOutput = new FileOutputStream(finalFile);
		FileInputStream fileInput = new FileInputStream(cacheFile);
		int byteReaded = -1;
		byte[] buffer = new byte[1024];
		while ((byteReaded = fileInput.read(buffer)) != -1)
		{
			finalFileOutput.write(buffer, 0, byteReaded);
		}
		finalFileOutput.close();
		fileInput.close();
		return new ZippedNMod(context, finalFile);
	}
}
