package com.mcal.ModdedPE;

import android.app.*;
import android.content.res.*;
import com.mcal.ModdedPE.app.*;
import com.mcal.ModdedPE.nmod.*;
import com.mcal.ModdedPE.utils.*;
import java.io.*;

public class ModdedPEApplication extends Application
{
	public static ModdedPEApplication mInstance;

	public void onCreate()
	{
		super.onCreate();
		mInstance = this;
		Thread.setDefaultUncaughtExceptionHandler(restartHandler);
		
		MinecraftInfo.initInstance(this);
		NModAPI.createInstance(this);
	}

	private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler()
	{
		public void uncaughtException(Thread thread, Throwable ex)
		{
			restartAppAndReport(ex);
		}
	};

	public void restartAppAndReport(Throwable ex)
	{
		ByteArrayOutputStream ous = new ByteArrayOutputStream();
		ex.printStackTrace(new PrintStream(ous));
		String msg = new String(ous.toByteArray());
		try
		{
			ous.close();
		}
		catch (IOException e)
		{}
		ModdedPEErrorActivity.startThisActivity(this, msg);
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
		try
		{
			finalize();
		}
		catch (Throwable e)
		{}
	}

	@Override
	public AssetManager getAssets()
	{
		return MinecraftInfo.getInstance(this).getAssets();
	}
}
