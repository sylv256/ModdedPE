package com.mcal.ModdedPE.nmod;
import android.content.*;
import java.util.*;
import android.os.*;

public final class NModAPI
{
	private Context context;
	private static NModAPI instance;
	private NModAPI(Context context)
	{
		this.context = context;
	}

	public static NModAPI getInstance(Context context)
	{
		createInstance(context);
		return instance;
	}

	public static void createInstance(Context context)
	{
		if (instance == null)
		{
			instance = new NModAPI(context);
		}
	}
	
	public void perloadNModDatas()
	{
		
	}
	
	public void copyPerlaunchFiles()
	{
		
	}
	
	public Vector<NMod> getLoadedNMods()
	{
		return null;
	}
	
	public Vector<NMod> getLoadedEnabledNMods()
	{
		return null;
	}
	
	public Vector<NMod> getLoadedDisabledNMods()
	{
		return null;
	}
	
	public Vector<NMod> getLoadedEnabledNModsHaveBanners()
	{
		return null;
	}
	
	public boolean addNewNMod(ZippedNMod nmod)
	{
		return false;
	}
	
	public boolean addNewNMod(PackagedNMod nmod)
	{
		return false;
	}
	
	public void replaceAddNMod(NMod nmod)
	{

	}
	
	public void addNModLoadInfosToBundle(Bundle bundle)
	{
		
	}
	
	public Vector<String> getNativeLibsPathsFromBundle(Bundle bundle)
	{
		return null;
	}
	
	public Vector<String> getPackageResourcePathsFromBundle(Bundle bundle)
	{
		return null;
	}
}
