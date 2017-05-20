package com.mcal.ModdedPE;

import android.app.*;
import android.content.*;
import android.content.res.*;
import com.mcal.ModdedPE.app.*;
import com.mcal.ModdedPE.nativeapi.*;
import com.mcal.ModdedPE.nmod.*;
import com.mcal.ModdedPE.utils.*;

public class ModdedPEApplication extends Application
{
	public static ModdedPEApplication instance;

	public void onCreate()
	{
		super.onCreate();
		instance = this;
		
		MinecraftInfo.initInstance(this);
		Thread.setDefaultUncaughtExceptionHandler(restartHandler);
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
		StackTraceElement[] strs=ex.getStackTrace();
		String msg=ex.toString() + "\n\n";
		for (StackTraceElement ste:strs)
			msg += (ste.toString() + "\n");
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

	static
	{
		LibraryLoader.loadLocalLibs();
	}
}
