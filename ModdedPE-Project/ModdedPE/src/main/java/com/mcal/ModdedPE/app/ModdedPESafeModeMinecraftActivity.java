package com.mcal.ModdedPE.app;
import android.os.*;
import com.mcal.pesdk.utils.*;

public class ModdedPESafeModeMinecraftActivity extends ModdedPEBaseMCActivity
{
	@Override
	public void onCreate(Bundle p1)
	{
		getPESdk().getGameManager().onMinecraftActivityCreate(this);
		super.onCreate(p1);
	}
}
