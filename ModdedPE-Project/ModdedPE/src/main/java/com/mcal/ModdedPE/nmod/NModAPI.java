package com.mcal.ModdedPE.nmod;
import android.content.*;
import android.content.res.*;
import android.os.*;
import com.google.gson.*;
import com.mcal.ModdedPE.utils.*;
import java.util.*;

public final class NModAPI
{
	private Context mContext;
	private NModManager mNModManager;
	private NModArchiver mArchiver;
	private static NModAPI mInstance;

	public static final String NMOD_DATA_TAG = "nmod_data";
	public static final int MSG_COPYING_NMOD_FILES = 5623;
	public static final int MSG_PERLOADING_NATIVE_LIBS = 5624;
	public static final int MSG_PERLOADING_NATIVE_LIBS_FAILED = 5625;
	public static final int MSG_MERGING_ASSETS = 5626;
	public static final int MSG_LOADING_DEX = 5627;

	public static final String FILEPATH_DIR_NAME_NMOD_PACKS = "nmod_packs";
	public static final String FILEPATH_DIR_NAME_NMOD_LIBS = "nmod_libs";
	public static final String FILEPATH_DIR_NAME_NMOD_LANG_DATA = "nmod_lang";
	public static final String FILEPATH_FILE_NAME_NMOD_CAHCHE = "nmod_cached";
	
	
	private NModAPI(Context context)
	{
		this.mContext = context;
		this.mNModManager = new NModManager(context);
		this.mArchiver = new NModArchiver(context);
	}

	public ZippedNMod archiveZippedNMod(String filePath) throws ArchiveFailedException
	{
		return mArchiver.archiveFromStorage(filePath);
	}

	public static NModAPI getInstance(Context context)
	{
		createInstance(context);
		return mInstance;
	}

	@Deprecated
	public static NModAPI getInstance()
	{
		return mInstance;
	}

	public static void createInstance(Context context)
	{
		if (mInstance == null)
		{
			mInstance = new NModAPI(context);
		}
	}

	public void initNModDatas()
	{
		mNModManager.init();
	}

	public void perloadNMods(Bundle bundle, Handler handler)
	{
		new PerloadNModsThread(bundle, handler).start();
	}

	private boolean loadNModElfFiles(NMod nmod, NMod.NModPerloadBean perloadDataItem, Handler handler)
	{
		if (handler == null)
			handler = new Handler();
		MinecraftInfo minecraftInfo = MinecraftInfo.getInstance(mContext);

		if (perloadDataItem.native_libs != null && perloadDataItem.native_libs.length > 0)
		{
			for (String nameItem:perloadDataItem.native_libs)
			{
				try
				{
					System.load(nameItem);
				}
				catch (Throwable t)
				{
					nmod.setBugPack(new LoadFailedException("Loading native lib [" + nameItem + "] of nmod [" + nmod.getPackageName() + "(" + nmod.getName() + ")" + "] failed.", t));
					Message message3 = new Message();
					message3.what = MSG_PERLOADING_NATIVE_LIBS_FAILED;
					message3.obj = nmod;
					handler.sendMessage(message3);
					return false;
				}
			}

			for (String nameItem:perloadDataItem.native_libs)
			{
				NModLoader.callOnLoad(nameItem, minecraftInfo.getMinecraftVersionName(), mContext.getString(com.mcal.ModdedPE.R.string.app_name));
			}
		}
		return true;
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

	
	/*
	  Called in onCreate of com.mojang.minecraftpe.MainActicity or it's child classes.
	  @prama Bundle : Extras in Intent.
	  @prama AssetManager : Assets of com.mojang.minecraftpe.MainActicity or it's child classes.
	  @prama handler : UI handler.It can be null.
	*/
	
	public void loadToGame(Bundle bundle, AssetManager assetManager, Handler handler)
	{
		Gson gson = new Gson();
		NModPerloadData perloadData = gson.fromJson(bundle.getString(NMOD_DATA_TAG), NModPerloadData.class);


		for (int i=perloadData.assets_packs_path.length - 1;i >= 0;--i)
		{
			String assetsPath = perloadData.assets_packs_path[i];
			AssetOverrideManager.addAssetOverride(assetManager, assetsPath);
		}

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

	public void callOnActivityCreate(com.mojang.minecraftpe.MainActivity activity, Bundle savedInstanceState, Bundle data)
	{
		Gson gson = new Gson();
		NModPerloadData perloadData = gson.fromJson(data.getString(NMOD_DATA_TAG), NModPerloadData.class);

		String[] loadedNModLibs = perloadData.loaded_libs;
		for (int i=loadedNModLibs.length - 1;i >= 0;--i)
		{
			String nativeLibName = loadedNModLibs[i];
			NModLoader.callOnActivityCreate(nativeLibName, activity, savedInstanceState);
		}
	}

	public void callOnActivityDestroy(com.mojang.minecraftpe.MainActivity activity, Bundle data)
	{
		Gson gson = new Gson();
		NModPerloadData perloadData = gson.fromJson(data.getString(NMOD_DATA_TAG), NModPerloadData.class);

		String[] loadedNModLibs = perloadData.loaded_libs;
		for (int i=loadedNModLibs.length - 1;i >= 0;--i)
		{
			String nativeLibName = loadedNModLibs[i];
			NModLoader.callOnActivityFinish(nativeLibName, activity);
		}
	}

	private class PerloadNModsThread extends Thread
	{
		private Bundle mBundle;
		private Handler mHandler;
		
		public PerloadNModsThread(Bundle bundle, Handler handler)
		{
			this.mBundle = bundle;
			this.mHandler = handler;
		}

		@Override
		public void run()
		{
			if (mHandler == null)
				mHandler = new Handler();
			Gson gson = new Gson();
			NModPerloadData perloadData = new NModPerloadData();
			ArrayList<String> assetsArrayList = new ArrayList<String>();
			ArrayList<String> dexPathArrayList = new ArrayList<String>();
			ArrayList<String> loadedNativeLibs = new ArrayList<String>();

			ArrayList<NMod> loadedEnabledNMods = getImportedEnabledNMods();
			for (NMod nmod:loadedEnabledNMods)
			{
				Message message = new Message();
				message.what = MSG_COPYING_NMOD_FILES;
				message.obj = nmod;
				mHandler.sendMessage(message);

				NMod.NModPerloadBean perloadDataItem = nmod.copyNModFiles();

				Message message2 = new Message();
				message2.what = MSG_PERLOADING_NATIVE_LIBS;
				message2.obj = nmod;
				mHandler.sendMessage(message2);

				if (loadNModElfFiles(nmod, perloadDataItem, mHandler))
				{
					if (perloadDataItem.assets_path != null)
						assetsArrayList.add(perloadDataItem.assets_path);
					if (perloadDataItem.dex_path != null)
						dexPathArrayList.add(perloadDataItem.dex_path);

					if (perloadDataItem.native_libs != null && perloadDataItem.native_libs.length > 0)
					{
						for (String nameItem:perloadDataItem.native_libs)
						{
							loadedNativeLibs.add(nameItem);
						}
					}
				}
			}
			perloadData.assets_packs_path = (String[])assetsArrayList.toArray();
			perloadData.dex_path = (String[])dexPathArrayList.toArray();
			perloadData.loaded_libs = (String[])loadedNativeLibs.toArray();
			mBundle.putString(NMOD_DATA_TAG, gson.toJson(perloadData));
		}
	}

	private class NModPerloadData
	{
		String[] assets_packs_path;
		String[] dex_path;
		String[] loaded_libs;
	}
}
