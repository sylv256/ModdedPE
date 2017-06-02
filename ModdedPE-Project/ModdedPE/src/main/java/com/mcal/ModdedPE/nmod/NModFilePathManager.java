package com.mcal.ModdedPE.nmod;
import android.content.*;
import java.io.*;

public class NModFilePathManager
{
	private Context context;

	public static final String FILEPATH_DIR_NAME_NMOD_PACKS = "nmod_packs";
	public static final String FILEPATH_DIR_NAME_NMOD_LIBS = "nmod_libs";
	public static final String FILEPATH_DIR_NAME_NMOD_LANG_DATA = "nmod_lang";
	public static final String FILEPATH_FILE_NAME_NMOD_CAHCHE = "nmod_cached";
	
	public NModFilePathManager(Context context)
	{
		this.context = context;
	}

	public String getNModsDir()
	{
		return context.getFilesDir().toString() + File.separator + FILEPATH_DIR_NAME_NMOD_PACKS;
	}

	public String getNModLibsDir()
	{
		return context.getFilesDir().toString() + File.separator + FILEPATH_DIR_NAME_NMOD_LIBS;
	}

	public String getNModCacheDir()
	{
		return context.getCacheDir().getPath();
	}

	public String getNModCachePath()
	{
		return context.getCacheDir().getPath() + File.separator + FILEPATH_FILE_NAME_NMOD_CAHCHE;
	}
}
