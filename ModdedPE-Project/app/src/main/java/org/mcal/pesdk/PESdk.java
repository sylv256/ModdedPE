package org.mcal.pesdk;

import android.content.Context;
import org.mcal.pesdk.nmod.NModAPI;
import org.mcal.pesdk.utils.LauncherOptions;
import org.mcal.pesdk.utils.MinecraftInfo;

public class PESdk
{
	private MinecraftInfo mMinecraftInfo;
	private NModAPI mNModAPI;
	private LauncherOptions mLauncherOptions;
	private GameManager mGameManager;
	private boolean mIsInited;

	public PESdk(Context context, LauncherOptions options)
	{
		mMinecraftInfo = new MinecraftInfo(context, options);
		mNModAPI = new NModAPI(context, options);
		mLauncherOptions = options;
		mGameManager = new GameManager(this);
		mIsInited = false;
	}

	public void init()
	{
		mNModAPI.initNModDatas();
		mIsInited = true;
	}

	public boolean isInited()
	{
		return mIsInited;
	}

	public NModAPI getNModAPI()
	{
		return mNModAPI;
	}

	public MinecraftInfo getMinecraftInfo()
	{
		return mMinecraftInfo;
	}

	public LauncherOptions getLauncherOptions()
	{
		return mLauncherOptions;
	}

	public GameManager getGameManager()
	{
		return mGameManager;
	}
}
