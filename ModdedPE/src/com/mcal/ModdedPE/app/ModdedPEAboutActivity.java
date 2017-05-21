package com.mcal.ModdedPE.app;

import android.os.*;
import com.mcal.MCDesign.app.*;
import com.mcal.ModdedPE.*;
import android.view.*;
import android.content.*;
import android.net.*;

public class ModdedPEAboutActivity extends MCDActivity
{
	private static final String URI_GITHUB = "https://github.com/MCAL-Team/ModdedPE.git";
	private static final String URI_APACHE_LICENSE_2_0 = "http://www.apache.org/licenses/LICENSE-2.0.html";;
	private static final String URI_GNU_LESSER_GENERAL_PUBLIC_LICENSE = "http://www.gnu.org/licenses/lgpl.html";
	
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
	
	public void onViewApacheLicenseClicked(View view)
	{
		openUri(URI_APACHE_LICENSE_2_0);
	}
	
	public void onGNULesserGeneralPublicLicenseClicked(View view)
	{
		openUri(URI_GNU_LESSER_GENERAL_PUBLIC_LICENSE);
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
