package org.mcal.moddedpe.app;

import android.content.res.AssetManager;
import android.os.Bundle;

import org.mcal.moddedpe.ModdedPEApplication;
import org.mcal.pesdk.PESdk;

public class MinecraftActivity extends com.mojang.minecraftpe.MainActivity
{
	@Override
	public void onCreate(Bundle p1)
	{
		getPESdk().getGameManager().onMinecraftActivityCreate(this,p1);
		if(!getPESdk().getGameManager().isSafeMode())
			new OpenGameLoadingDialog(this).show();
		super.onCreate(p1);
	}

	@Override
	protected void onDestroy()
	{
		getPESdk().getGameManager().onMinecraftActivityFinish(this);
		super.onDestroy();
	}
	
	protected PESdk getPESdk()
	{
		return ModdedPEApplication.mPESdk;
	}

	@Override
	public AssetManager getAssets()
	{
		return getPESdk().getGameManager().getAssets();
	}
}
