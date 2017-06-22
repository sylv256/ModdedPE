package org.mcal.moddedpe.app;

import android.app.Fragment;

import org.mcal.moddedpe.ModdedPEApplication;
import org.mcal.pesdk.PESdk;

public class BaseFragment extends Fragment
{
	protected PESdk getPESdk()
	{
		return ModdedPEApplication.mPESdk;
	}
}
