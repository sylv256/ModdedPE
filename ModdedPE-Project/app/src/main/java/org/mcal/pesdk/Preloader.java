package org.mcal.pesdk;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;

import org.mcal.pesdk.utils.MinecraftInfo;

import java.io.File;
import java.util.ArrayList;
import org.mcal.pesdk.nativeapi.LibraryLoader;
import org.mcal.pesdk.nmod.*;

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

	public void preload(Context context) throws PreloadException
	{
		mPreloadListener.onStart();

		if (mBundle == null)
			mBundle = new Bundle();
		Gson gson = new Gson();
		boolean safeMode = mPESdk.getLauncherOptions().isSafeMode();
		
		try
		{
			mPreloadListener.onLoadNativeLibs();
			mPreloadListener.onLoadSubstrateLib();
			LibraryLoader.loadSubstrate();
			mPreloadListener.onLoadFModLib();
			LibraryLoader.loadFMod(mPESdk.getMinecraftInfo().getMinecraftPackageNativeLibraryDir());
			mPreloadListener.onLoadMinecraftPELib();
			LibraryLoader.loadMinecraftPE(mPESdk.getMinecraftInfo().getMinecraftPackageNativeLibraryDir());
			mPreloadListener.onLoadGameLauncherLib();
			LibraryLoader.loadLauncher(mPESdk.getMinecraftInfo().getMinecraftPackageNativeLibraryDir());
			if (!safeMode)
			{
				mPreloadListener.onLoadNModAPILib();
				LibraryLoader.loadNModAPI(mPESdk.getMinecraftInfo().getMinecraftPackageNativeLibraryDir());
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
			ArrayList<String> assetsArrayList = new ArrayList<>();
			ArrayList<String> dexPathArrayList = new ArrayList<>();
			ArrayList<String> loadedNativeLibs = new ArrayList<>();

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
							loadedNativeLibs.add(nameItem.substring(nameItem.indexOf(File.separator)));
						}
					}
					mPreloadListener.onNModLoaded(nmod);
				}
				else
				{
					mPreloadListener.onFailedLoadingNMod(nmod);
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
					return false;
				}
			}

			for (String nameItem:preloadDataItem.native_libs)
			{
				NModLib lib = new NModLib(nameItem.substring(nameItem.indexOf(File.separator)));
				lib.callOnLoad(minecraftInfo.getMinecraftVersionName(), mPESdk.getNModAPI().getVersionName());
			}
		}
		return true;
	}

	static class NModPreloadData
	{
		String[] assets_packs_path;
		String[] dex_path;
		String[] loaded_libs;
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
