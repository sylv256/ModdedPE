package org.mcal.pesdk.nativeapi;
import android.content.*;
import java.io.*;
import org.mcal.pesdk.utils.*;

public class NativeUtils
{
	public static native void nativeSetDataDirectory(String directory);

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
}
