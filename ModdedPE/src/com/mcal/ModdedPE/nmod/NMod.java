package com.mcal.ModdedPE.nmod;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import com.google.gson.*;
import com.mcal.ModdedPE.*;
import java.io.*;
import java.util.*;

public abstract class NMod
{
	protected Context thisContext;
	
	public static final String TAG_MANIFEST_NAME = "nmod_manifest.json";
	
	public abstract boolean isBugPack();
	public abstract String getNewsTitle();
	public abstract String getName();
	public abstract Bitmap getVersionImage();
	public abstract Bitmap getIcon();
	public abstract boolean isValidNews();
	public abstract String getPackageName();
	public abstract NModLoader getLoader();
	public abstract NModLoadException getLoadException();
	public abstract String getDescription();
	public abstract String getAuthor();
	public abstract String getVersionName();
	public abstract String[] getNativeLibs();
	public abstract NModLanguageBean[] getLanguageBeans();
	public abstract AssetManager getAssets();
	public abstract void setBugPack(NModLoadException loadException);
	public abstract String getPackageResourcePath();
	public abstract String getNativeLibsPath();
	
	protected NMod(Context thisCon)
	{
		thisContext=thisCon;
	}
		
	public static class NModLanguageBean
	{
		public String name = null;
		public String location = null;
		public boolean format_space = false;
	}

	public static class NModVersionBean
	{
		public String version_name = null;
		public int version_code = -1;
		public String version_description_short = null;
		public String version_description = null;
		public String version_description_image = null;
	}

	public static class NModDataBean
	{
		public String[] native_libs = null;
		public String name = null;
		public String description = null;
		public String author = null;
		public NModLanguageBean[] languages = null;
		public NModVersionBean version_info = null;
		public boolean check_json_syntax = false;
	}
}
