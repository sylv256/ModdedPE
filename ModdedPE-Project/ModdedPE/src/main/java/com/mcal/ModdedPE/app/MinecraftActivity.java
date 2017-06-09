package com.mcal.ModdedPE.app;
import android.content.res.*;
import android.os.*;
import com.mcal.ModdedPE.*;
import com.mcal.pesdk.*;
import com.mojang.minecraftpe.*;

public class MinecraftActivity extends com.mojang.minecraftpe.MainActivity
{
	@Override
	public void onCreate(Bundle p1)
	{
		getPESdk().getGameManager().onMinecraftActivityCreate(this);
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
