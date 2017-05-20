package com.mcal.ModdedPE.app;
import android.content.*;
import android.content.res.*;
import android.os.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nativeapi.*;
import com.mcal.ModdedPE.utils.*;

public class ModdedPESafeModeMinecraftActivity extends com.mojang.minecraftpe.MainActivity
{
	protected void loadNativeLibraries()
	{
		LibraryLoader.loadGameLibs(this,MinecraftInfo.getInstance(this).getMinecraftNativeLibraryDir(), true);
	}

	protected void initAssetOverrides()
	{
		AssetOverrideManager.getInstance().addAssetOverride(MinecraftInfo.getInstance(this).getMinecraftPackageContext().getPackageResourcePath());
	}

	@Override
	public void onCreate(Bundle p1)
	{
		loadNativeLibraries();
		initAssetOverrides();
		super.onCreate(p1);
		GameLauncher.launch();
	}

	@Override
	public AssetManager getAssets()
	{
		return MinecraftInfo.getInstance(this).getAssets();
	}
}
