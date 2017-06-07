package com.mcal.pesdk.nmod;
import android.os.*;
import dalvik.system.*;
import java.io.*;
import java.util.*;

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

	public void callOnLoad(String mcver, String moddedpever)
	{
		nativeCallOnLoad(mName, mcver, moddedpever);
	}

	private static native void nativeCallOnDexLoaded(String name, DexClassLoader classLoader);
	private static native void nativeAppendTranslation(String name, String translation);
	private static native void nativeCallOnActivityFinish(String name, com.mojang.minecraftpe.MainActivity mainActivity);
	private static native void nativeCallOnLoad(String name, String mcver, String moddedpever);
	private static native void nativeCallOnActivityCreate(String mame, com.mojang.minecraftpe.MainActivity mainActivity, Bundle savedInstanceState);
}
