package com.mcal.ModdedPE.nmod;
import android.content.*;
import java.io.*;

public class NModFilePathManager
{
	private Context context;

	public NModFilePathManager(Context context)
	{
		this.context = context;
	}

	public String getNModsDir()
	{
		return context.getFilesDir().toString() + File.separator + NModAPI.FILEPATH_DIR_NAME_NMOD_PACKS;
	}

	public String getNModLibsDir()
	{
		return context.getFilesDir().toString() + File.separator + NModAPI.FILEPATH_DIR_NAME_NMOD_LIBS;
	}

	public String getNModCacheDir()
	{
		return context.getCacheDir().getPath();
	}

	public String getNModCachePath()
	{
		return context.getCacheDir().getPath() + File.separator + NModAPI.FILEPATH_FILE_NAME_NMOD_CAHCHE;
	}
}
