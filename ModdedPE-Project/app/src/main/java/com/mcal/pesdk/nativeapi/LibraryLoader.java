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
		System.load(new File(mcContext.getApplicationInfo().nativeLibraryDir,FMOD_LIB_NAME).getAbsolutePath());
	}

	static public void loadMinecraftPE(Context mcContext)
	{
		System.load(new File(mcContext.getApplicationInfo().nativeLibraryDir,MINECRAFTPE_LIB_NAME).getAbsolutePath());
	}

	static public void loadNModAPI()
	{
		System.loadLibrary(API_NAME);
	}
}
