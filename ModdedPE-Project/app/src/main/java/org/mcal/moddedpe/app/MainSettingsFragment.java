package org.mcal.moddedpe.app;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import org.mcal.moddedpe.R;
import org.mcal.moddedpe.utils.I18n;
import org.mcal.moddedpe.utils.UtilsSettings;
import org.mcal.pesdk.utils.LauncherOptions;

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
					if(checkPermissions())
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
					int type = Integer.valueOf((String)p2);
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

	private boolean checkPermissions()
	{
		if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
			return false;
		}
		return true;
	}

	private void showPermissionDinedDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.permission_grant_failed_title);
		builder.setMessage(R.string.permission_grant_failed_message);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Intent intent = new Intent();
				intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				startActivity(intent);
			}
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == 1)
		{
			boolean isAllGranted = true;

			for (int grant : grantResults)
			{
				if (grant != PackageManager.PERMISSION_GRANTED)
				{
					isAllGranted = false;
					break;
				}
			}

			if (isAllGranted)
			{
				DirPickerActivity.startThisActivity(getActivity());
			}
			else
			{
				showPermissionDinedDialog();
			}
		}
	}
}
