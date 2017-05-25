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

public class NModManager
{
	private Vector<NMod> activeNMods=new Vector<NMod>();
	private Vector<NMod> allNMods=new Vector<NMod>();
	private Vector<NMod> disabledNMods=new Vector<NMod>();
	private Context contextThis;
	private Loader loader = new Loader();
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

	public Vector<NMod> getActiveNMods()
	{
		return activeNMods;
	}

	public Vector<NMod> getActiveNModsIsValidBanner()
	{
		Vector<NMod> ret=new Vector<NMod>();
		for (NMod nmod:getActiveNMods())
		{
			if (nmod.isValidBanner())
				ret.add(nmod);
		}
		return ret;
	}

	public Vector<NMod> getAllNMods()
	{
		return allNMods;
	}

	private void preAddNMod()
	{
		allNMods = new Vector<NMod>();
		activeNMods = new Vector<NMod>();
		disabledNMods = new Vector<NMod>();

		NModOptions options = new NModOptions(contextThis);
		Vector<String> allList = options.getAllList();

		for (String packageName:allList)
		{
			try
			{
				Context contextPackage = contextThis.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
				try
				{
					InputStream is = contextPackage.getAssets().open(NMod.MANIFEST_NAME);
					if (is != null)
					{
						is.close();
						addNewNMod(new PackagedNMod(contextThis, contextPackage));
					}
				}
				catch (IOException e)
				{

				}
			}
			catch (PackageManager.NameNotFoundException e)
			{
				File nmod_file=new File(new FilePathManager(contextThis).getNModLibsPath() + File.separator + packageName);
				if (nmod_file.exists())
				{
					try
					{
						ZippedNMod newZippedNMod = new ZippedNMod(contextThis, nmod_file);
						addNewNMod(newZippedNMod);
					}
					catch (IOException e2)
					{}
				}
			}
		}

		refreshDatas();
	}

	public Vector<NMod> findInstalledNMods()
	{
		PackageManager packageManager = contextThis.getPackageManager();
		List<PackageInfo> infos = packageManager.getInstalledPackages(0);
		Vector<NMod> ret = new Vector<NMod>();
		for (PackageInfo info:infos)
		{
			try
			{
				Context contextPackage = contextThis.createPackageContext(info.applicationInfo.packageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
				contextPackage.getAssets().open(NMod.MANIFEST_NAME).close();
				ret.add(new PackagedNMod(contextThis,contextPackage));
			}
			catch (IOException e)
			{}
			catch(PackageManager.NameNotFoundException notFoundE)
			{}
		}
		return ret;
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

		for (String item:options.getAllList())
		{
			if (getNMod(item) == null)
			{
				options.removeByName(item);
			}
		}

		Vector<String> activeList=options.getActiveList();
		activeNMods = new Vector<NMod>();
		for (String item:activeList)
		{
			NMod nmod=getNMod(item);
			if (nmod != null)
				activeNMods.add(nmod);
		}

		Vector<String> disabledList=options.getDisabledList();
		disabledNMods = new Vector<NMod>();
		for (String item:disabledList)
		{
			NMod nmod=getNMod(item);
			if (nmod != null)
				disabledNMods.add(nmod);
		}
	}

	public NMod getNMod(String pkgname)
	{
		for (NMod nmod:allNMods)
			if (nmod.getPackageName().equals(pkgname))
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

	public Loader getLoader()
	{
		return loader;
	}

	public class Loader
	{
		public void loadNModLibs()
		{
			
		}
	}
}
