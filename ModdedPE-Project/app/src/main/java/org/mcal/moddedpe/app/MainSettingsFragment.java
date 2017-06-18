package org.mcal.moddedpe.app;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import org.mcal.mcdesign.widget.*;
import org.mcal.moddedpe.*;
import android.preference.*;
import org.mcal.moddedpe.utils.*;
import android.app.*;
import android.support.design.widget.*;
import org.mcal.pesdk.utils.*;
import java.util.*;

public class MainSettingsFragment extends PreferenceFragment
{
	private UtilsSettings mSettings = null;
	private CheckBoxPreference mSafeModePreference = null;
	private Preference mDataPathPreference = null;
	private Preference mAboutPreference = null;
	private Preference mPkgPreference = null;
	private ListPreference mLanguagePreference = null;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		mSettings = new UtilsSettings(getActivity());

		mSafeModePreference = (CheckBoxPreference)findPreference("safe_mode");
		mSafeModePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
			{

				@Override
				public boolean onPreferenceChange(Preference p1, Object p2)
				{
					mSettings.setSafeMode((boolean)p2);
					return true;
				}


			});
		mSafeModePreference.setChecked(mSettings.isSafeMode());

		mDataPathPreference = findPreference("data_saved_path");
		mDataPathPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
			{

				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					DirPickerActivity.startThisActivity(getActivity());
					return true;
				}


			});
		mAboutPreference = findPreference("about");
		mAboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
			{

				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					Intent intent = new Intent(getActivity(), AboutActivity.class);
					getActivity().startActivity(intent);
					return true;
				}


			});
		mPkgPreference = findPreference("game_pkg_name");
		mPkgPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
			{

				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					MCPkgPickerActivity.startThisActivity(getActivity());
					return true;
				}


			});

		mLanguagePreference = (ListPreference)findPreference("language");
		mLanguagePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
			{

				@Override
				public boolean onPreferenceChange(Preference p1, Object p2)
				{
					int type = new Integer((String)p2);
					mSettings.setLanguaheType(type);
					switch (type)
					{
						case 0:
							getResources().getConfiguration().locale = Locale.getDefault();
							break;
						case 1:
							getResources().getConfiguration().locale = Locale.ENGLISH;
							break;
						case 2:
							getResources().getConfiguration().locale = Locale.SIMPLIFIED_CHINESE;
							break;
					}
					getResources().updateConfiguration(getResources().getConfiguration(), getResources().getDisplayMetrics());
					Intent intent = new Intent(getActivity(), SplashesActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					getActivity().startActivity(intent);
					getActivity().finish();
					return true;
				}


			});
		updatePreferences();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == DirPickerActivity.REQUEST_PICK_DIR && resultCode == Activity.RESULT_OK)
		{
			String dir = data.getExtras().getString(DirPickerActivity.TAG_DIR_PATH);
			mSettings.setDataSavedPath(dir);
			if (dir.equals(LauncherOptions.STRING_VALUE_DEFAULT))
				Snackbar.make(getActivity().getWindow().getDecorView(), getString(R.string.preferences_update_message_reset_data_path), 2500).show();
			else
				Snackbar.make(getActivity().getWindow().getDecorView(), getString(R.string.preferences_update_message_data_path, new Object[]{dir}), 2500).show();
		}
		else if (requestCode == MCPkgPickerActivity.REQUEST_PICK_PACKAGE && resultCode == Activity.RESULT_OK)
		{
			String pkgName = data.getExtras().getString(MCPkgPickerActivity.TAG_PACKAGE_NAME);
			mSettings.setMinecraftPackageName(pkgName);
			if (pkgName.equals(LauncherOptions.STRING_VALUE_DEFAULT))
				Snackbar.make(getActivity().getWindow().getDecorView(), getString(R.string.preferences_update_message_reset_pkg_name), 2500).show();
			else
				Snackbar.make(getActivity().getWindow().getDecorView(), getString(R.string.preferences_update_message_pkg_name, new Object[]{pkgName}), 2500).show();
			Intent intent = new Intent(getActivity(), SplashesActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			getActivity().startActivity(intent);
		}
		updatePreferences();
	}

	private void updatePreferences()
	{
		if (!mSettings.getDataSavedPath().equals(mSettings.STRING_VALUE_DEFAULT))
			mDataPathPreference.setSummary(mSettings.getDataSavedPath());
		else
			mDataPathPreference.setSummary(R.string.preferences_summary_data_saved_path);

		if (!mSettings.getMinecraftPEPackageName().equals(mSettings.STRING_VALUE_DEFAULT))
			mPkgPreference.setSummary(mSettings.getMinecraftPEPackageName());
		else
			mPkgPreference.setSummary(R.string.preferences_summary_game_pkg_name);
	}
}
