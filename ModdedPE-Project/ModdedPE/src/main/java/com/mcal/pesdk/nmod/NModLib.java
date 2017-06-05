package com.mcal.pesdk.nmod;
import android.os.*;
import dalvik.system.*;
import java.io.*;
import java.util.*;

class NModLib
{
	private static LoadFailedException loadLanguages(NMod nmod)
	{

		if (nmod.getLanguageBeans() == null)
			return null;

		for (NMod.NModLanguageBean languageBean:nmod.getLanguageBeans())
		{
			if (languageBean.name == null || languageBean.name.isEmpty())
				continue;
			try
			{
				InputStream locationIns=nmod.getAssets().open(languageBean.path);
				byte[] buffer=new byte[locationIns.available()];
				locationIns.read(buffer);
				locationIns.close();
				String allChars=new String(buffer);
				String[] translations=allChars.split("\n");
				if (translations != null && translations.length > 0)
				{
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
							{
								nativeAppendTranslation(languageBean.name, translation);
							}
						}
					}
				}
			}
			catch (IOException e)
			{}
		}
		return null;
	}

	public static void writeLanguageData(NModFilePathManager mgr, ArrayList<NMod> nmods)
	{

	}

	public static void callOnActivityCreate(String nativeLibName, com.mojang.minecraftpe.MainActivity mainActivity, Bundle bundle)
	{
		nativeCallOnActivityCreate(nativeLibName, mainActivity, bundle);
	}

	public static void callOnActivityFinish(String nativeLibMame, com.mojang.minecraftpe.MainActivity mainActivity)
	{
		nativeCallOnActivityFinish(nativeLibMame, mainActivity);
	}
	
	public static void callOnLoad(String name,String mcver, String moddedpever)
	{
		nativeCallOnLoad(name,mcver,moddedpever);
	}

	private static native void nativeCallOnDexLoaded(String name, DexClassLoader classLoader);
	private static native void nativeAppendTranslation(String name, String translation);
	private static native void nativeCallOnActivityFinish(String name, com.mojang.minecraftpe.MainActivity mainActivity);
	private static native void nativeCallOnLoad(String name, String mcver, String moddedpever);
	private static native void nativeCallOnActivityCreate(String mame, com.mojang.minecraftpe.MainActivity mainActivity, Bundle savedInstanceState);
}
