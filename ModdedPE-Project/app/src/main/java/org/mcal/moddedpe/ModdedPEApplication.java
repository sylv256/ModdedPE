package org.mcal.moddedpe;

import android.app.*;
import android.content.res.*;
import org.mcal.pesdk.*;

public class ModdedPEApplication extends Application
{
	public static PESdk mPESdk;
	
	public void onCreate()
	{
		super.onCreate();
		mPESdk = new PESdk(this,new UtilsSettings(this));
	}

	@Override
	public AssetManager getAssets()
	{
		return mPESdk.getMinecraftInfo().getAssets();
	}
}
