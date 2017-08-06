package org.mcal.pesdk.nativeapi;

import java.io.File;
import java.io.IOException;

public class LibraryLoader
{
	private static final String FMOD_LIB_NAME = "libfmod.so";
	private static final String MINECRAFTPE_LIB_NAME = "libminecraftpe.so";

	private static final String PESDK_LIB_NAME = "pesdk";
	private static final String SUBSTRATE_NAME = "substrate";

	static public void loadSubstrate()
	{
		System.loadLibrary(SUBSTRATE_NAME);
	}

	static public void loadFMod(String mcLibsPath) throws IOException
	{
		System.load(new File(mcLibsPath,FMOD_LIB_NAME).getAbsolutePath());
	}

	static public void loadMinecraftPE(String mcLibsPath) throws IOException
	{
		System.load(new File(mcLibsPath,MINECRAFTPE_LIB_NAME).getAbsolutePath());
	}

	static public void loadPESdkLib(String libPath,boolean isSafeMode)
	{
		System.loadLibrary(PESDK_LIB_NAME);
		nativeOnPESdkLoaded(libPath + File.separator + MINECRAFTPE_LIB_NAME,isSafeMode);
	}
	
	private static native void nativeOnPESdkLoaded(String libPath,boolean isSafeMode);
}
