package com.mcal.ModdedPE.nmodpe;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import com.google.gson.*;
import java.io.*;

public class NModPE
{
	private Context packageContext;
	private Context thisContext;
	private NModPEDataBean dataBean;
	private boolean isActive;
	private NModPELoader loader;
	private NModPELoadException bugExpection = null ;
	public static final String TAG_MANIFEST_NAME = "nmodpe_manifest.json";
	
	public boolean isBugPack()
	{
		return bugExpection!=null;
	}
	
	public NModPELoadException getBugPackException()
	{
		try
		{
			InputStream is=packageContext.getAssets().open(TAG_MANIFEST_NAME);
			byte[] buffer=new byte[is.available()];
			is.read(buffer);
			String jsonStr=new String(buffer);
			Gson gson=new Gson();
			NModPEDataBean theDataBean=gson.fromJson(jsonStr,NModPEDataBean.class);
			dataBean=theDataBean;
			if(dataBean==null)
				return new NModPELoadException(NModPELoadException.BAD_MANIFEST_GRAMMAR,new String());
		}
		catch(Throwable t)
		{
			return new NModPELoadException(NModPELoadException.BAD_MANIFEST_GRAMMAR,t);
		}
		
		return null;
	}
	
	public NModPE(Context packageContext,Context contextThiz)
	{
		this.packageContext=packageContext;
		this.thisContext=contextThiz;
		this.bugExpection=null;
		
		try
		{
			InputStream is=packageContext.getAssets().open(TAG_MANIFEST_NAME);
			byte[] buffer=new byte[is.available()];
			is.read(buffer);
			String jsonStr=new String(buffer);
			Gson gson=new Gson();
			NModPEDataBean theDataBean=gson.fromJson(jsonStr,NModPEDataBean.class);
			dataBean=theDataBean;
		}
		catch (Exception e)
		{
			dataBean=null;
			bugExpection=getBugPackException();
			loader=new NModPELoader(this);
			return;
		}
		
		NModPEOptions options=new NModPEOptions(contextThiz);
		isActive=options.isActive(this);
		loader=new NModPELoader(this);
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void setActive(boolean isActive)
	{
		this.isActive=isActive;
	}
	
	public Context getPackageContext()
	{
		return packageContext;
	}
	
	public AssetManager getAsset()
	{
		return packageContext.getAssets();
	}
	
	public String getPackageName()
	{
		if(getPackageContext()==null)
			return toString();
		return getPackageContext().getPackageName();
	}
	
	public Bitmap getIcon()
	{
		try
		{
			PackageManager packageManager = getPackageContext().getPackageManager();
			PackageInfo packageInfo = null;
			packageInfo = packageManager.getPackageInfo(getPackageContext().getPackageName(), 0);
			int iconRes = packageInfo.applicationInfo.icon;
			return BitmapFactory.decodeResource(getPackageContext().getResources(),iconRes);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public String[] getNativeLibs()
	{
		return dataBean.native_libs;
	}
	
	public NModPELoader getLoader()
	{
		return loader;
	}
	
	public String getName()
	{
		if(isBugPack())
			return getPackageName();
		return dataBean.name;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(getClass()==obj.getClass())
			return getPackageName().equals(((NModPE)obj).getPackageName());
		return false;
	}
	
	public static class NModPELanguageBean
	{
		public String name;
		public String location;
		public boolean auto_split;
	}
	
	public static class NModPEVersionBean
	{
		public String version_name;
		public int version_code;
		public String version_title;
		public String version_image;
	}
	
	public NModPELanguageBean[] getLanguageBeans()
	{
		return dataBean.languages;
	}
	
	public static class NModPEDataBean
	{
		public String version_name;
		public String[] native_libs;
		public int version_code;
		public String name;
		public String description;
		public String description_image;
		public String author;
		public NModPELanguageBean[] languages;
		public NModPEVersionBean vesion_info;
	}
}
