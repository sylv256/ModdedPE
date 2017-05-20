package com.mcal.ModdedPE.utils;
import android.content.*;
import java.io.*;

public class FilePathManager
{
	private Context context;
	
	public static final String DIR_NAME_NMOD_PACKS = "nmod_packs";
	public static final String DIR_NAME_NMOD_LIBS = "nmod_libs";
	
	public FilePathManager(Context context)
	{
		this.context = context;
	}
	
	public String getNModsPath()
	{
		return context.getFilesDir().toString() + File.separator + DIR_NAME_NMOD_PACKS;
	}
	
	public String getNModLibsPath()
	{
		return context.getFilesDir().toString() + File.separator + DIR_NAME_NMOD_LIBS;
	}
}
