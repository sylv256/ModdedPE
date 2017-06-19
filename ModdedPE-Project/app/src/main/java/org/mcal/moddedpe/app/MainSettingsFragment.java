package org.mcal.moddedpe.app;
import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.support.design.widget.*;
import java.util.*;
import org.mcal.moddedpe.*;
import org.mcal.moddedpe.utils.*;
import org.mcal.pesdk.utils.*;

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
					mSettings.setLanguageType(type);
					I18n.setLanguage(getActivity());
					Intent intent = new Intent(getActivity(), SplashesActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
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
