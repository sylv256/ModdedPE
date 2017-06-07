package com.mcal.pesdk.nativeapi;
import android.content.*;
import java.io.*;

public class LibraryLoader
{
	public static final String FMOD_LIB_NAME = "libfmod.so";
	public static final String MINECRAFTPE_LIB_NAME = "libminecraftpe.so";

	public static final String UTILS_NAME = "nmodapi-utils";
	public static final String SUBSTRATE_NAME = "substrate";
	public static final String LAUNCHER_NAME = "nmodapi-launcher";
	
	static public void loadGameLibs(Context context, String mcLibDir, boolean isSafeMode)
	{
		System.loadLibrary(SUBSTRATE_NAME);
		System.loadLibrary(LAUNCHER_NAME);
		System.load(mcLibDir + File.separator + FMOD_LIB_NAME);
		System.load(mcLibDir + File.separator + MINECRAFTPE_LIB_NAME);
		if (!isSafeMode)
			System.loadLibrary(UTILS_NAME);
	}
}
