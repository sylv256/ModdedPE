package org.mcal.pesdk.nativeapi;

import android.content.Context;

import java.io.File;

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

	static public void loadLauncher(String mcLibsPath)
	{
		System.loadLibrary(LAUNCHER_NAME);
		nativeOnLauncherLoaded(mcLibsPath + File.separator + MINECRAFTPE_LIB_NAME);
	}

	static public void loadFMod(String mcLibsPath)
	{
		System.load(new File(mcLibsPath,FMOD_LIB_NAME).getAbsolutePath());
	}

	static public void loadMinecraftPE(String mcLibsPath)
	{
		System.load(new File(mcLibsPath,MINECRAFTPE_LIB_NAME).getAbsolutePath());
	}

	static public void loadNModAPI(String mcLibsPath)
	{
		System.loadLibrary(API_NAME);
		nativeOnNModAPILoaded(mcLibsPath + File.separator + MINECRAFTPE_LIB_NAME);
	}
	
	private static native void nativeOnLauncherLoaded(String libPath);
	private static native void nativeOnNModAPILoaded(String libPath);
}
