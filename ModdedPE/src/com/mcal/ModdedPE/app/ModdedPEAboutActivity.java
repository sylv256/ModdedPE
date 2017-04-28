package com.mcal.ModdedPE.app;

import android.os.*;
import com.mcal.MCDesign.app.*;
import com.mcal.ModdedPE.*;

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
