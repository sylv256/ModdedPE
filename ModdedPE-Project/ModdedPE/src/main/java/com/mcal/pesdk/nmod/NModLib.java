package com.mcal.pesdk.nmod;
import android.os.*;
import dalvik.system.*;
import java.io.*;
import java.util.*;
import com.mojang.minecraftpe.*;

class NModLib
{
	private String mName;
	public NModLib(String name)
	{
		mName = name;
	}

	public void callOnActivityCreate(com.mojang.minecraftpe.MainActivity mainActivity, Bundle bundle)
	{
		nativeCallOnActivityCreate(mName, mainActivity, bundle);
	}

	public void callOnActivityFinish(com.mojang.minecraftpe.MainActivity mainActivity)
	{
		nativeCallOnActivityFinish(mName, mainActivity);
	}

	public void callOnLoad(String mcver, String apiVer)
	{
		nativeCallOnLoad(mName, mcver, apiVer);
	}

	public void callOnDexLoaded(DexClassLoader dexClassLoader)
	{
		nativeCallOnDexLoaded(mName, dexClassLoader);
	}

	private static native void nativeCallOnDexLoaded(String name, DexClassLoader classLoader);
	private static native void nativeCallOnActivityFinish(String name, MainActivity mainActivity);
	private static native void nativeCallOnLoad(String name, String mcVersion, String apiVersion);
	private static native void nativeCallOnActivityCreate(String mame, MainActivity mainActivity, Bundle savedInstanceState);
}
