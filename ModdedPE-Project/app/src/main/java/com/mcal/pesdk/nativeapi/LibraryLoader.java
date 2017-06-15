package com.mcal.pesdk.nativeapi;
import android.content.*;
import java.io.*;

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
	
	static public void loadFMod(String mcLibDir)
	{
		System.load(mcLibDir + File.separator + FMOD_LIB_NAME);
	}
	
	static public void loadMinecraftPE(String mcLibDir)
	{
		System.load(mcLibDir + File.separator + MINECRAFTPE_LIB_NAME);
	}
	
	static public void loadNModAPI()
	{
		System.loadLibrary(API_NAME);
	}
}