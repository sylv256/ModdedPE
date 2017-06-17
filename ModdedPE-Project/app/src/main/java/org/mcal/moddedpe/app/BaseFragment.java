package org.mcal.moddedpe.app;
import android.support.v4.app.*;
import com.mcal.ModdedPE.*;
import com.mcal.pesdk.nmod.*;
import com.mcal.pesdk.*;

public class BaseFragment extends Fragment
{
	protected PESdk getPESdk()
	{
		return ModdedPEApplication.mPESdk;
	}
}
