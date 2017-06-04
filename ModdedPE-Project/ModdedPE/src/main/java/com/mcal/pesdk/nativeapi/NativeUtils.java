package com.mcal.pesdk.nativeapi;
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
