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
	private PESdk mPESdk;
	private PreloadListener mPreloadListener;

	public Preloader(PESdk pesdk, Bundle bundle, PreloadListener listener)
	{
		mBundle = bundle;
		mPreloadListener = listener;
		mPESdk = pesdk;
		if (mPreloadListener == null)
			mPreloadListener = new PreloadListener();
	}

	public Preloader(PESdk pesdk, Bundle bundle)
	{
		this(pesdk, bundle, null);
	}

	public void preload()throws PreloadException
	{
		mPreloadListener.onStart();

		if (mBundle == null)
			mBundle = new Bundle();
		Gson gson = new Gson();
		boolean safeMode = mPESdk.getLauncherOptions().isSafeMode();
		String abiInfo = ABIInfo.getTargetABIType();

		if (abiInfo == null)
			throw new PreloadException(PreloadException.TYPE_UNSUPPORTED_ABI);

		try
		{
			mPreloadListener.onLoadNativeLibs();
			mPreloadListener.onLoadSubstrateLib();
			LibraryLoader.loadSubstrate();
			mPreloadListener.onLoadGameLauncherLib();
			LibraryLoader.loadLauncher();
			mPreloadListener.onLoadFModLib();
			LibraryLoader.loadFMod(mPESdk.getMinecraftInfo().getMinecraftPackageNativeLibraryDir());
			mPreloadListener.onLoadMinecraftPELib();
			LibraryLoader.loadMinecraftPE(mPESdk.getMinecraftInfo().getMinecraftPackageNativeLibraryDir());
			if (!safeMode)
			{
				mPreloadListener.onLoadNModAPILib();
				LibraryLoader.loadNModAPI();
			}
			mPreloadListener.onFinishedLoadingNativeLibs();
		}
		catch (Throwable throwable)
		{
			throw new PreloadException(PreloadException.TYPE_LOAD_LIBS_FAILED, throwable);
		}

		if (!safeMode)
		{
			mPreloadListener.onStartLoadingAllNMods();

			NModPreloadData preloadData = new NModPreloadData();
			ArrayList<String> assetsArrayList = new ArrayList<String>();
			ArrayList<String> dexPathArrayList = new ArrayList<String>();
			ArrayList<String> loadedNativeLibs = new ArrayList<String>();

			ArrayList<NMod> loadedEnabledNMods = mPESdk.getNModAPI().getImportedEnabledNMods();
			for (NMod nmod:loadedEnabledNMods)
			{
				if (nmod.isBugPack())
				{
					mPreloadListener.onFailedLoadingNMod(nmod);
					continue;
				}

				NMod.NModPreloadBean preloadDataItem = nmod.copyNModFiles();
				if (loadNModElfFiles(nmod, preloadDataItem))
				{
					if (preloadDataItem.assets_path != null)
						assetsArrayList.add(preloadDataItem.assets_path);
					if (preloadDataItem.dex_path != null)
						dexPathArrayList.add(preloadDataItem.dex_path);

					if (preloadDataItem.native_libs != null && preloadDataItem.native_libs.length > 0)
					{
						for (String nameItem:preloadDataItem.native_libs)
						{
							loadedNativeLibs.add(nameItem);
						}
					}
					mPreloadListener.onNModLoaded(nmod);
				}
				else
				{
					mPreloadListener.onFailedLoadingNMod(nmod);
					continue;
				}
			}
			preloadData.assets_packs_path = assetsArrayList.toArray(new String[0]);
			preloadData.dex_path = dexPathArrayList.toArray(new String[0]);
			preloadData.loaded_libs = loadedNativeLibs.toArray(new String[0]);
			mBundle.putString(PreloadingInfo.NMOD_DATA_TAG, gson.toJson(preloadData));
			mPreloadListener.onFinishedLoadingAllNMods();
		}
		else
			mBundle.putString(PreloadingInfo.NMOD_DATA_TAG, gson.toJson(new Preloader.NModPreloadData()));
		mPreloadListener.onFinish(mBundle);
	}

	private boolean loadNModElfFiles(NMod nmod, NMod.NModPreloadBean preloadDataItem)
	{
		MinecraftInfo minecraftInfo = mPESdk.getMinecraftInfo();

		if (preloadDataItem.native_libs != null && preloadDataItem.native_libs.length > 0)
		{
			for (String nameItem:preloadDataItem.native_libs)
			{
				try
				{
					System.load(nameItem);
				}
				catch (Throwable t)
				{
					nmod.setBugPack(new LoadFailedException(LoadFailedException.TYPE_LOAD_LIB_FAILED, t));
					mPreloadListener.onFailedLoadingNMod(nmod);
					return false;
				}
			}

			for (String nameItem:preloadDataItem.native_libs)
			{
				NModLib lib = new NModLib(nameItem);
				lib.callOnLoad(minecraftInfo.getMinecraftVersionName(), mPESdk.getNModAPI().getVersionName());
			}
		}
		return true;
	}

	public static class NModPreloadData
	{
		public String[] assets_packs_path;
		public String[] dex_path;
		public String[] loaded_libs;
	}

	public static class PreloadListener
	{
		public void onStart()
		{}
		public void onLoadNativeLibs()
		{}
		public void onLoadSubstrateLib()
		{}
		public void onLoadGameLauncherLib()
		{}
		public void onLoadFModLib()
		{}
		public void onLoadMinecraftPELib()
		{}
		public void onLoadNModAPILib()
		{}
		public void onFinishedLoadingNativeLibs()
		{}

		public void onStartLoadingAllNMods()
		{}
		public void onNModLoaded(NMod nmod)
		{}
		public void onFailedLoadingNMod(NMod nmod)
		{}
		public void onFinishedLoadingAllNMods()
		{}

		public void onFinish(Bundle bundle)
		{}
	}
}
