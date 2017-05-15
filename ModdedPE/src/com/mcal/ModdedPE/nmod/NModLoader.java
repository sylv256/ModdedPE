package com.mcal.ModdedPE.nmod;
import android.os.*;
import java.io.*;
public class NModLoader
{
	private NMod nmod;

	public NModLoader(NMod nmod)
	{
		this.nmod = nmod;
	}

	public void load(String mcver, String moddedpever) throws Exception
	{
		loadLibs(mcver, moddedpever);
	}

	private void loadLibs(String mcver, String moddedpever) throws Exception
	{
		if (nmod == null || nmod.getNativeLibs() == null)
			return;
		for (String lib : nmod.getNativeLibs())
			tryToLoadLib(lib, mcver, moddedpever);
	}

	private void tryToLoadLib(String name, String mcver, String moddedpever)throws Exception
	{
		try
		{
			System.load(nmod.getNativeLibsPath() + File.separator + name);
		}
		catch (Throwable e)
		{
			throw new Exception(e);
		}
		nativeCallOnLoad(name, mcver, moddedpever);

		loadLanguages();
	}

	private void loadLanguages()
	{
		if (nmod.getLanguageBeans() == null)
			return;
		for (NMod.NModLanguageBean languageBean:nmod.getLanguageBeans())
		{
			if (languageBean.name == null || languageBean.name.isEmpty())
				continue;
			try
			{
				InputStream locationIns=nmod.getAssets().open(languageBean.location);
				byte[] buffer=new byte[locationIns.available()];
				locationIns.read(buffer);
				locationIns.close();
				String allChars=new String(buffer);
				String[] translations=allChars.split("\n");
				for (String translation:translations)
				{
					if (translation != null && !translation.isEmpty())
					{
						if (languageBean.format_space)
						{
							String tmp=translation;
							if (tmp.indexOf("=") != -1 && tmp.indexOf(" ") != -1)
							{
								String str_left=tmp.substring(0, tmp.indexOf("="));
								String str_right=tmp.substring(tmp.indexOf("="), tmp.length());
								str_left = str_left.replaceAll(" ", "");
								translation = str_left + str_right;
							}
						}
						if (translation != null && !translation.isEmpty())
							nativeAppendTranslation(languageBean.name, translation);
					}
				}

			}
			catch (Exception e)
			{}
		}
	}

	public void callOnActivityCreate(com.mojang.minecraftpe.MainActivity mainActivity, Bundle bundle)
	{
		for (String lib : nmod.getNativeLibs())
			nativeCallOnActivityCreate(lib, mainActivity, bundle);
	}

	public void callOnActivityFinish(com.mojang.minecraftpe.MainActivity mainActivity)
	{
		for (String lib : nmod.getNativeLibs())
			nativeCallOnActivityFinish(lib, mainActivity);
	}

	private static native void nativeAppendTranslation(String name, String translation);
	private static native void nativeCallOnActivityFinish(String name, com.mojang.minecraftpe.MainActivity mainActivity);
	private static native void nativeCallOnLoad(String name, String mcver, String moddedpever);
	private static native void nativeCallOnActivityCreate(String mame, com.mojang.minecraftpe.MainActivity mainActivity, Bundle savedInstanceState);
}
