package com.mcal.pesdk.nmod;
import android.content.*;
import java.io.*;

public class NModFilePathManager
{
	private Context mContext;

	public static final String FILEPATH_DIR_NAME_NMOD_PACKS = "nmod_packs";
	public static final String FILEPATH_DIR_NAME_NMOD_LIBS = "nmod_libs";
	public static final String FILEPATH_DIR_NAME_NMOD_ICON = "nmod_icon";
	public static final String FILEPATH_FILE_NAME_NMOD_CAHCHE = "nmod_cached";
	
	public NModFilePathManager(Context context)
	{
		this.mContext = context;
	}

	public File getNModsDir()
	{
		return new File(mContext.getFilesDir().getAbsolutePath() + File.separator + FILEPATH_DIR_NAME_NMOD_PACKS);
	}

	public File getNModLibsDir()
	{
		return new File(mContext.getFilesDir().getAbsolutePath() + File.separator + FILEPATH_DIR_NAME_NMOD_LIBS);
	}

	public File getNModCacheDir()
	{
		return new File(mContext.getCacheDir().getAbsolutePath());
	}

	public File getNModCachePath()
	{
		return new File(mContext.getCacheDir().getAbsolutePath() + File.separator + FILEPATH_FILE_NAME_NMOD_CAHCHE);
	}
	
	public File getNModIconDir()
	{
		return new File(mContext.getFilesDir().getAbsolutePath() + File.separator + FILEPATH_DIR_NAME_NMOD_ICON);
	}
	
	public File getNModIconPath(NMod nmod)
	{
		return new File(getNModIconDir().getAbsolutePath() + File.separator + nmod.getPackageName());
	}
}
