package org.mcal.pesdk.nmod;
import android.content.*;
import java.io.*;

class NModFilePathManager
{
	private Context mContext;

	private static final String FILEPATH_DIR_NAME_NMOD_PACKS = "nmod_packs";
	private static final String FILEPATH_DIR_NAME_NMOD_LIBS = "nmod_libs";
	private static final String FILEPATH_DIR_NAME_NMOD_ICON = "nmod_icon";
	private static final String FILEPATH_FILE_NAME_NMOD_CAHCHE = "nmod_cached";
	private static final String FILEPATH_DIR_NAME_NMOD_JSON_PACKS = "nmod_json_packs";
	private static final String FILEPATH_DIR_NAME_NMOD_TEXT_PACKS = "nmod_text_packs";

	NModFilePathManager(Context context)
	{
		this.mContext = context;
	}

	File getNModsDir()
	{
		return new File(mContext.getFilesDir().getAbsolutePath() + File.separator + FILEPATH_DIR_NAME_NMOD_PACKS);
	}

	File getNModJsonDir()
	{
		return new File(mContext.getFilesDir().getAbsolutePath() + File.separator + FILEPATH_DIR_NAME_NMOD_JSON_PACKS);
	}

	File getNModJsonPath(NMod nmod)
	{
		return new File(getNModJsonDir(),nmod.getPackageName());
	}

	File getNModTextDir()
	{
		return new File(mContext.getFilesDir().getAbsolutePath() + File.separator + FILEPATH_DIR_NAME_NMOD_TEXT_PACKS);
	}

	File getNModTextPath(NMod nmod)
	{
		return new File(getNModTextDir(),nmod.getPackageName());
	}

	File getNModLibsDir()
	{
		return new File(mContext.getFilesDir().getAbsolutePath() + File.separator + FILEPATH_DIR_NAME_NMOD_LIBS);
	}

	File getNModCacheDir()
	{
		return new File(mContext.getCacheDir().getAbsolutePath());
	}

	File getNModCachePath()
	{
		return new File(mContext.getCacheDir().getAbsolutePath() + File.separator + FILEPATH_FILE_NAME_NMOD_CAHCHE);
	}
	
	File getNModIconDir()
	{
		return new File(mContext.getFilesDir().getAbsolutePath() + File.separator + FILEPATH_DIR_NAME_NMOD_ICON);
	}
	
	File getNModIconPath(NMod nmod)
	{
		return new File(getNModIconDir().getAbsolutePath() + File.separator + nmod.getPackageName());
	}
}