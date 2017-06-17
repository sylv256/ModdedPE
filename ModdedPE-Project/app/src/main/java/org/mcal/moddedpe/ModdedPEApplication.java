package org.mcal.moddedpe;

import android.app.Application;
import android.content.res.AssetManager;
import org.mcal.moddedpe.utils.UtilsSettings;
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
