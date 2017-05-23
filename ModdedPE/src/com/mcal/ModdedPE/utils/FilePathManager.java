package com.mcal.ModdedPE.utils;
import android.content.*;
import java.io.*;

public class FilePathManager
{
	private Context context;
	
	public static final String DIR_NAME_NMOD_PACKS = "nmod_packs";
	public static final String DIR_NAME_NMOD_LIBS = "nmod_libs";
	public static final String FILE_NAME_NMOD_ICON = "nmod_icon";
	
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
	
	public String getNModIconFilePath()
	{
		return context.getFilesDir().toString() + FILE_NAME_NMOD_ICON;
	}
}
