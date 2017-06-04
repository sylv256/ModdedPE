package com.mcal.pesdk;
import com.mcal.pesdk.utils.*;
import android.content.*;
import com.mcal.pesdk.nmod.*;
import com.mcal.pesdk.nativeapi.*;

public class PESdk
{
	private MinecraftInfo mMinecraftInfo;
	private NModAPI mNModAPI;
	private Context mContext;
	private LauncherOptions mLauncherOptions;
	private GameManager mGameManager;
	
	public PESdk(Context context,LauncherOptions options)
	{
		mContext = context;
		
		mMinecraftInfo = new MinecraftInfo(mContext);
		mNModAPI = new NModAPI(mContext,options);
		mLauncherOptions = options;
		mGameManager = new GameManager(this);
	}
	
	public void init()
	{
		LibraryLoader.loadLocalLibs();
		mNModAPI.initNModDatas();
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
