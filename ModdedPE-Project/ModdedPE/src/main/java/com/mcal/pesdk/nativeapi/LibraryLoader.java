package com.mcal.pesdk.nativeapi;
import android.content.*;
import java.io.*;

public class LibraryLoader
{
	public static final String FMOD_LIB_NAME = "libfmod.so";
	public static final String MINECRAFTPE_LIB_NAME = "libminecraftpe.so";

	public static final String API_NAME = "nmodapi";
	public static final String SUBSTRATE_NAME = "substrate";
	public static final String LAUNCHER_NAME = "nmodapi-game-launcher";
	
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
