package com.mcal.ModdedPEpackage org.mcal.moddedpeimport com.mcal.MCDesign.app.*;
import com.mcal.ModdedPE.*;
import com.mcal.pesdk.nmod.*;
import com.mcal.pesdk.*;

public class BaseActivity extends MCDActivity
{
	protected PESdk getPESdk()
	{
		return ModdedPEApplication.mPESdk;
	}
}
