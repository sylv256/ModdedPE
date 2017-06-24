package org.mcal.moddedpe.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import org.mcal.moddedpe.R;

public class AboutActivity extends BaseActivity
{
	private static final String URI_GITHUB = "https://github.com/ModelPart/ModdedPE.git";
	private static final String URI_PRIVACY_POLICY = "https://github.com/ModelPart/ModdedPE/blob/master/PrivacyPolicy/README.md";
	private static final String URI_NMOD_API = "http://github.com/ModelPart/NModAPI.git";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_about);

		setActionBarButtonCloseRight();

		findViewById(R.id.about_view_github_button).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					openUri(URI_GITHUB);
				}


			});

		findViewById(R.id.about_view_nmod_api_button).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					openUri(URI_NMOD_API);
				}


			});

		findViewById(R.id.about_view_privacy_policy_button).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					openUri(URI_PRIVACY_POLICY);
				}


			});

		findViewById(R.id.about_translators_button).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					new AlertDialog.Builder(AboutActivity.this).setTitle(R.string.about_translators).setMessage(R.string.about_translators_message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								p1.dismiss();
							}


						}).show();
				}


			});
	}

	private void openUri(String uri)
	{
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(uri);
		intent.setData(content_url);
		startActivity(intent);
	}
}
