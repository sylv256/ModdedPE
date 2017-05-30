package com.mcal.ModdedPE.utils;
import android.content.*;
import java.io.*;

public class FilePathManager
{
	private Context context;
	
	public static final String DIR_NAME_NMOD_PACKS = "nmod_packs";
	public static final String DIR_NAME_NMOD_LIBS = "nmod_libs";
	public static final String DIR_NAME_NMOD_LANG_DATA = "nmod_lang";
	public static final String FILE_NAME_NMOD_ICON = "nmod_icon";
	public static final String FILE_NAME_NMOD_CAHCHE = "cached_nmod";
	public FilePathManager(Context context)
	{
		this.context = context;
	}
	
	public String getNModsDir()
	{
		return context.getFilesDir().toString() + File.separator + DIR_NAME_NMOD_PACKS;
	}
	
	public String getNModLibsDir()
	{
		return context.getFilesDir().toString() + File.separator + DIR_NAME_NMOD_LIBS;
	}
	
	public String getNModIconFilePath()
	{
		return context.getFilesDir().toString() + File.separator + FILE_NAME_NMOD_ICON;
	}
	
	public String getNModCacheDir()
	{
		return context.getCacheDir().getPath();
	}
	
	public String getNModCachePath()
	{
		return context.getCacheDir().getPath() + File.separator + FILE_NAME_NMOD_CAHCHE;
	}
}
