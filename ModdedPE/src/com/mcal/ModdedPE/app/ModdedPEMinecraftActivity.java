package com.mcal.ModdedPE.app;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.os.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nativeapi.*;
import com.mcal.ModdedPE.nmodpe.*;
import com.mcal.ModdedPE.utils.*;

public class ModdedPEMinecraftActivity extends com.mojang.minecraftpe.MainActivity
{
	private Context mcPackageContext=null;
	private String mcLibDir=ModdedPEApplication.MC_NATIVE_DIR;
	
	private void initFields()
	{
		mcPackageContext = getMcContext();
		mcLibDir = getNativeLibDirectory();
	}

	private Context getMcContext()
	{
		if(mcPackageContext!=null)
			return mcPackageContext;
		try
		{
			return mcPackageContext=createPackageContext(ModdedPEApplication.MC_PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	protected void loadNativeLibraries()
	{
		checkNullMcContext();
		LibraryLoader.loadGameLibs(mcLibDir,false);
	}
	
	protected void initAssetOverrides()
	{
		AssetOverrideManager.instance.init();
		AssetOverrideManager.instance.addAssetOverride(mcPackageContext.getAssets(),mcPackageContext.getPackageResourcePath());
	}

	private String getNativeLibDirectory()
	{
		if (checkNullMcContext())
			return mcPackageContext.getApplicationInfo().nativeLibraryDir;
		else
			return ModdedPEApplication.MC_NATIVE_DIR;
	}

	private boolean checkNullMcContext()
	{
		if (mcPackageContext == null)
			mcPackageContext = getMcContext();
		return mcPackageContext != null;
	}

	@Override
	public void onCreate(Bundle p1)
	{
		initFields();
		loadNativeLibraries();
		new LoadingDialog(this).show();
		initAssetOverrides();
		loadNModPEs();
		setNativeUtilsAttributes();
		super.onCreate(p1);
	}

	private void setNativeUtilsAttributes()
	{
		Settings settings=new Settings(this);
		
		Utils.nativeSetDataDirectory("/data/data/"+getPackageName()+"/");
		Utils.nativeSetRedstoneDot(settings.getRedstoneDot());
		Utils.nativeSetToggleDebugText(settings.getToggleDebugText());
	}

	private void loadNModPEs()
	{
		NModPEManager nmodpeManager=new NModPEManager(this);
		String mcVer="";
		String moddedpeVer=getString(R.string.app_version);
		try
		{
			mcVer=getMcContext().getPackageManager().getPackageInfo(getMcContext().getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		for (int i=nmodpeManager.getActiveNModPEs().size() - 1;i >= 0;--i)
		{
			NModPE nmodpe=nmodpeManager.getActiveNModPEs().get(i);
			
			try
			{
				nmodpe.getLoader().load(mcVer,moddedpeVer);
			}
			catch (Exception e)
			{
				LoadErrorDialog loadErrorDialog=new LoadErrorDialog(this,e,nmodpe);
				loadErrorDialog.show();
				continue;
			}
			AssetOverrideManager.instance.addAssetOverride(nmodpe.getAsset(),nmodpe.getPackageContext().getPackageResourcePath());
		}
		
		
		for (int i=nmodpeManager.getActiveNModPEs().size() - 1;i >= 0;--i)
		{
			NModPE nmodpe=nmodpeManager.getActiveNModPEs().get(i);
			nmodpe.getLoader().callOnActivityFinish(this);
		}
	}

	@Override
	public AssetManager getAssets()
	{
		AssetManager mgr=AssetOverrideManager.instance.getLocalAssetManager();
		if(mgr!=null)
			return mgr;
		return super.getAssets();
	}

	@Override
	protected void onDestroy()
	{
		NModPEManager nmodpeManager=new NModPEManager(this);
		for (int i=nmodpeManager.getActiveNModPEs().size() - 1;i >= 0;--i)
		{
			NModPE nmodpe=nmodpeManager.getActiveNModPEs().get(i);
			nmodpe.getLoader().callOnActivityFinish(this);
		}
		super.onDestroy();
	}
}
