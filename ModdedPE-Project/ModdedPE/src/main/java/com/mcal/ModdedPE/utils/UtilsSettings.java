package com.mcal.ModdedPE.utils;
import android.content.*;
import com.mcal.pesdk.utils.*;

public class UtilsSettings implements LauncherOptions
{
	private Context mContext;
	private final static String TAG_SETTINGS = "moddedpe_settings";
	private final static String TAG_SAFE_MODE = "safe_mode";
	private final static String TAG_ABI_TYPE = "abi_type";
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

	@Override
	public String getABIType()
	{
		String type = mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_MULTI_PROCESS).getString(TAG_ABI_TYPE, ABI_AUTO);
		if (type != ABI_X86 && type != ABI_ARMEABI_V7A && type != ABI_AUTO)
		{
			setABIType(ABI_AUTO);
			return getABIType();
		}
		return type;
	}

	public void setABIType(String abiType)
	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(TAG_SETTINGS, Context.MODE_MULTI_PROCESS).edit();
		editor.putString(TAG_ABI_TYPE, abiType);
		editor.commit();
	}
}
