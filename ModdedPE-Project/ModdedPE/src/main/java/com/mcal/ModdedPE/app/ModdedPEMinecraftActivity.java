package com.mcal.ModdedPE.app;
import android.os.*;
import com.mcal.ModdedPE.nativeapi.*;
import com.mcal.pesdk.utils.*;

public class ModdedPEMinecraftActivity extends ModdedPEBaseMCActivity
{
	@Override
	public void onCreate(Bundle p1)
	{
		loadNativeLibraries(false);
		new OpenGameLoadingDialog(this).show();
		mergeGameAssets();
		NativeUtils.setValues(this);
		getNModAPI().loadToGame(getIntent().getExtras(), getAssets(), null);
		super.onCreate(p1);
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
