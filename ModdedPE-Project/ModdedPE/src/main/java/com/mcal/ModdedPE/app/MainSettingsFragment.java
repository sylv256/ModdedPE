package com.mcal.ModdedPE.app;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mcal.MCDesign.widget.*;
import com.mcal.ModdedPE.*;

public class MainSettingsFragment extends ModdedPEFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View options_view=inflater.inflate(R.layout.moddedpe_options, null);

		return options_view;
	}
}
