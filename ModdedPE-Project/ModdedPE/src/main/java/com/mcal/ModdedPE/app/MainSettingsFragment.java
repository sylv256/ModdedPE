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
	private ListView mOptionsListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View options_view=inflater.inflate(R.layout.moddedpe_options, null);

		mOptionsListView =(ListView) options_view.findViewById(R.id.moddedpe_options_list_view);
		loadOptions(getContext());

		/*options_switchSafetyMode.setOnCheckedChangeListener(options_switchUpdateListener);
		options_switchRedstoneDot.setOnCheckedChangeListener(options_switchUpdateListener);
		options_switchHideDebugText.setOnCheckedChangeListener(options_switchUpdateListener);
		options_switchAutoSaveLevel.setOnCheckedChangeListener(options_switchUpdateListener);
		options_switchSelectAllInLeft.setOnCheckedChangeListener(options_switchUpdateListener);
		options_switchDisableTextureIsotropic.setOnCheckedChangeListener(options_switchUpdateListener);
*/
		refreshOptionsViews();

		return options_view;
	}

	private void refreshOptionsViews()
	{
		/*if (options_switchSafetyMode.isChecked())
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
		}*/
	}

	private void saveOptions(Context c)
	{
		/*UtilsSettings settings=new UtilsSettings(c);
		settings.setRedstoneDot(options_switchRedstoneDot.isChecked());
		settings.setSafeMode(options_switchSafetyMode.isChecked());
		settings.setHideDebugText(options_switchHideDebugText.isChecked());
		settings.setAutoSaveLevel(options_switchAutoSaveLevel.isChecked());
		settings.setSelectAllInLeft(options_switchSelectAllInLeft.isChecked());
		settings.setDisableTextureIsotropic(options_switchDisableTextureIsotropic.isChecked());*/
	}

	private void loadOptions(Context c)
	{
		/*UtilsSettings settings=new UtilsSettings(c);
		options_switchSafetyMode.setChecked(settings.getSafeMode());
		options_switchRedstoneDot.setChecked(settings.getRedstoneDot());
		options_switchHideDebugText.setChecked(settings.getHideDebugText());
		options_switchAutoSaveLevel.setChecked(settings.getAutoSaveLevel());
		options_switchSelectAllInLeft.setChecked(settings.getSelectAllInLeft());
		options_switchDisableTextureIsotropic.setChecked(settings.getDisableTextureIsotropic());*/
	}
	
	private class OptionsListAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return 0;
		}

		@Override
		public Object getItem(int p1)
		{
			return p1;
		}

		@Override
		public long getItemId(int p1)
		{
			return p1;
		}

		@Override
		public View getView(int p1, View p2, ViewGroup p3)
		{
			View view = getActivity().getLayoutInflater().inflate(R.layout.moddedpe_options_item,null);
			AppCompatTextView name = (AppCompatTextView) view.findViewById(R.id.moddedpe_settings_item_name);
			name.setText(getItemName(p1));
			MCDSwitch switch_view = (MCDSwitch) view.findViewById(R.id.moddedpe_settings_item_switch);
			CompoundButton.OnCheckedChangeListener options_switchUpdateListener=new CompoundButton.OnCheckedChangeListener()
			{

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					refreshOptionsViews();
					saveOptions(p1.getContext());
				}

			};
			switch_view.setOnCheckedChangeListener(options_switchUpdateListener);
			return view;
		}
		
		private String getItemName(int p1)
		{
			return "";
		}
	}
}
