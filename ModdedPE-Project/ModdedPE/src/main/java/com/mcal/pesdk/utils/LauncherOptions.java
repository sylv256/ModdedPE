package com.mcal.pesdk.utils;

public interface LauncherOptions
{
	public static final String ABI_AUTO = "auto";
	public static final String ABI_ARMEABI_V7A = "armeabi-v7a";
	public static final String ABI_X86 = "x86";
	
	public boolean isSafeMode();
	public String getABIType();
}
