package org.mcal.moddedpe.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.mcal.pesdk.utils.LauncherOptions;

public class UtilsSettings implements LauncherOptions
{
	private Context mContext;
	private final static String TAG_SETTINGS = "moddedpe_settings";
	private final static String TAG_SAFE_MODE = "safe_mode";
	private final static String TAG_FIRST_LOADED = "first_loaded";
	private final static String TAG_DATA_SAVED_PATH = "data_saved_path";
	private final static String TAG_PKG_NAME = "pkg_name";
	private final static String TAG_LANGUAGE = "language_type";
	private final static String TAG_OPEN_GAME_FAILED = "open_game_failed_msg";

	public UtilsSettings(Context context)
	{
		this.mContext = context;
	}

	public void setSafeMode(boolean z)
	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_PRIVATE).edit();
		editor.putBoolean(TAG_SAFE_MODE, z);
		editor.apply();
	}

	public boolean isSafeMode()
	{
		return mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_PRIVATE).getBoolean(TAG_SAFE_MODE, false);
	}

	public void setFirstLoaded(boolean z)
	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_PRIVATE).edit();
		editor.putBoolean(TAG_FIRST_LOADED, z);
		editor.apply();
	}

	public boolean isFirstLoaded()
	{
		return mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_PRIVATE).getBoolean(TAG_FIRST_LOADED, false);
	}
	
	public void setLanguageType(int z)
	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_PRIVATE).edit();
		editor.putInt(TAG_LANGUAGE, z);
		editor.apply();
	}

	public int getLanguageType()
	{
		return mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_PRIVATE).getInt(TAG_LANGUAGE, 0);
	}
	
	@Override
	public String getDataSavedPath()
	{
		return mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_PRIVATE).getString(TAG_DATA_SAVED_PATH, STRING_VALUE_DEFAULT);
	}

	@Override
	public String getMinecraftPEPackageName()
	{
		return mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_PRIVATE).getString(TAG_PKG_NAME, STRING_VALUE_DEFAULT);
	}
	
	public void setDataSavedPath(String z)
	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_PRIVATE).edit();
		editor.putString(TAG_DATA_SAVED_PATH, z);
		editor.apply();
	}
	
	public void setMinecraftPackageName(String z)
	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_PRIVATE).edit();
		editor.putString(TAG_PKG_NAME, z);
		editor.apply();
	}

	public void setOpenGameFailed(String z)
	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_PRIVATE).edit();
		editor.putString(TAG_OPEN_GAME_FAILED, z);
		editor.apply();
	}

	public String getOpenGameFailed()
	{
		return mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_PRIVATE).getString(TAG_OPEN_GAME_FAILED, null);
	}
}
