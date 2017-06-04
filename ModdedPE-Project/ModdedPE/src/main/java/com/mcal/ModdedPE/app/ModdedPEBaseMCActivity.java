package com.mcal.ModdedPE.app;
import android.content.res.*;
import com.mcal.ModdedPE.*;
import com.mcal.pesdk.nativeapi.*;
import com.mcal.pesdk.nmod.*;
import com.mcal.pesdk.utils.*;
import com.mcal.pesdk.*;

public class ModdedPEBaseMCActivity extends com.mojang.minecraftpe.MainActivity
{
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
