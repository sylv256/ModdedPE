package com.mcal.ModdedPE.nmod;
import android.content.*;
import android.content.pm.*;
import java.io.*;
import java.util.*;

public class NModManager
{
	private Vector<NMod> activeNMods=new Vector<NMod>();
	private Vector<NMod> allNMods=new Vector<NMod>();
	private Vector<NMod> disabledNMods=new Vector<NMod>();
	private Context contextThis;
	private static NModManager instance=null;

	public static void reCalculate(Context c)
	{
		instance = new NModManager(c);
		instance.searchAndAddNMod();
	}

	public static NModManager getNModManager(Context c)
	{
		if (instance == null)
			instance = new NModManager(c);
		return instance;
	}

	private NModManager(Context contextThis)
	{
		this.contextThis = contextThis;
	}

	public Vector<NMod> getActiveNMods()
	{
		return activeNMods;
	}

	public Vector<NMod> getActiveNModsHasNews()
	{
		Vector<NMod> ret=new Vector<NMod>();
		for (NMod nmod:getActiveNMods())
		{
			if (nmod.isValidNews())
				ret.add(nmod);
		}
		return ret;
	}

	public Vector<NMod> getAllNMods()
	{
		return allNMods;
	}

	public void searchAndAddNMod()
	{
		allNMods = new Vector<NMod>();
		activeNMods = new Vector<NMod>();

		PackageManager packageManager = contextThis.getPackageManager();
		List<PackageInfo> infos=packageManager.getInstalledPackages(0);
		for (PackageInfo info:infos)
		{
			try
			{
				Context contextPackage=contextThis.createPackageContext(info.applicationInfo.packageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
				InputStream is=contextPackage.getAssets().open(NMod.TAG_MANIFEST_NAME);
				if (is != null)
					addNewNMod(new PackagedNMod(contextPackage, contextThis));
			}
			catch (Throwable e)
			{

			}
		}

		refreshDatas();
	}

	private void addNewNMod(NMod newNMod)
	{
		for (NMod nmod:allNMods)
			if (nmod.getPackageName().equals(newNMod.getPackageName()))
				return;
		allNMods.add(newNMod);
	}

	public void refreshDatas()
	{
		NModOptions options=new NModOptions(contextThis);
		Vector<String> activeList=options.getActiveList();

		activeNMods = new Vector<NMod>();
		for (String item:activeList)
		{
			NMod nmod=getNMod(item);
			if (nmod != null)
				activeNMods.add(nmod);
		}

		disabledNMods = new Vector<NMod>();
		for (NMod nmod:allNMods)
		{
			if (activeNMods.indexOf(nmod) == -1)
				disabledNMods.add(nmod);
		}

		for (String item:activeList)
		{
			if (getNMod(item) == null || getNMod(item).isBugPack())
				options.removeByName(item);
		}
	}

	private NMod getNMod(String name)
	{
		for (NMod nmod:allNMods)
			if (nmod.getPackageName().equals(name))
				return nmod;
		return null;
	}

	public void removeActive(NMod nmod)
	{
		NModOptions options=new NModOptions(contextThis);
		options.setIsActive(nmod, false);
		activeNMods.remove(nmod);
		refreshDatas();
	}

	public void makeUp(NMod nmod)
	{
		NModOptions options=new NModOptions(contextThis);
		options.upNMod(nmod);
		refreshDatas();
	}

	public void makeDown(NMod nmod)
	{
		NModOptions options=new NModOptions(contextThis);
		options.downNMod(nmod);
		refreshDatas();
	}

	public void addActive(NMod nmod)
	{
		if (nmod.isBugPack())
			return;
		NModOptions options=new NModOptions(contextThis);
		options.setIsActive(nmod, true);
		activeNMods.add(nmod);
		refreshDatas();
	}

	public Vector<NMod> getDisabledNMods()
	{
		return disabledNMods;
	}

	public NMod getNModByPackageName(String packageName)
	{
		for (NMod nmod:allNMods)
		{
			if (nmod.getPackageName().equals(packageName))
				return nmod;
		}
		return null;
	}
}
