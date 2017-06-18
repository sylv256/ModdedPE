package org.mcal.pesdk.nativeapi;
import android.content.*;
import java.io.*;
import android.os.*;
import org.mcal.pesdk.utils.*;

public class NativeUtils
{
	public static native boolean nativeIsGameStarted();
	public static native void nativeSetDataDirectory(String directory);
	public static native String nativeDemangle(String symbol_name);
	public static native void nativeRegisterNatives(Class cls);

	public static void setValues(Context context, LauncherOptions options)
	{
		if (options.getDataSavedPath().equals(LauncherOptions.STRING_VALUE_DEFAULT))
		{
			NativeUtils.nativeSetDataDirectory(context.getFilesDir().getAbsolutePath() + File.separator);
		}
		else
		{
			String pathStr = options.getDataSavedPath();
			if (!pathStr.endsWith(File.separator))
				pathStr += File.separator;
			NativeUtils.nativeSetDataDirectory(pathStr);
		}
	}

	static
	{
		nativeRegisterNatives(NativeUtils.class);
	}
}
