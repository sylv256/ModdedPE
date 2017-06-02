package com.mcal.ModdedPE.nmod;
import android.content.*;
import java.io.*;
import android.content.pm.*;
import java.util.*;

public class NModArchiver
{
	Context context;
	
	NModArchiver(Context context)
	{
		this.context = context;
	}
	
	PackagedNMod archiveFromInstalledPackage(String packageName)throws ArchiveFailedException
	{
		try
		{
			Context contextPackage = context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
			contextPackage.getAssets().open(NMod.MANIFEST_NAME).close();
			return new PackagedNMod(context,contextPackage);
		}
		catch (IOException e)
		{
			throw new ArchiveFailedException(ArchiveFailedException.TYPE_NO_MANIFEST);
		}
		catch(PackageManager.NameNotFoundException notFoundE)
		{
			throw new ArchiveFailedException(ArchiveFailedException.TYPE_PACKAGE_NOT_FOUND,notFoundE);
		}
	}
	
	ArrayList<NMod> archiveAllFromInstalled()
	{
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> infos = packageManager.getInstalledPackages(0);
		ArrayList<NMod> list = new ArrayList<NMod>();
		for (PackageInfo info:infos)
		{
			try
			{
				PackagedNMod packagedNMod = archiveFromInstalledPackage(info.packageName);
				list.add(packagedNMod);
				continue;
			}
			catch (ArchiveFailedException e)
			{}
		}
		return list;
	}
}
