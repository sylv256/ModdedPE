package com.mcal.ModdedPE.app;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import com.mcal.MCDesign.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.utils.*;

import android.support.v4.app.Fragment;

public class MainSettingsFragment extends Fragment
{
	private MCDSwitch options_switchSafetyMode;
	private MCDSwitch options_switchRedstoneDot;
	private MCDSwitch options_switchHideDebugText;
	private MCDSwitch options_switchAutoSaveLevel;
	private MCDSwitch options_switchSelectAllInLeft;
	private MCDSwitch options_switchDisableTextureIsotropic;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View options_view=inflater.inflate(R.layout.moddedpe_options, null);

		options_switchSafetyMode = (MCDSwitch)options_view.findViewById(R.id.moddedpeoptionsMCDSwitchSafetyMode);
		options_switchRedstoneDot = (MCDSwitch)options_view.findViewById(R.id.moddedpeoptionsMCDSwitchRedstoneDot);
		options_switchHideDebugText = (MCDSwitch)options_view.findViewById(R.id.moddedpeoptionsMCDSwitchToggleDebugText);
		options_switchAutoSaveLevel = (MCDSwitch)options_view.findViewById(R.id.moddedpeoptionsMCDSwitchAutoSaveLevel);
		options_switchSelectAllInLeft = (MCDSwitch)options_view.findViewById(R.id.moddedpeoptionsMCDSwitchSelectAllInLeft);
		options_switchDisableTextureIsotropic = (MCDSwitch)options_view.findViewById(R.id.moddedpeoptionsMCDSwitchDisableTextureIsotropic);

		loadOptions(getContext());
		
		CompoundButton.OnCheckedChangeListener options_switchUpdateListener=new CompoundButton.OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				refreshOptionsViews();
				saveOptions(p1.getContext());
			}

		};

		options_switchSafetyMode.setOnCheckedChangeListener(options_switchUpdateListener);
		options_switchRedstoneDot.setOnCheckedChangeListener(options_switchUpdateListener);
		options_switchHideDebugText.setOnCheckedChangeListener(options_switchUpdateListener);
		options_switchAutoSaveLevel.setOnCheckedChangeListener(options_switchUpdateListener);
		options_switchSelectAllInLeft.setOnCheckedChangeListener(options_switchUpdateListener);
		options_switchDisableTextureIsotropic.setOnCheckedChangeListener(options_switchUpdateListener);

		refreshOptionsViews();

		return options_view;
	}

	private void refreshOptionsViews()
	{
		if (options_switchSafetyMode.isChecked())
		{
			options_switchRedstoneDot.setChecked(false);
			options_switchRedstoneDot.setClickable(false);
			options_switchHideDebugText.setChecked(false);
			options_switchHideDebugText.setClickable(false);
			options_switchAutoSaveLevel.setChecked(false);
			options_switchAutoSaveLevel.setClickable(false);
			options_switchSelectAllInLeft.setChecked(false);
			options_switchSelectAllInLeft.setClickable(false);
			options_switchDisableTextureIsotropic.setChecked(false);
			options_switchDisableTextureIsotropic.setClickable(false);
		}
		else
		{
			options_switchRedstoneDot.setClickable(true);
			options_switchHideDebugText.setClickable(true);
			options_switchAutoSaveLevel.setClickable(true);
			options_switchSelectAllInLeft.setClickable(true);
			options_switchDisableTextureIsotropic.setClickable(true);
		}
	}

	private void saveOptions(Context c)
	{
		UtilsSettings settings=new UtilsSettings(c);
		settings.setRedstoneDot(options_switchRedstoneDot.isChecked());
		settings.setSafeMode(options_switchSafetyMode.isChecked());
		settings.setHideDebugText(options_switchHideDebugText.isChecked());
		settings.setAutoSaveLevel(options_switchAutoSaveLevel.isChecked());
		settings.setSelectAllInLeft(options_switchSelectAllInLeft.isChecked());
		settings.setDisableTextureIsotropic(options_switchDisableTextureIsotropic.isChecked());
	}

	private void loadOptions(Context c)
	{
		UtilsSettings settings=new UtilsSettings(c);
		options_switchSafetyMode.setChecked(settings.getSafeMode());
		options_switchRedstoneDot.setChecked(settings.getRedstoneDot());
		options_switchHideDebugText.setChecked(settings.getHideDebugText());
		options_switchAutoSaveLevel.setChecked(settings.getAutoSaveLevel());
		options_switchSelectAllInLeft.setChecked(settings.getSelectAllInLeft());
		options_switchDisableTextureIsotropic.setChecked(settings.getDisableTextureIsotropic());
	}
}
