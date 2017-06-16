package com.mcal.pesdk.nativeapi;
import android.content.*;
import java.io.*;
import java.util.zip.*;
import com.mcal.pesdk.*;

public class LibraryLoader
{
	private static final String FMOD_LIB_NAME = "libfmod.so";
	private static final String MINECRAFTPE_LIB_NAME = "libminecraftpe.so";

	private static final String API_NAME = "nmodapi";
	private static final String SUBSTRATE_NAME = "substrate";
	private static final String LAUNCHER_NAME = "pesdk-game-launcher";

	static public void loadSubstrate()
	{
		System.loadLibrary(SUBSTRATE_NAME);
	}

	static public void loadLauncher()
	{
		System.loadLibrary(LAUNCHER_NAME);
	}

	static public void loadFMod(Context mcContext)
	{
		File filesDir = mcContext.getFilesDir();
		File parent = filesDir.getParentFile();
		File libsDir = new File(parent, "lib");
		File libFile = new File(libsDir, FMOD_LIB_NAME);
		System.load(libFile.getAbsolutePath());
	}

	static public void loadMinecraftPE(Context mcContext)
	{
		File filesDir = mcContext.getFilesDir();
		File parent = filesDir.getParentFile();
		File libsDir = new File(parent, "lib");
		File libFile = new File(libsDir, MINECRAFTPE_LIB_NAME);
		System.load(libFile.getAbsolutePath());
	}

	static public void loadNModAPI()
	{
		System.loadLibrary(API_NAME);
	}
}
