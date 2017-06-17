package org.mcal.moddedpe.utils;
import android.content.*;
import org.mcal.pesdk.utils.*;

public class UtilsSettings implements LauncherOptions
{
	private Context mContext;
	private final static String TAG_SETTINGS = "moddedpe_settings";
	private final static String TAG_SAFE_MODE = "safe_mode";
	private final static String TAG_FIRST_LOADED = "first_loaded";

	public UtilsSettings(Context context)
	{
		this.mContext = context;
	}

	public void setSafeMode(boolean z)
	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_MULTI_PROCESS).edit();
		editor.putBoolean(TAG_SAFE_MODE, z);
		editor.commit();
	}

	public boolean isSafeMode()
	{
		return mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_MULTI_PROCESS).getBoolean(TAG_SAFE_MODE, false);
	}

	public void setFirstLoaded(boolean z)
	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_MULTI_PROCESS).edit();
		editor.putBoolean(TAG_FIRST_LOADED, z);
		editor.commit();
	}

	public boolean isFirstLoaded()
	{
		return mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_MULTI_PROCESS).getBoolean(TAG_FIRST_LOADED, false);
	}
}
