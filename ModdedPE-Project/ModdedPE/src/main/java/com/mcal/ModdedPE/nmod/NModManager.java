package com.mcal.ModdedPE.nmod;
import android.content.*;
import android.content.pm.*;
import java.io.*;
import java.util.*;
import android.content.pm.PackageManager.*;
import com.mcal.ModdedPE.widget.*;
import android.graphics.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.utils.*;
import com.mcal.ModdedPE.app.*;

class NModManager
{
	private ArrayList<NMod> activeNMods=new ArrayList<NMod>();
	private ArrayList<NMod> allNMods=new ArrayList<NMod>();
	private ArrayList<NMod> disabledNMods=new ArrayList<NMod>();
	private Context contextThis;
	private static NModManager instance=null;

	public static void reCalculate(Context c)
	{
		instance = new NModManager(c);
		instance.preAddNMod();
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

	public ArrayList<NMod> getActiveNMods()
	{
		return activeNMods;
	}

	public ArrayList<NMod> getActiveNModsIsValidBanner()
	{
		ArrayList<NMod> ret=new ArrayList<NMod>();
		for (NMod nmod:getActiveNMods())
		{
			if (nmod.isValidBanner())
				ret.add(nmod);
		}
		return ret;
	}

	public ArrayList<NMod> getAllNMods()
	{
		return allNMods;
	}

	private void preAddNMod()
	{
		allNMods = new ArrayList<NMod>();
		activeNMods = new ArrayList<NMod>();
		disabledNMods = new ArrayList<NMod>();

		NModOptions options = new NModOptions(contextThis);

		for (String item:options.getAllList())
		{
			if (!NModUtils.isValidPackageName(item))
			{
				options.removeByName(item);
			}
		}

		forEachItemToAddNMod(options.getActiveList(), true);
		forEachItemToAddNMod(options.getDisabledList(), false);
		refreshDatas();
	}

	public void deleteNMod(NMod nmod)
	{
		activeNMods.remove(nmod);
		disabledNMods.remove(nmod);
		allNMods.remove(nmod);
		NModOptions options=new NModOptions(contextThis);
		options.removeByName(nmod.getPackageName());
		if (nmod.getNModType() == NMod.NMOD_TYPE_ZIPPED)
		{
			String zippedNModPath = new FilePathManager(contextThis).getNModsDir() + File.separator + nmod.getPackageName();
			File file = new File(zippedNModPath);
			if (file.exists())
			{
				file.delete();
			}
		}
	}

	private void forEachItemToAddNMod(ArrayList<String> list, boolean enabled)
	{
		for (String packageName:list)
		{
			try
			{
				String zippedNModPath = new FilePathManager(contextThis).getNModsDir() + File.separator + packageName;
				ZippedNMod zippedNMod = new ZippedNMod(contextThis, new File(zippedNModPath));
				if (zippedNMod != null)
				{
					zippedNMod.setPackageName(packageName);
					addNewNMod(zippedNMod, false, enabled);
					continue;
				}

			}
			catch (IOException e)
			{}

			PackagedNMod packagedNMod = NModUtils.archivePackagedNMod(contextThis, packageName);
			if (packagedNMod != null)
			{
				addNewNMod(packagedNMod, false, enabled);
			}
		}
	}

	public ArrayList<NMod> findInstalledNMods()
	{
		PackageManager packageManager = contextThis.getPackageManager();
		List<PackageInfo> infos = packageManager.getInstalledPackages(0);
		ArrayList<NMod> ret = new ArrayList<NMod>();
		for (PackageInfo info:infos)
		{
			PackagedNMod packagedNMod = NModUtils.archivePackagedNMod(contextThis, info.packageName);
			if (packagedNMod != null)
			{
				ret.add(packagedNMod);
			}
		}
		return ret;
	}

	public boolean addNewNMod(NMod newNMod, boolean replace, boolean enabled)
	{
		if (replace)
		{
			for (NMod nmod:allNMods)
				if (nmod.getPackageName().equals(newNMod.getPackageName()))
					allNMods.remove(nmod);
			for (NMod nmod:activeNMods)
				if (nmod.getPackageName().equals(newNMod.getPackageName()))
					activeNMods.remove(nmod);
			for (NMod nmod:disabledNMods)
				if (nmod.getPackageName().equals(newNMod.getPackageName()))
					disabledNMods.remove(nmod);
			allNMods.add(newNMod);
			if (enabled)
				setActive(newNMod);
			else
				setDisable(newNMod);
			return true;
		}
		for (NMod nmod:allNMods)
			if (nmod.getPackageName().equals(newNMod.getPackageName()))
				return false;
		allNMods.add(newNMod);
		if (enabled)
			setActive(newNMod);
		else
			setDisable(newNMod);
		return true;
	}



	public void refreshDatas()
	{
		NModOptions options=new NModOptions(contextThis);

		for (String item:options.getAllList())
		{
			if (getNMod(item) == null)
			{
				options.removeByName(item);
			}
		}
	}

	public NMod getNMod(String pkgname)
	{
		for (NMod nmod:allNMods)
			if (nmod.getPackageName().equals(pkgname))
				return nmod;
		return null;
	}

	public void makeUp(NMod nmod)
	{
		NModOptions options=new NModOptions(contextThis);
		options.upNMod(nmod);
		refreshEnabledOrderList();
	}

	public void makeDown(NMod nmod)
	{
		NModOptions options=new NModOptions(contextThis);
		options.downNMod(nmod);
		refreshEnabledOrderList();
	}

	private void refreshEnabledOrderList()
	{
		NModOptions options=new NModOptions(contextThis);
		ArrayList<String> enabledList = options.getActiveList();
		activeNMods.clear();
		for (String pkgName : enabledList)
		{
			NMod nmod = getNMod(pkgName);
			if (nmod != null)
			{
				activeNMods.add(nmod);
			}
		}
	}

	public void setActive(NMod nmod)
	{
		if (nmod.isBugPack())
			return;
		NModOptions options=new NModOptions(contextThis);
		options.setIsActive(nmod, true);
		activeNMods.add(nmod);
		disabledNMods.remove(nmod);
	}

	public void setDisable(NMod nmod)
	{
		NModOptions options=new NModOptions(contextThis);
		options.setIsActive(nmod, false);
		disabledNMods.add(nmod);
		activeNMods.remove(nmod);
	}

	public ArrayList<NMod> getDisabledNMods()
	{
		return disabledNMods;
	}
}
