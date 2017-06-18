package org.mcal.moddedpe.app;
import android.app.*;
import org.mcal.moddedpe.*;
import org.mcal.pesdk.nmod.*;
import org.mcal.pesdk.*;

public class BaseFragment extends Fragment
{
	protected PESdk getPESdk()
	{
		return ModdedPEApplication.mPESdk;
	}
}
