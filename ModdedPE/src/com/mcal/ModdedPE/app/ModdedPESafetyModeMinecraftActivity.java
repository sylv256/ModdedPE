package com.mcal.ModdedPE.app;
import android.content.*;
import android.content.res.*;
import android.os.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nativeapi.*;
import com.mcal.ModdedPE.utils.*;
import android.app.*;

public class ModdedPESafetyModeMinecraftActivity extends com.mojang.minecraftpe.MainActivity
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
		LibraryLoader.loadGameLibs(mcLibDir,true);
	}

	protected void initAssetOverrides()
	{
		AssetOverrideManager.getInstance().addAssetOverride(mcPackageContext.getPackageResourcePath());
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
		initAssetOverrides();
		super.onCreate(p1);
	}

	@Override
	public AssetManager getAssets()
	{
		AssetManager mgr=AssetOverrideManager.getInstance().getLocalAssetManager();
		if(mgr!=null)
			return mgr;
		return super.getAssets();
	}
}
