package org.mcal.pesdk.nmod;
import android.content.*;
import java.io.*;
import java.util.*;

class NModManager
{
	private ArrayList<NMod> mEnabledNMods=new ArrayList<NMod>();
	private ArrayList<NMod> mAllNMods=new ArrayList<NMod>();
	private ArrayList<NMod> mDisabledNMods=new ArrayList<NMod>();
	private Context mContext;

	NModManager(Context context)
	{
		this.mContext = context;
	}

	ArrayList<NMod> getEnabledNMods()
	{
		return mEnabledNMods;
	}

	ArrayList<NMod> getEnabledNModsIsValidBanner()
	{
		ArrayList<NMod> ret=new ArrayList<NMod>();
		for (NMod nmod:getEnabledNMods())
		{
			if (nmod.isValidBanner())
				ret.add(nmod);
		}
		return ret;
	}

	ArrayList<NMod> getAllNMods()
	{
		return mAllNMods;
	}

	void init()
	{
		mAllNMods = new ArrayList<NMod>();
		mEnabledNMods = new ArrayList<NMod>();
		mDisabledNMods = new ArrayList<NMod>();

		NModDataLoader dataloader = new NModDataLoader(mContext);

		for (String item:dataloader.getAllList())
		{
			if (!PackageNameChecker.isValidPackageName(item))
			{
				dataloader.removeByName(item);
			}
		}

		forEachItemToAddNMod(dataloader.getEnabledList(), true);
		forEachItemToAddNMod(dataloader.getDisabledList(), false);
		refreshDatas();
	}

	void removeImportedNMod(NMod nmod)
	{
		mEnabledNMods.remove(nmod);
		mDisabledNMods.remove(nmod);
		mAllNMods.remove(nmod);
		NModDataLoader dataloader=new NModDataLoader(mContext);
		dataloader.removeByName(nmod.getPackageName());
		if (nmod.getNModType() == NMod.NMOD_TYPE_ZIPPED)
		{
			String zippedNModPath = new NModFilePathManager(mContext).getNModsDir() + File.separator + nmod.getPackageName();
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
				String zippedNModPath = new NModFilePathManager(mContext).getNModsDir() + File.separator + packageName;
				ZippedNMod zippedNMod = new ZippedNMod(packageName, mContext, new File(zippedNModPath));
				if (zippedNMod != null)
				{
					importNMod(zippedNMod, enabled);
					continue;
				}

			}
			catch (IOException e)
			{}

			try
			{
				NModArchiver archiver = new NModArchiver(mContext);
				PackagedNMod packagedNMod = archiver.archiveFromInstalledPackage(packageName);
				importNMod(packagedNMod, enabled);
				continue;
			}
			catch (ArchiveFailedException e)
			{}
		}
	}

	boolean importNMod(NMod newNMod, boolean enabled)
	{
		boolean replaced = false;
		Iterator<NMod> iterator = mAllNMods.iterator();
		while (iterator.hasNext())
		{
			NMod nmod = iterator.next();
			if (nmod.equals(newNMod))
			{
				iterator.remove();
				mEnabledNMods.remove(nmod);
				mDisabledNMods.remove(nmod);
				replaced = true;
			}
		}

		mAllNMods.add(newNMod);
		if (enabled)
			setEnabled(newNMod);
		else
			setDisable(newNMod);
		return replaced;
	}



	void refreshDatas()
	{
		NModDataLoader dataloader=new NModDataLoader(mContext);

		for (String item:dataloader.getAllList())
		{
			if (getImportedNMod(item) == null)
			{
				dataloader.removeByName(item);
			}
		}
	}

	NMod getImportedNMod(String pkgname)
	{
		for (NMod nmod : mAllNMods)
			if (nmod.getPackageName().equals(pkgname))
				return nmod;
		return null;
	}

	void makeUp(NMod nmod)
	{
		NModDataLoader dataloader=new NModDataLoader(mContext);
		dataloader.upNMod(nmod);
		refreshEnabledOrderList();
	}

	void makeDown(NMod nmod)
	{
		NModDataLoader dataloader=new NModDataLoader(mContext);
		dataloader.downNMod(nmod);
		refreshEnabledOrderList();
	}

	private void refreshEnabledOrderList()
	{
		NModDataLoader dataloader=new NModDataLoader(mContext);
		ArrayList<String> enabledList = dataloader.getEnabledList();
		mEnabledNMods.clear();
		for (String pkgName : enabledList)
		{
			NMod nmod = getImportedNMod(pkgName);
			if (nmod != null)
			{
				mEnabledNMods.add(nmod);
			}
		}
	}

	void setEnabled(NMod nmod)
	{
		if (nmod.isBugPack())
			return;
		NModDataLoader dataloader=new NModDataLoader(mContext);
		dataloader.setIsEnabled(nmod, true);
		mEnabledNMods.add(nmod);
		mDisabledNMods.remove(nmod);
	}

	void setDisable(NMod nmod)
	{
		NModDataLoader dataloader=new NModDataLoader(mContext);
		dataloader.setIsEnabled(nmod, false);
		mDisabledNMods.add(nmod);
		mEnabledNMods.remove(nmod);
	}

	ArrayList<NMod> getDisabledNMods()
	{
		return mDisabledNMods;
	}
}
