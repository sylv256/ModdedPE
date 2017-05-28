package com.mcal.ModdedPE.utils;
import android.content.*;

public class UtilsSettings
{
	private Context context;
	private final static String TAG_SETTINGS="moddedpe_settings";
	public UtilsSettings(Context context)
	{
		this.context=context;
	}
	
	public void setSafeMode(boolean z)
	{
		SharedPreferences.Editor editor=context.getSharedPreferences(TAG_SETTINGS,Context.MODE_MULTI_PROCESS).edit();
		editor.putBoolean("safeMode",z);
		editor.commit();
	}
	
	public boolean getSafeMode()
	{
		return context.getSharedPreferences(TAG_SETTINGS,Context.MODE_MULTI_PROCESS).getBoolean("safeMode",false);
	}
	
	public void setFirstLoaded(boolean z)
	{
		SharedPreferences.Editor editor=context.getSharedPreferences(TAG_SETTINGS,Context.MODE_MULTI_PROCESS).edit();
		editor.putBoolean("firstLoaded",z);
		editor.commit();
	}

	public boolean getAutoSaveLevel()
	{
		return context.getSharedPreferences(TAG_SETTINGS,Context.MODE_MULTI_PROCESS).getBoolean("autoSaveLevel",false);
	}
}
