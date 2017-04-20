package com.mcal.ModdedPE.app;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.MCDesign.app.*;

public class ModdedPEAboutActivity extends MCDActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_about);
		
		setActionBarButtonCloseRight();
	}
}
