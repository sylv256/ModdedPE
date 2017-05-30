package com.mcal.ModdedPE.nmod;
import android.content.*;
import java.util.*;

class NModOptions
{
	private Context contextThis;
	public static final String TAG_SHARED_PREFERENCE = "nmod_data_list";
	public static final String TAG_ACTIVE_LIST = "enabled_nmods_list";
	public static final String TAG_DISABLE_LIST = "disabled_nmods_list";

	public NModOptions(Context thisContext)
	{
		contextThis = thisContext;
	}

	public ArrayList<String> getAllList()
	{
		ArrayList<String> ret = new ArrayList<String>();
		ret.addAll(getDisabledList());
		ret.addAll(getActiveList());
		return ret;
	}

	public void removeByName(String name)
	{
		SharedPreferences preferences=getSharedPreferences();
		ArrayList<String> activeList=getActiveList();
		ArrayList<String> disableList=getDisabledList();
		activeList.remove(name);
		disableList.remove(name);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ACTIVE_LIST, fromArrayList(activeList));
		editor.putString(TAG_DISABLE_LIST, fromArrayList(disableList));
		editor.commit();
	}

	public void remove(NMod nmod)
	{
		removeByName(nmod.getPackageName());
	}

	public void setIsActive(NMod nmod, boolean isActive)
	{
		if (isActive)
			addNewActive(nmod);
		else
			removeActive(nmod);
	}

	public boolean isActive(NMod nmod)
	{
		ArrayList<String> activeList=getActiveList();
		return activeList.indexOf(nmod.getPackageName()) != -1;
	}

	private SharedPreferences getSharedPreferences()
	{
		return contextThis.getSharedPreferences(TAG_SHARED_PREFERENCE, Context.MODE_MULTI_PROCESS);
	}

	private void addNewActive(NMod nmod)
	{
		SharedPreferences preferences=getSharedPreferences();
		ArrayList<String> activeList=getActiveList();
		ArrayList<String> disableList=getDisabledList();
		if (activeList.indexOf(nmod.getPackageName()) == -1)
			activeList.add(nmod.getPackageName());
		disableList.remove(nmod.getPackageName());
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ACTIVE_LIST, fromArrayList(activeList));
		editor.putString(TAG_DISABLE_LIST, fromArrayList(disableList));
		editor.commit();
	}

	public void removeActive(NMod nmod)
	{
		SharedPreferences preferences=getSharedPreferences();
		ArrayList<String> activeList=getActiveList();
		ArrayList<String> disableList=getDisabledList();
		activeList.remove(nmod.getPackageName());
		if (disableList.indexOf(nmod.getPackageName()) == -1)
			disableList.add(nmod.getPackageName());
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ACTIVE_LIST, fromArrayList(activeList));
		editor.putString(TAG_DISABLE_LIST, fromArrayList(disableList));
		editor.commit();
	}

	public void upNMod(NMod nmod)
	{
		SharedPreferences preferences=getSharedPreferences();
		ArrayList<String> activeList=getActiveList();
		int index=activeList.indexOf(nmod.getPackageName());
		if (index == -1 || index == 0)
			return;
		int indexFront=index - 1;
		String nameFront=activeList.get(indexFront);
		if (nameFront == null || nameFront.isEmpty())
			return;
		String nameSelf=nmod.getPackageName();
		activeList.set(indexFront, nameSelf);
		activeList.set(index, nameFront);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ACTIVE_LIST, fromArrayList(activeList));
		editor.commit();
	}

	public void downNMod(NMod nmod)
	{
		SharedPreferences preferences=getSharedPreferences();
		ArrayList<String> activeList=getActiveList();
		int index=activeList.indexOf(nmod.getPackageName());
		if (index == -1 || index == (activeList.size() - 1))
			return;
		int indexBack=index + 1;
		String nameBack=activeList.get(indexBack);
		if (nameBack == null || nameBack.isEmpty())
			return;
		String nameSelf=nmod.getPackageName();
		activeList.set(indexBack, nameSelf);
		activeList.set(index, nameBack);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ACTIVE_LIST, fromArrayList(activeList));
		editor.commit();
	}

	public ArrayList<String> getActiveList()
	{
		SharedPreferences preferences=getSharedPreferences();
		return toArrayList(preferences.getString(TAG_ACTIVE_LIST, ""));
	}

	public ArrayList<String> getDisabledList()
	{
		SharedPreferences preferences=getSharedPreferences();
		return toArrayList(preferences.getString(TAG_DISABLE_LIST, ""));
	}
	private static ArrayList<String> toArrayList(String str)
	{
		String[] mstr=str.split("/");
		ArrayList<String> list = new ArrayList<String>();
		if (mstr != null)
		{
			for (String strElement:mstr)
			{
				if (strElement != null && !strElement.isEmpty())
					list.add(strElement);
			}
		}
		return list;
	}

	private static String fromArrayList(ArrayList<String> arrayList)
	{
		String str="";
		if (arrayList != null)
		{
			for (String mstr:arrayList)
			{
				str += mstr;
				str += "/";
			}
		}
		return str;
	}
}
