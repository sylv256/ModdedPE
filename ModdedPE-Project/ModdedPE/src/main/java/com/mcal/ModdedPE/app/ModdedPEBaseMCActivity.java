package com.mcal.ModdedPE.app;
import android.content.res.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nativeapi.*;
import com.mcal.pesdk.nmod.*;
import com.mcal.pesdk.utils.*;

public class ModdedPEBaseMCActivity extends com.mojang.minecraftpe.MainActivity
{
	protected NModAPI getNModAPI()
	{
		return ModdedPEApplication.mNModAPI;
	}
	
	@Override
	public AssetManager getAssets()
	{
		return MinecraftInfo.getInstance(this).getAssets();
	}
	
	protected void loadNativeLibraries(boolean safeMode)
	{
		LibraryLoader.loadGameLibs(this, MinecraftInfo.getInstance(this).getMinecraftNativeLibraryDir(), safeMode);
	}
}
