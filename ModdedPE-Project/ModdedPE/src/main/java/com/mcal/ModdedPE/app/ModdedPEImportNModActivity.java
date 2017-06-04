package com.mcal.ModdedPE.app;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import java.io.*;
import java.net.*;

public class ModdedPEImportNModActivity extends ModdedPEActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		File targetFile = getTargetNModFile();
		setTitle(targetFile.getAbsolutePath());
	}
	
	private File getTargetNModFile()
	{
		try
		{
			Intent intent = getIntent();
			Uri uri = intent.getData();
			return new File(new URI(uri.toString()));
		}
		catch (URISyntaxException e)
		{}
		return null;
	}
}
