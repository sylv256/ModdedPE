package com.mcal.ModdedPE.nmod;
import android.content.*;
import java.util.*;
import android.os.*;
import android.content.res.*;
import com.mojang.minecraftpe.*;
import com.google.gson.*;
import com.mcal.ModdedPE.utils.*;
import android.support.annotation.*;
import java.io.*;
import android.content.pm.*;
import java.util.zip.*;

public final class NModAPI
{
	private Context context;
	private static NModAPI instance;

	public static final String NMOD_DATA_TAG = "nmod_data";
	public static final int MSG_COPYING_NMOD_FILES = 5623;
	public static final int MSG_PERLOADING_NATIVE_LIBS = 5624;
	public static final int MSG_PERLOADING_NATIVE_LIBS_FAILED = 5625;
	public static final int MSG_MERGING_ASSETS = 5626;
	public static final int MSG_LOADING_DEX = 5627;

	public static final int ADD_RETURN_CODE_SUCCEED = 0;
	public static final int ADD_RETURN_CODE_REPLACED = 1;
	public static final int ENABLE_RETURN_CODE_VERSION_NOT_TARGET = 2;
	public static final int ENABLE_RETURN_CODE_API_NEEDED = 3;
	public static final int ENABLE_RETURN_CODE_ABI_NOT_TARGET = 3;
	
	private NModAPI(Context context)
	{
		this.context = context;
	}

	public ZippedNMod archiveZippedNMod(String filePath) throws NModLoadException
	{
		return NModUtils.archiveZippedNMod(context,filePath);
	}

	public static NModAPI getInstance(Context context)
	{
		createInstance(context);
		return instance;
	}

	@Deprecated
	public static NModAPI getInstance()
	{
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
		NModManager.getNModManager(context).reCalculate(context);
	}

	public void perlaunchNMods(Bundle bundle, Handler handler)
	{
		new PerlaunchNModsThread(bundle, handler).start();
	}

	private boolean loadNModElfFiles(NMod nmod, NMod.NModPerloadBean perloadDataItem, Handler handler)
	{
		if (handler == null)
			handler = new Handler();
		MinecraftInfo minecraftInfo = MinecraftInfo.getInstance(context);

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
					nmod.setBugPack(new NModLoadException("Loading native lib [" + nameItem + "] of nmod [" + nmod.getPackageName() + "(" + nmod.getName() + ")" + "] failed.", t));
					Message message3 = new Message();
					message3.what = MSG_PERLOADING_NATIVE_LIBS_FAILED;
					message3.obj = nmod;
					handler.sendMessage(message3);
					return false;
				}
			}

			for (String nameItem:perloadDataItem.native_libs)
			{
				NModLoader.callOnLoad(nameItem, minecraftInfo.getMinecraftVersionName(), context.getString(com.mcal.ModdedPE.R.string.app_name));
			}
		}
		return true;
	}

	public ArrayList<NMod> getLoadedNMods()
	{
		return NModManager.getNModManager(context).getAllNMods();
	}

	public ArrayList<NMod> getLoadedEnabledNMods()
	{
		return NModManager.getNModManager(context).getActiveNMods();
	}

	public ArrayList<NMod> getLoadedDisabledNMods()
	{
		return NModManager.getNModManager(context).getDisabledNMods();
	}

	public ArrayList<NMod> getLoadedEnabledNModsHaveBanners()
	{
		return NModManager.getNModManager(context).getActiveNModsIsValidBanner();
	}

	public ArrayList<NMod> findInstalledNMods()
	{
		return NModManager.getNModManager(context).findInstalledNMods();
	}

	public boolean addNewNMod(NMod nmod, boolean replace)
	{
		return NModManager.getNModManager(context).addNewNMod(nmod, replace, false);
	}

	public void loadToGame(Bundle bundle, AssetManager mgr, Handler handler)
	{
		Gson gson = new Gson();
		NModPerloadData perloadData = gson.fromJson(bundle.getString(NMOD_DATA_TAG), NModPerloadData.class);


		for (int i=perloadData.assets_packs_path.length - 1;i >= 0;--i)
		{
			String assetsPath = perloadData.assets_packs_path[i];
			AssetOverrideManager.addAssetOverride(mgr, assetsPath);
		}

	}

	public void removeNMod(NMod nmod)
	{
		NModManager.getNModManager(context).deleteNMod(nmod);
	}

	public void setEnabled(NMod nmod, boolean enabled)
	{
		if (enabled)
			NModManager.getNModManager(context).setActive(nmod);
		else
			NModManager.getNModManager(context).setDisable(nmod);
	}

	public void upPosNMod(NMod nmod)
	{
		NModManager.getNModManager(context).makeUp(nmod);
	}

	public void downPosNMod(NMod nmod)
	{
		NModManager.getNModManager(context).makeDown(nmod);
	}

	public PackagedNMod archivePackagedNMod(String packageName)
	{
		return NModUtils.archivePackagedNMod(context, packageName);
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

	private class PerlaunchNModsThread extends Thread
	{
		private Bundle bundle;
		private Handler handler;

		public PerlaunchNModsThread(Bundle bundle, Handler handler)
		{
			this.bundle = bundle;
			this.handler = handler;
		}

		@Override
		public void run()
		{
			if (handler == null)
				handler = new Handler();
			Gson gson = new Gson();
			NModPerloadData perloadData = new NModPerloadData();
			ArrayList<String> assetsArrayList = new ArrayList<String>();
			ArrayList<String> dexPathArrayList = new ArrayList<String>();
			ArrayList<String> loadedNativeLibs = new ArrayList<String>();

			ArrayList<NMod> loadedEnabledNMods = getLoadedEnabledNMods();
			for (NMod nmod:loadedEnabledNMods)
			{
				Message message = new Message();
				message.what = MSG_COPYING_NMOD_FILES;
				message.obj = nmod;
				handler.sendMessage(message);

				NMod.NModPerloadBean perloadDataItem = nmod.copyNModFiles();

				Message message2 = new Message();
				message2.what = MSG_PERLOADING_NATIVE_LIBS;
				message2.obj = nmod;
				handler.sendMessage(message2);

				if (loadNModElfFiles(nmod, perloadDataItem, handler))
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
			bundle.putString(NMOD_DATA_TAG, gson.toJson(perloadData));
		}
	}

	private class NModPerloadData
	{
		String[] assets_packs_path;
		String[] dex_path;
		String[] loaded_libs;
	}
}
