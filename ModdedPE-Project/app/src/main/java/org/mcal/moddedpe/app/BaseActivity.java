package org.mcal.moddedpe.app;
import org.mcal.mcdesign.app.*;
import org.mcal.moddedpe.*;
import org.mcal.pesdk.nmod.*;
import org.mcal.pesdk.*;

public class BaseActivity extends MCDActivity
{
	protected PESdk getPESdk()
	{
		return ModdedPEApplication.mPESdk;
	}
}
