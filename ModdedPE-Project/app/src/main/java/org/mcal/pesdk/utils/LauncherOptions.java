package org.mcal.pesdk.utils;

public interface LauncherOptions
{
	boolean isSafeMode();
	String getDataSavedPath();
	String getMinecraftPEPackageName();
	
	String STRING_VALUE_DEFAULT = "default";
}
