package org.mcal.pesdk.nmod;
import android.os.*;
import dalvik.system.*;
import java.io.*;
import java.util.*;
import com.mojang.minecraftpe.*;

public class NModLib
{
	private String mName;
	public NModLib(String name)
	{
		mName = name;
	}

	public boolean callOnActivityCreate(com.mojang.minecraftpe.MainActivity mainActivity, Bundle bundle)
	{
		return nativeCallOnActivityCreate(mName, mainActivity, bundle);
	}

	public boolean callOnActivityFinish(com.mojang.minecraftpe.MainActivity mainActivity)
	{
		return nativeCallOnActivityFinish(mName, mainActivity);
	}

	public boolean callOnLoad(String mcver, String apiVer)
	{
		return nativeCallOnLoad(mName, mcver, apiVer);
	}

	public boolean callOnDexLoaded(DexClassLoader dexClassLoader)
	{
		return nativeCallOnDexLoaded(mName, dexClassLoader);
	}
	
	static
	{
		nativeRegisterNatives(NModLib.class);
	}
	
	private static native boolean nativeRegisterNatives(Class cls);
	private static native boolean nativeCallOnDexLoaded(String name, DexClassLoader classLoader);
	private static native boolean nativeCallOnActivityFinish(String name, MainActivity mainActivity);
	private static native boolean nativeCallOnLoad(String name, String mcVersion, String apiVersion);
	private static native boolean nativeCallOnActivityCreate(String mame, MainActivity mainActivity, Bundle savedInstanceState);
}
