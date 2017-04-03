package com.mcal.ModdedPE.nmodpe;
import android.content.*;
import java.util.*;

public class NModPEOptions
{
	private Context contextThis;
	public static final String TAG_SHARED_PREFERENCE = "nmodpe_list";
	public static final String TAG_ACTIVE_LIST = "nmodpe_active_list";
	
	public NModPEOptions(Context thisContext)
	{
		contextThis=thisContext;
	}
	
	public void setIsActive(NModPE nmodpe,boolean isActive)
	{
		if(isActive)
			addNewActive(nmodpe);
		else
			removeActive(nmodpe);
	}
	
	public boolean isActive(NModPE nmodpe)
	{
		Vector<String> activeList=getActiveList();
		return activeList.indexOf(nmodpe.getPackageName())!=-1;
	}
	
	private SharedPreferences getSharedPreferences()
	{
		return contextThis.getSharedPreferences(TAG_SHARED_PREFERENCE,Context.MODE_MULTI_PROCESS);
	}
	
	private void addNewActive(NModPE nmodpe)
	{
		SharedPreferences preferences=getSharedPreferences();
		Vector<String> activeList=getActiveList();
		if(activeList.indexOf(nmodpe.getPackageName()) == -1)
			activeList.add(nmodpe.getPackageName());
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ACTIVE_LIST,fromVector(activeList));
		editor.commit();
	}
	
	public void removeActive(NModPE nmodpe)
	{
		SharedPreferences preferences=getSharedPreferences();
		Vector<String> activeList=getActiveList();
		if(activeList.indexOf(nmodpe.getPackageName()) != -1)
			activeList.remove(nmodpe.getPackageName());
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ACTIVE_LIST,fromVector(activeList));
		editor.commit();
	}
	
	public void upNModPE(NModPE nmodpe)
	{
		SharedPreferences preferences=getSharedPreferences();
		Vector<String> activeList=getActiveList();
		int index=activeList.indexOf(nmodpe.getPackageName());
		if(index==-1||index==0)
			return;
		int indexFront=index-1;
		String nameFront=activeList.get(indexFront);
		if(nameFront==null||nameFront.isEmpty())
			return;
		String nameSelf=nmodpe.getPackageName();
		activeList.set(indexFront,nameSelf);
		activeList.set(index,nameFront);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ACTIVE_LIST,fromVector(activeList));
		editor.commit();
	}
	
	public void downNModPE(NModPE nmodpe)
	{
		SharedPreferences preferences=getSharedPreferences();
		Vector<String> activeList=getActiveList();
		int index=activeList.indexOf(nmodpe.getPackageName());
		if(index==-1||index==(activeList.size()-1))
			return;
		int indexBack=index+1;
		String nameBack=activeList.get(indexBack);
		if(nameBack==null||nameBack.isEmpty())
			return;
		String nameSelf=nmodpe.getPackageName();
		activeList.set(indexBack,nameSelf);
		activeList.set(index,nameBack);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(TAG_ACTIVE_LIST,fromVector(activeList));
		editor.commit();
	}
	
	public Vector<String> getActiveList()
	{
		SharedPreferences preferences=getSharedPreferences();
		return toVector(preferences.getString(TAG_ACTIVE_LIST,""));
	}
	
	private static Vector<String> toVector(String str)
	{
		String[] mstr=str.split("/");
		Vector<String> list = new Vector<String>();
		if(mstr!=null)
		{
			for(String strElement:mstr)
			{
				if(strElement!=null&&!strElement.isEmpty())
					list.add(strElement);
			}
		}
		return list;
	}
	
	private static String fromVector(Vector<String> vector)
	{
		String str="";
		if(vector!=null)
		{
			for(String mstr:vector)
			{
				str+=mstr;
				str+="/";
			}
		}
		return str;
	}
}
