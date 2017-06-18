package org.mcal.pesdk.utils;

public interface LauncherOptions
{
	public boolean isSafeMode();
	public String getDataSavedPath();
	public String getMinecraftPEPackageName();
	
	public static final String STRING_VALUE_DEFAULT = "default";
}
