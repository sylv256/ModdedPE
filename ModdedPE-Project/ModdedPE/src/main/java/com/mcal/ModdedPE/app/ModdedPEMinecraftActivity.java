package com.mcal.ModdedPE.app;
import android.os.*;
import com.mcal.pesdk.nativeapi.*;
import com.mcal.pesdk.utils.*;

public class ModdedPEMinecraftActivity extends ModdedPEBaseMCActivity
{
	@Override
	public void onCreate(Bundle p1)
	{
		getPESdk().getGameManager().onMinecraftActivityCreate(this);
		new OpenGameLoadingDialog(this).show();
		super.onCreate(p1);
	}

	@Override
	protected void onDestroy()
	{
		getPESdk().getGameManager().onMinecraftActivityFinish(this);
		super.onDestroy();
	}
}
