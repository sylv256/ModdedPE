package com.mcal.pesdk;
import android.content.res.*;
import android.os.*;
import com.mojang.minecraftpe.*;
import com.mcal.pesdk.nativeapi.*;
import net.hockeyapp.android.tasks.*;

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
		if(mPESdk.getLauncherOptions().isSafeMode())
			return;
		mPESdk.getNModAPI().perloadNMods(extras, handler);
	}

	public void onMinecraftActivityCreate(MainActivity activity)
	{
		boolean safeMode = mPESdk.getLauncherOptions().isSafeMode();
		LibraryLoader.loadGameLibs(activity, mPESdk.getMinecraftInfo().getMinecraftNativeLibraryDir(), safeMode);
		if (!safeMode)
		{
			NativeUtils.setValues(activity);
			mPESdk.getNModAPI().loadToGame(activity.getIntent().getExtras(), getAssets(), null);
		}
	}

	public void onMinecraftActivityFinish(MainActivity activity)
	{
		if(mPESdk.getLauncherOptions().isSafeMode())
			return;
		mPESdk.getNModAPI().callOnActivityDestroy(activity, activity.getIntent().getExtras());
	}
}
