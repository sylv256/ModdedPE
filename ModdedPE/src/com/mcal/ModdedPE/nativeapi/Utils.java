package com.mcal.ModdedPE.nativeapi;

public class Utils
{
	public static native boolean nativeIsGameStarted();
	
	public static native void nativeSetDataDirectory(String directory);
	
	public static native void nativeSetRedstoneDot(boolean z);
	public static native void nativeSetToggleDebugText(boolean z);
	public static native void nativeSetFlatWorldLayers(boolean z);
}
