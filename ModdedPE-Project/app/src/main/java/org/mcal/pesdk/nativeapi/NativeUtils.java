package org.mcal.pesdk.nativeapi;
import android.content.*;
import java.io.*;
import android.os.*;

public class NativeUtils
{
	public static native boolean nativeIsGameStarted();
	public static native void nativeSetDataDirectory(String directory);
	public static native String nativeDemangle(String symbol_name);
	public static native void nativeRegisterNatives(Class cls);
	
	public static void setValues(Context context)
	{
		NativeUtils.nativeSetDataDirectory(context.getFilesDir().getAbsolutePath() + File.separator);
	}
	
	static
	{
		nativeRegisterNatives(NativeUtils.class);
	}
}
