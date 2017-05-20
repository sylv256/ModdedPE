package com.mcal.ModdedPE.nativeapi;
import com.mcal.ModdedPE.*;
import android.content.*;
import com.mcal.ModdedPE.utils.*;
import java.io.*;

public class LibraryLoader
{
	public static final String FMOD_LIB_NAME = "libfmod.so";
	public static final String MINECRAFTPE_LIB_NAME = "libfmod.so";

	static public void loadGameLibs(Context context, String mcLibDir, boolean isSafeMode)
	{
		try
		{
			System.load(mcLibDir + File.separator + FMOD_LIB_NAME);
			System.load(mcLibDir + File.separator + MINECRAFTPE_LIB_NAME);
		}
		catch (Throwable t)
		{
			try
			{
				System.load(MinecraftInfo.getInstance(context).getMinecraftPackageNativeLibraryDir() + File.separator + FMOD_LIB_NAME);
				System.load(MinecraftInfo.getInstance(context).getMinecraftPackageNativeLibraryDir() + File.separator + MINECRAFTPE_LIB_NAME);
			}
			catch (Throwable t2)
			{
				ModdedPEApplication.instance.restartAppAndReport(t);
				return;
			}
		}
		if (!isSafeMode)
			System.loadLibrary("moddedpe_utils");
	}

	static public void loadLocalLibs()
	{
		System.loadLibrary("substrate");
		System.loadLibrary("moddedpe_launcher");
	}
}
