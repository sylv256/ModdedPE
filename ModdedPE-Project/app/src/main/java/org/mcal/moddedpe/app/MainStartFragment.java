package org.mcal.moddedpe.app;

import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import org.mcal.moddedpe.*;
import org.mcal.moddedpe.utils.*;

public class MainStartFragment extends BaseFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.moddedpe_main, null);
		view.findViewById(R.id.moddedpe_main_play_button).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					onPlayClicked();
				}


			});
		return view;
	}
	
	private void onPlayClicked()
	{
		if (!getPESdk().getMinecraftInfo().isMinecraftInstalled())
		{
			android.support.v7.app.AlertDialog.Builder mdialog = new AlertDialog.Builder(getActivity());
			mdialog.setTitle(getString(R.string.no_mcpe_found_title));
			mdialog.setMessage(getString(R.string.no_mcpe_found));
			mdialog.setPositiveButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int id)
					{
						p1.dismiss();
					}
				});
			mdialog.show();
		}
		else if (!getPESdk().getMinecraftInfo().isSupportedMinecraftVersion(getResources().getStringArray(R.array.target_mcpe_versions)))
		{
			android.support.v7.app.AlertDialog.Builder mdialog = new AlertDialog.Builder(getActivity());
			mdialog.setTitle(getString(R.string.no_available_mcpe_version_found_title));
			mdialog.setMessage(getString(R.string.no_available_mcpe_version_found, new Object[]{getPESdk().getMinecraftInfo().getMinecraftVersionName(),getString(R.string.target_mcpe_version_info)}));
			mdialog.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int id)
					{
						p1.dismiss();
					}
				});
			mdialog.setPositiveButton(getString(R.string.no_available_mcpe_version_continue), new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int id)
					{
						startMinecraft();
					}


				});
			mdialog.show();
		}
		else
			startMinecraft();
	}

	private void startMinecraft()
	{
		if (new UtilsSettings(getActivity()).isSafeMode())
		{
			new AlertDialog.Builder(getActivity()).setTitle(R.string.safe_mode_on_title).setMessage(R.string.safe_mode_on_message).
				setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						Intent intent = null;
						intent = new Intent(getActivity(), PreloadActivity.class);
						startActivity(intent);
						getActivity().finish();
						p1.dismiss();
					}


				}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						p1.dismiss();
					}


				}).show();
		}
		else
		{
			startActivity(new Intent(getActivity(), PreloadActivity.class));
			getActivity().finish();
		}
	}
}
