package com.mcal.pesdk.nativeapi;
import android.content.*;
import java.io.*;
import java.util.zip.*;
import com.mcal.pesdk.*;

public class LibraryLoader
{
	private static final String FMOD_LIB_NAME = "libfmod.so";
	private static final String MINECRAFTPE_LIB_NAME = "libminecraftpe.so";

<<<<<<< HEAD
	private static final String API_NAME = "nmodapi";
	private static final String SUBSTRATE_NAME = "substrate";
	private static final String LAUNCHER_NAME = "pesdk-game-launcher";
	
=======
	public static final String API_NAME = "nmodapi";
	public static final String SUBSTRATE_NAME = "substrate";
	public static final String LAUNCHER_NAME = "pesdk-game-launcher";

	private static final String DIR_LIBS = "mcpe_native_libs";

>>>>>>> 817fd62286ab6f633b7ce6ecef185569b0251d2f
	static public void loadSubstrate()
	{
		System.loadLibrary(SUBSTRATE_NAME);
	}

	static public void loadLauncher()
	{
		System.loadLibrary(LAUNCHER_NAME);
	}

	static public void loadFMod(Context context, Context mcContext) throws IOException,RuntimeException,ZipException
	{
		ZipFile zipFile = new ZipFile(new File(mcContext.getPackageResourcePath()));
		ZipEntry entry = zipFile.getEntry("lib" + File.separator + ABIInfo.getTargetABIType() + File.separator + FMOD_LIB_NAME);
		if (entry == null)
			throw new RuntimeException("Elf file libfmod.so not found in apk.");
		File dir = new File(context.getCacheDir(), DIR_LIBS);
		dir.mkdirs();
		File elfFile = new File(dir, FMOD_LIB_NAME);
		elfFile.createNewFile();
		InputStream libInputStream = zipFile.getInputStream(entry);
		int byteReaded = -1;
		byte[] buffer = new byte[1024];
		FileOutputStream writerStream = new FileOutputStream(elfFile);
		while ((byteReaded = libInputStream.read(buffer)) != -1)
		{
			writerStream.write(buffer, 0, byteReaded);
		}
		libInputStream.close();
		writerStream.close();
		System.load(elfFile.getAbsolutePath());
	}

	static public void loadMinecraftPE(Context context, Context mcContext) throws IOException,RuntimeException,ZipException
	{
		ZipFile zipFile = new ZipFile(new File(mcContext.getPackageResourcePath()));
		ZipEntry entry = zipFile.getEntry("lib" + File.separator + ABIInfo.getTargetABIType() + File.separator + MINECRAFTPE_LIB_NAME);
		if (entry == null)
			throw new RuntimeException("Elf file libminecraftpe.so not found in apk.");
		File dir = new File(context.getCacheDir(), DIR_LIBS);
		dir.mkdirs();
		File elfFile = new File(dir, MINECRAFTPE_LIB_NAME);
		elfFile.createNewFile();
		InputStream libInputStream = zipFile.getInputStream(entry);
		int byteReaded = -1;
		byte[] buffer = new byte[1024];
		FileOutputStream writerStream = new FileOutputStream(elfFile);
		while ((byteReaded = libInputStream.read(buffer)) != -1)
		{
			writerStream.write(buffer, 0, byteReaded);
		}
		libInputStream.close();
		writerStream.close();
		System.load(elfFile.getAbsolutePath());
	}

	static public void loadNModAPI()
	{
		System.loadLibrary(API_NAME);
	}
}