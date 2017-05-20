package com.mcal.ModdedPE.nativeapi;
import com.mcal.ModdedPE.utils.*;
import android.content.*;
import java.io.*;

public class NativeUtils
{
	public static native boolean nativeIsGameStarted();
	
	public static native void nativeSetDataDirectory(String directory);
	
	public static native void nativeSetAutoSaveLevel(boolean z);
	public static native void nativeSetSelectAllInLeft(boolean z);
	public static native void nativeSetRedstoneDot(boolean z);
	public static native void nativeSetHideDebugText(boolean z);
	public static native void nativeSetFlatWorldLayers(boolean z);
	public static native void nativeSetDisableTextureIsotropic(boolean z);
	
	public static native String nativeDemangle(String symbol_name);
	
	public static void setValues(Context context)
	{
		UtilsSettings settings=new UtilsSettings(context);
		
		NativeUtils.nativeSetDataDirectory("/data/data/" + context.getPackageName() + File.separator);
		NativeUtils.nativeSetRedstoneDot(settings.getRedstoneDot());
		NativeUtils.nativeSetHideDebugText(settings.getHideDebugText());
		NativeUtils.nativeSetAutoSaveLevel(settings.getAutoSaveLevel());
		NativeUtils.nativeSetSelectAllInLeft(settings.getSelectAllInLeft());
		NativeUtils.nativeSetDisableTextureIsotropic(settings.getDisableTextureIsotropic());
	}
}
