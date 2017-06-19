package org.mcal.moddedpe.app;
import org.mcal.mcdesign.app.*;
import org.mcal.moddedpe.*;
import org.mcal.pesdk.nmod.*;
import org.mcal.pesdk.*;
import android.os.*;
import org.mcal.moddedpe.utils.*;

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
