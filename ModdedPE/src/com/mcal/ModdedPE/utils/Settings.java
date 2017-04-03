package com.mcal.ModdedPE.utils;
import android.content.*;

public class Settings
{
	private Context context;
	private final static String TAG_SETTINGS="moddedpe_settings";
	public Settings(Context context)
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

	public boolean getFirstLoaded()
	{
		return context.getSharedPreferences(TAG_SETTINGS,Context.MODE_MULTI_PROCESS).getBoolean("firstLoaded",false);
	}
	
	public boolean getRedstoneDot()
	{
		return context.getSharedPreferences(TAG_SETTINGS,Context.MODE_MULTI_PROCESS).getBoolean("redstoneDot",false);
	}
	
	public void setRedstoneDot(boolean z)
	{
		SharedPreferences.Editor editor=context.getSharedPreferences(TAG_SETTINGS,Context.MODE_MULTI_PROCESS).edit();
		editor.putBoolean("redstoneDot",z);
		editor.commit();
	}
	
	public boolean getToggleDebugText()
	{
		return context.getSharedPreferences(TAG_SETTINGS,Context.MODE_MULTI_PROCESS).getBoolean("toggle_debug_text",false);
	}

	public void setToggleDebugText(boolean z)
	{
		SharedPreferences.Editor editor=context.getSharedPreferences(TAG_SETTINGS,Context.MODE_MULTI_PROCESS).edit();
		editor.putBoolean("toggle_debug_text",z);
		editor.commit();
	}
}
