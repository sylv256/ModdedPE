package org.mcal.moddedpe.app;

import android.os.*;
import org.mcal.moddedpe.*;
import android.view.*;
import android.content.*;
import android.net.*;
import android.support.v7.app.*;

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
	}
	
	public void onViewGithubClicked(View view)
	{
		openUri(URI_GITHUB);
	}
	
	public void onViewPrivacyPolicyClicked(View view)
	{
		openUri(URI_PRIVACY_POLICY);
	}
	
	public void onViewNModAPIClicked(View view)
	{
		openUri(URI_NMOD_API);
	}
	
	public void onViewTranslatorsClicked(View view)
	{
		new AlertDialog.Builder(this).setTitle(R.string.about_translators).setMessage(R.string.about_translators_message).setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					p1.dismiss();
				}
				
			
		}).show();
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
