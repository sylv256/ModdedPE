package org.mcal.moddedpe_new.app;

import android.app.Fragment;

import org.mcal.moddedpe_new.ModdedPEApplication;
import org.mcal.pesdk.PESdk;

public class BaseFragment extends Fragment
{
	protected PESdk getPESdk()
	{
		return ModdedPEApplication.mPESdk;
	}
}
