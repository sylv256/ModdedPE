package com.mcal.ModdedPE;

import android.app.*;
import android.content.res.*;
import com.mcal.ModdedPE.app.*;
import com.mcal.pesdk.nmod.*;
import com.mcal.pesdk.utils.*;
import java.io.*;
import com.mcal.ModdedPE.utils.*;
import com.mcal.pesdk.*;

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
