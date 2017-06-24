package org.mcal.pesdk.nmod;

import android.content.Context;

import org.mcal.pesdk.utils.LauncherOptions;

import java.util.ArrayList;

public final class NModAPI
{
	private Context mContext;
	private NModManager mNModManager;
	private NModArchiver mArchiver;

	public NModAPI(Context context)
	{
		this.mContext = context;
		this.mNModManager = new NModManager(context);
		this.mArchiver = new NModArchiver(context);
	}

	public ZippedNMod archiveZippedNMod(String filePath) throws ArchiveFailedException
	{
		return mArchiver.archiveFromZipped(filePath);
	}

	public void initNModDatas()
	{
		mNModManager.init();
	}

	public ArrayList<NMod> getLoadedNMods()
	{
		return mNModManager.getAllNMods();
	}

	public ArrayList<NMod> getImportedEnabledNMods()
	{
		return mNModManager.getEnabledNMods();
	}

	public ArrayList<NMod> getImportedDisabledNMods()
	{
		return mNModManager.getDisabledNMods();
	}

	public ArrayList<NMod> getImportedEnabledNModsHaveBanners()
	{
		return mNModManager.getEnabledNModsIsValidBanner();
	}

	public ArrayList<NMod> findInstalledNMods()
	{
		NModArchiver arvhiver = new NModArchiver(mContext);
		return arvhiver.archiveAllFromInstalled();
	}

	public boolean importNMod(NMod nmod)
	{
		return mNModManager.importNMod(nmod, false);
	}

	public void removeImportedNMod(NMod nmod)
	{
		mNModManager.removeImportedNMod(nmod);
	}

	public void setEnabled(NMod nmod, boolean enabled)
	{
		if (enabled)
			mNModManager.setEnabled(nmod);
		else
			mNModManager.setDisable(nmod);
	}

	public void upPosNMod(NMod nmod)
	{
		mNModManager.makeUp(nmod);
	}

	public void downPosNMod(NMod nmod)
	{
		mNModManager.makeDown(nmod);
	}

	public PackagedNMod archivePackagedNMod(String packageName) throws ArchiveFailedException
	{
		NModArchiver arvhiver = new NModArchiver(mContext);
		return arvhiver.archiveFromInstalledPackage(packageName);
	}

	public String getVersionName()
	{
		return "1.1";
	}

	
}
