package com.mcal.ModdedPE.nativeapi;
import com.mcal.ModdedPE.utils.*;
import android.content.*;
import java.io.*;

public class NativeUtils
{
	public static native boolean nativeIsGameStarted();
	public static native void nativeSetDataDirectory(String directory);
	public static native String nativeDemangle(String symbol_name);
	
	public static void setValues(Context context)
	{
		NativeUtils.nativeSetDataDirectory("/data/data/" + context.getPackageName() + File.separator);
	}
}
