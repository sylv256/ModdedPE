package com.mcal.ModdedPE;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.os.*;
import com.mcal.ModdedPE.app.*;
import com.mcal.ModdedPE.nativeapi.*;
import com.mcal.ModdedPE.nmodpe.*;
import java.io.*;
import android.text.format.*;
import com.mcal.ModdedPE.utils.*;

public class ModdedPEApplication extends Application
{
	public static String MC_PACKAGE_NAME = "com.mojang.minecraftpe";
	public static String MC_NATIVE_DIR = "/data/data/com.mojang.minecraftpe/lib";
	public static ModdedPEApplication instance;
	public static Context mcPkgContext;
	
	public static Context getMcPackageContext()
	{
		return mcPkgContext;
	}

	public void onCreate()
	{
		super.onCreate();
		instance = this;
		try
		{
			mcPkgContext=this.createPackageContext(MC_PACKAGE_NAME,Context.CONTEXT_IGNORE_SECURITY|Context.CONTEXT_INCLUDE_CODE);
		}
		catch(Exception e){}
		Thread.setDefaultUncaughtExceptionHandler(restartHandler);
		try
		{
			if(mcPkgContext!=null)
				AssetOverrideManager.getInstance().addAssetOverride(mcPkgContext.getPackageResourcePath());
		}
		catch(Throwable t)
		{
			
		}
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
		Intent intent=new Intent(this,ModdedPEErrorActivity.class);
		Bundle b=new Bundle();
		StackTraceElement[] strs=ex.getStackTrace();
		String msg=ex.toString();
		msg+="\n\n";
		for(StackTraceElement ste:strs)
		{
			msg+=(ste.toString()+"\n");
		}
		b.putString("msg",msg);
		
		Time time=new Time();
		time.setToNow();
		File file=new File(Environment.getExternalStorageDirectory(),"/ModdedPE-crash-"+time.toString()+".txt");
		try
		{
			file.createNewFile();
			FileOutputStream fos=new FileOutputStream(file);
			fos.write(msg.getBytes());
		}
		catch (IOException e)
		{}

		intent.putExtras(b);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		instance.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
		try
		{
			finalize();
		}
		catch (Throwable e)
		{}
	}
	
	public void startLauncher(Context c)
	{
		c.startActivity(new Intent(c,ModdedPEMainActivity.class));
	}
	
	@Override
	public AssetManager getAssets()
	{
		AssetManager mgr=AssetOverrideManager.getInstance().getLocalAssetManager();
		if(mgr!=null)
			return mgr;
		return super.getAssets();
	}
	
	static
	{
		LibraryLoader.loadLocalLibs();
	}
}
