package com.mcal.ModdedPE.nativeapi;

public class Utils
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
}
