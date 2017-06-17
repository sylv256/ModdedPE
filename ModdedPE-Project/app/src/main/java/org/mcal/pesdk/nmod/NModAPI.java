package org.mcal.pesdk.nmod;
import android.content.*;
import android.content.res.*;
import android.os.*;
import com.google.gson.*;
import org.mcal.pesdk.utils.*;
import java.util.*;
import org.mcal.pesdk.*;

public final class NModAPI
{
	private Context mContext;
	private NModManager mNModManager;
	private NModArchiver mArchiver;
	private LauncherOptions mLauncherOptions;

	public NModAPI(Context context, LauncherOptions launcherOptions)
	{
		this.mContext = context;
		this.mNModManager = new NModManager(context);
		this.mArchiver = new NModArchiver(context);
		this.mLauncherOptions = launcherOptions;
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
		return "1.0";
	}

	
}
