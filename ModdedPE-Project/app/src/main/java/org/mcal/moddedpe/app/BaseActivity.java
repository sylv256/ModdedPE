package org.mcal.moddedpe.app;

import android.os.Bundle;

import org.mcal.mcdesign.app.MCDActivity;
import org.mcal.moddedpe.ModdedPEApplication;
import org.mcal.moddedpe.utils.I18n;
import org.mcal.pesdk.PESdk;

public class BaseActivity extends MCDActivity
{
	protected PESdk getPESdk()
	{
		return ModdedPEApplication.mPESdk;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		I18n.setLanguage(this);
	}
}
