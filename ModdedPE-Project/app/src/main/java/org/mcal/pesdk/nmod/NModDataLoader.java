package org.mcal.pesdk.nmod;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

class NModDataLoader
{
	private Context mContext;
	private static final String TAG_SHARED_PREFERENCE = "nmod_data_list";
	private static final String TAG_ENABLED_LIST = "enabled_nmods_list";
	private static final String TAG_DISABLE_LIST = "disabled_nmods_list";

	NModDataLoader(Context context)
	{
		mContext = context;
	}

	ArrayList<String> getAllList()
	{
		ArrayList<String> ret = new ArrayList<>();
		ret.addAll(getDisabledList());
		ret.addAll(getEnabledList());
		return ret;
	}

	void removeByName(String name)
	{
		SharedPreferences preferences=getSharedPreferences();
		ArrayList<String> enabledList=getEnabledList();
		ArrayList<String> disableList=getDisabledList();
		enabledList.remove(name);
		disableList.remove(name);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ENABLED_LIST, fromArrayList(enabledList));
		editor.putString(TAG_DISABLE_LIST, fromArrayList(disableList));
		editor.apply();
	}

	void setIsEnabled(NMod nmod, boolean isEnabled)
	{
		if (isEnabled)
			addNewEnabled(nmod);
		else
			removeEnabled(nmod);
	}

	private SharedPreferences getSharedPreferences()
	{
		return mContext.getSharedPreferences(TAG_SHARED_PREFERENCE, Context.MODE_PRIVATE);
	}

	private void addNewEnabled(NMod nmod)
	{
		SharedPreferences preferences=getSharedPreferences();
		ArrayList<String> enabledList=getEnabledList();
		ArrayList<String> disableList=getDisabledList();
		if (enabledList.indexOf(nmod.getPackageName()) == -1)
			enabledList.add(nmod.getPackageName());
		disableList.remove(nmod.getPackageName());
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ENABLED_LIST, fromArrayList(enabledList));
		editor.putString(TAG_DISABLE_LIST, fromArrayList(disableList));
		editor.apply();
	}

	private void removeEnabled(NMod nmod)
	{
		SharedPreferences preferences=getSharedPreferences();
		ArrayList<String> enabledList=getEnabledList();
		ArrayList<String> disableList=getDisabledList();
		enabledList.remove(nmod.getPackageName());
		if (disableList.indexOf(nmod.getPackageName()) == -1)
			disableList.add(nmod.getPackageName());
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ENABLED_LIST, fromArrayList(enabledList));
		editor.putString(TAG_DISABLE_LIST, fromArrayList(disableList));
		editor.apply();
	}

	void upNMod(NMod nmod)
	{
		SharedPreferences preferences=getSharedPreferences();
		ArrayList<String> enabledList=getEnabledList();
		int index=enabledList.indexOf(nmod.getPackageName());
		if (index == -1 || index == 0)
			return;
		int indexFront=index - 1;
		String nameFront=enabledList.get(indexFront);
		if (nameFront == null || nameFront.isEmpty())
			return;
		String nameSelf=nmod.getPackageName();
		enabledList.set(indexFront, nameSelf);
		enabledList.set(index, nameFront);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ENABLED_LIST, fromArrayList(enabledList));
		editor.apply();
	}

	void downNMod(NMod nmod)
	{
		SharedPreferences preferences=getSharedPreferences();
		ArrayList<String> enabledList=getEnabledList();
		int index=enabledList.indexOf(nmod.getPackageName());
		if (index == -1 || index == (enabledList.size() - 1))
			return;
		int indexBack=index + 1;
		String nameBack=enabledList.get(indexBack);
		if (nameBack == null || nameBack.isEmpty())
			return;
		String nameSelf=nmod.getPackageName();
		enabledList.set(indexBack, nameSelf);
		enabledList.set(index, nameBack);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ENABLED_LIST, fromArrayList(enabledList));
		editor.apply();
	}

	ArrayList<String> getEnabledList()
	{
		SharedPreferences preferences=getSharedPreferences();
		return toArrayList(preferences.getString(TAG_ENABLED_LIST, ""));
	}

	ArrayList<String> getDisabledList()
	{
		SharedPreferences preferences=getSharedPreferences();
		return toArrayList(preferences.getString(TAG_DISABLE_LIST, ""));
	}
	private static ArrayList<String> toArrayList(String str)
	{
		String[] mStr = str.split("/");
		ArrayList<String> list = new ArrayList<>();
		for (String strElement:mStr)
		{
			if (strElement != null && !strElement.isEmpty())
				list.add(strElement);
		}
		return list;
	}

	private static String fromArrayList(ArrayList<String> arrayList)
	{
		String str="";
		if (arrayList != null)
		{
			for (String mStr:arrayList)
			{
				str += mStr;
				str += "/";
			}
		}
		return str;
	}
}
