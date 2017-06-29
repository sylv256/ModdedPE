package org.mcal.moddedpe_new.app;

import android.os.Bundle;

import org.mcal.mcdesign.app.MCDActivity;
import org.mcal.moddedpe_new.ModdedPEApplication;
import org.mcal.moddedpe_new.utils.I18n;
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
