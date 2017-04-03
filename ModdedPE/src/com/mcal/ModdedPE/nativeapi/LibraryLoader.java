package com.mcal.ModdedPE.nativeapi;
import com.mcal.ModdedPE.*;

public class LibraryLoader
{
	static public void loadGameLibs(String mcLibDir,boolean isSafeMode)
	{
		try
		{
			System.load(mcLibDir + "/libfmod.so");
			System.load(mcLibDir + "/libminecraftpe.so");
		}
		catch(Throwable t)
		{
			try
			{
				System.load(ModdedPEApplication.MC_NATIVE_DIR + "/libfmod.so");
				System.load(ModdedPEApplication.MC_NATIVE_DIR + "/libminecraftpe.so");
			}
			catch(Throwable t2)
			{
				ModdedPEApplication.instance.restartAppAndReport(t);
				return;
			}
		}
		if(!isSafeMode)
			System.loadLibrary("moddedpe_utils");
	}
	
	static public void loadLocalLibs()
	{
		System.loadLibrary("substrate");
		System.loadLibrary("moddedpe_launcher");
	}
}
