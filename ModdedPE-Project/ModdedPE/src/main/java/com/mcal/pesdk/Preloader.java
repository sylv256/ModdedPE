package com.mcal.pesdk;

import android.os.*;
import com.google.gson.*;
import com.mcal.pesdk.nmod.*;
import com.mcal.pesdk.utils.*;
import java.util.*;
import com.mcal.pesdk.nativeapi.*;
import android.content.*;

public class Preloader
{
	private Bundle mBundle;
	private Handler mHandler;
	private PESdk mPESdk;

	public Preloader(Bundle bundle, Handler handler, PESdk pesdk)
	{
		mBundle = bundle;
		mHandler = handler;
		mPESdk = pesdk;
	}

	public void start()
	{
		if (mHandler == null)
			mHandler = new Handler();
		if (mBundle == null)
			mBundle = new Bundle();
		Gson gson = new Gson();
		boolean safeMode = mPESdk.getLauncherOptions().isSafeMode();

		String abiInfo = ABIInfo.getTargetABIType();
		if (abiInfo == null)
		{
			mHandler.sendEmptyMessage(PreloadingInfo.MSG_UNSUPPORTED_ABI);
			return;
		}

		try
		{
			mHandler.sendEmptyMessage(PreloadingInfo.MSG_LOADING_SUBSTRATE_FRAMEWORK);
			LibraryLoader.loadSubstrate();
			mHandler.sendEmptyMessage(PreloadingInfo.MSG_LOADING_GAME_LAUNCHER);
			LibraryLoader.loadLauncher();
			mHandler.sendEmptyMessage(PreloadingInfo.MSG_LOADING_FMOD);
			LibraryLoader.loadFMod(mPESdk.getMinecraftInfo().getMinecraftNativeLibraryDir());
			mHandler.sendEmptyMessage(PreloadingInfo.MSG_LOADING_MINECRAFT_PE);
			LibraryLoader.loadMinecraftPE(mPESdk.getMinecraftInfo().getMinecraftNativeLibraryDir());
			if (!safeMode)
			{
				mHandler.sendEmptyMessage(PreloadingInfo.MSG_LOADING_NMOD_API);
				LibraryLoader.loadNModAPI();
				mHandler.sendEmptyMessage(PreloadingInfo.MSG_LOADING_LIBS_SUCCEEDED);
			}
		}
		catch (Throwable throwable)
		{
			Message failMessage = new Message();
			failMessage.what = PreloadingInfo.MSG_LOADING_LIBS_FAILED;
			failMessage.obj = throwable;
			mHandler.sendMessage(failMessage);
			return;
		}


		if (!safeMode)
		{
			NModPreloadData perloadData = new NModPreloadData();
			ArrayList<String> assetsArrayList = new ArrayList<String>();
			ArrayList<String> dexPathArrayList = new ArrayList<String>();
			ArrayList<String> loadedNativeLibs = new ArrayList<String>();

			ArrayList<NMod> loadedEnabledNMods = mPESdk.getNModAPI().getImportedEnabledNMods();
			for (NMod nmod:loadedEnabledNMods)
			{
				Message message = new Message();
				message.what = PreloadingInfo.MSG_COPYING_NMOD_FILES;
				message.obj = nmod;
				mHandler.sendMessage(message);

				NMod.NModPerloadBean perloadDataItem = nmod.copyNModFiles();

				Message message2 = new Message();
				message2.what = PreloadingInfo.MSG_PRELOADING_NATIVE_LIBS;
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
			perloadData.assets_packs_path = assetsArrayList.toArray(new String[0]);
			perloadData.dex_path = dexPathArrayList.toArray(new String[0]);
			perloadData.loaded_libs = loadedNativeLibs.toArray(new String[0]);
			mBundle.putString(PreloadingInfo.NMOD_DATA_TAG, gson.toJson(perloadData));
		}
		else
			mBundle.putString(PreloadingInfo.NMOD_DATA_TAG, gson.toJson(new Preloader.NModPreloadData()));
		mHandler.sendEmptyMessage(PreloadingInfo.MSG_FINISH);
	}

	private boolean loadNModElfFiles(NMod nmod, NMod.NModPerloadBean perloadDataItem, Handler handler)
	{
		if (handler == null)
			handler = new Handler();
		MinecraftInfo minecraftInfo = mPESdk.getMinecraftInfo();

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
					message3.what = PreloadingInfo.MSG_PRELOADING_NATIVE_LIBS_FAILED;
					message3.obj = nmod;
					handler.sendMessage(message3);
					return false;
				}
			}

			for (String nameItem:perloadDataItem.native_libs)
			{
				NModLib lib = new NModLib(nameItem);
				lib.callOnLoad(minecraftInfo.getMinecraftVersionName(), mPESdk.getNModAPI().getVersionName());
			}
		}
		return true;
	}

	public class NModPreloadData
	{
		public String[] assets_packs_path;
		public String[] dex_path;
		public String[] loaded_libs;
	}
}
