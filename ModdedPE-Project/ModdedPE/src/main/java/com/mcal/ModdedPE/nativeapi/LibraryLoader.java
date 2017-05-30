package com.mcal.ModdedPE.nativeapi;
import com.mcal.ModdedPE.*;
import android.content.*;
import com.mcal.ModdedPE.utils.*;
import java.io.*;

public class LibraryLoader
{
	public static final String FMOD_LIB_NAME = "libfmod.so";
	public static final String MINECRAFTPE_LIB_NAME = "libminecraftpe.so";

	public static final String UTILS_NAME = "moddedpe_utils";
	public static final String SUBSTRATE_NAME = "substrate";
	public static final String LAUNCHER_NAME = "moddedpe_launcher";
	
	static public void loadGameLibs(Context context, String mcLibDir, boolean isSafeMode)
	{
		System.load(mcLibDir + File.separator + FMOD_LIB_NAME);
		System.load(mcLibDir + File.separator + MINECRAFTPE_LIB_NAME);
		if (!isSafeMode)
			System.loadLibrary(UTILS_NAME);
	}

	static public void loadLocalLibs()
	{
		System.loadLibrary(SUBSTRATE_NAME);
		System.loadLibrary(LAUNCHER_NAME);
	}
}
