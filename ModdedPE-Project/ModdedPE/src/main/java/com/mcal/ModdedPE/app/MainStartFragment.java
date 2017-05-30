package com.mcal.ModdedPE.app;
import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import com.mcal.ModdedPE.widget.*;
import android.support.v7.widget.*;
import com.mcal.ModdedPE.nmod.*;
import com.mcal.ModdedPE.*;
import android.widget.*;
import com.mcal.ModdedPE.utils.*;
import com.mcal.MCDesign.widget.*;

public class MainStartFragment extends ModdedPEFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View main_view = inflater.inflate(R.layout.moddedpe_main, null);
		return main_view;
	}
}
