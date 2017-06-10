package com.mcal.pesdk;
import android.content.res.*;
import android.os.*;
import com.google.gson.*;
import com.mcal.pesdk.*;
import com.mcal.pesdk.nativeapi.*;
import com.mcal.pesdk.nmod.*;
import com.mojang.minecraftpe.*;
import com.mcal.pesdk.utils.*;
import android.content.*;

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

	public void perloadForLaunch(Bundle extras, Handler handler)
	{
		new Preloader(extras,handler,mPESdk).start();
	}

	public void onMinecraftActivityCreate(MainActivity activity,Bundle savedInstanceState)
	{
		boolean safeMode = mPESdk.getLauncherOptions().isSafeMode();
		AssetOverrideManager.addAssetOverride(activity.getAssets(), mPESdk.getMinecraftInfo().getMinecraftPackageContext().getPackageResourcePath());
		
		if (!safeMode)
		{
			NativeUtils.setValues(activity);
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
		if(mPESdk.getLauncherOptions().isSafeMode())
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
