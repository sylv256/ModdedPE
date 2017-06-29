package org.mcal.moddedpe_new;

import android.app.Application;
import android.content.res.AssetManager;
import org.mcal.moddedpe_new.utils.UtilsSettings;
import org.mcal.pesdk.PESdk;

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
