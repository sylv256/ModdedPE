package org.mcal.pesdk;

import android.content.res.AssetManager;
import android.os.Bundle;

import com.google.gson.Gson;
import com.mojang.minecraftpe.MainActivity;

import org.mcal.pesdk.nativeapi.NativeUtils;
import org.mcal.pesdk.nmod.NModLib;
import org.mcal.pesdk.utils.AssetOverrideManager;

public class GameManager
{
	private PESdk mPESdk;

	GameManager(PESdk pesdk)
	{
		mPESdk = pesdk;
	}

	public boolean isSafeMode()
	{
		return mPESdk.getLauncherOptions().isSafeMode();
	}

	public AssetManager getAssets()
	{
		return mPESdk.getMinecraftInfo().getAssets();
	}

	public void onMinecraftActivityCreate(MainActivity activity, Bundle savedInstanceState)
	{
		boolean safeMode = mPESdk.getLauncherOptions().isSafeMode();
		AssetOverrideManager.addAssetOverride(activity.getAssets(), mPESdk.getMinecraftInfo().getMinecraftPackageContext().getPackageResourcePath());

		if (!safeMode)
		{
			NativeUtils.setValues(activity, mPESdk.getLauncherOptions());
			Gson gson = new Gson();
			Bundle data = activity.getIntent().getExtras();

			Preloader.NModPreloadData preloadData = gson.fromJson(data.getString(PreloadingInfo.NMOD_DATA_TAG), Preloader.NModPreloadData.class);

			for (int i=preloadData.assets_packs_path.length - 1;i >= 0;--i)
			{
				String assetsPath = preloadData.assets_packs_path[i];
				AssetOverrideManager.addAssetOverride(activity.getAssets(), assetsPath);
			}

			String[] loadedNModLibs = preloadData.loaded_libs;
			for (int i=loadedNModLibs.length - 1;i >= 0;--i)
			{
				String nativeLibName = loadedNModLibs[i];
				NModLib lib = new NModLib(nativeLibName);
				lib.callOnActivityCreate(activity, savedInstanceState);
			}
		}
	}

	public void onMinecraftActivityFinish(MainActivity activity)
	{
		if (mPESdk.getLauncherOptions().isSafeMode())
			return;
		Gson gson = new Gson();
		Preloader.NModPreloadData preloadData = gson.fromJson(activity.getIntent().getExtras().getString(PreloadingInfo.NMOD_DATA_TAG), Preloader.NModPreloadData.class);

		String[] loadedNModLibs = preloadData.loaded_libs;
		for (int i=loadedNModLibs.length - 1;i >= 0;--i)
		{
			String nativeLibName = loadedNModLibs[i];
			NModLib lib = new NModLib(nativeLibName);
			lib.callOnActivityFinish(activity);
		}
	}
}
