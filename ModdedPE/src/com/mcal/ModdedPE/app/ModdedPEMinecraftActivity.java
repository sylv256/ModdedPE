package com.mcal.ModdedPE.app;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.os.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nativeapi.*;
import com.mcal.ModdedPE.nmod.*;
import com.mcal.ModdedPE.utils.*;
import java.util.*;
import java.io.*;

public class ModdedPEMinecraftActivity extends ModdedPEBaseMCActivity
{
	@Override
	public void onCreate(Bundle p1)
	{
		new OpenGameLoadingDialog(this).show();
		loadNativeLibraries(false);
		mergeGameAssets();
		NativeUtils.setValues(this);
		getNModAPI().loadToGame(getIntent().getExtras(), getAssets(), null);
		super.onCreate(p1);
		GameLauncher.launch();
	}

	private void mergeGameAssets()
	{
		AssetOverrideManager.getInstance().addAssetOverride(MinecraftInfo.getInstance(this).getMinecraftPackageContext().getPackageResourcePath());
	}

	@Override
	protected void onDestroy()
	{
		getNModAPI().callOnActivityDestroy(this, getIntent().getExtras());
		super.onDestroy();
	}
}
