package com.mcal.ModdedPE.app;
import android.os.*;
import com.mcal.ModdedPE.nativeapi.*;
import com.mcal.pesdk.utils.*;

public class ModdedPESafeModeMinecraftActivity extends ModdedPEBaseMCActivity
{
	protected void initAssetOverrides()
	{
		AssetOverrideManager.getInstance().addAssetOverride(MinecraftInfo.getInstance(this).getMinecraftPackageContext().getPackageResourcePath());
	}

	@Override
	public void onCreate(Bundle p1)
	{
		loadNativeLibraries(true);
		initAssetOverrides();
		super.onCreate(p1);
	}

	
}
