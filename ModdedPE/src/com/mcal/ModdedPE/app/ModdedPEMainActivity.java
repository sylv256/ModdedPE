package com.mcal.ModdedPE.app;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mcal.MCDesign.app.*;
import com.mcal.MCDesign.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;
import com.mcal.ModdedPE.utils.*;
import java.util.*;
import com.mcal.ModdedPE.widget.*;

public class ModdedPEMainActivity extends MCDActivity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_main);
		resetViews();
		
		final Settings settings=new Settings(this);
		if(!settings.getFirstLoaded())
		{
//			final MCDAlertDialog.Builder mdialog = new MCDAlertDialog.Builder(this);
//			mdialog.setTitle(getString(R.string.welcome_title));
//			mdialog.setMessage(getString(R.string.welcome_text));
//			mdialog.setNegativeButton(getString(R.string.ok),new DialogInterface.OnClickListener()
//				{
//
//					@Override
//					public void onClick(DialogInterface p1,int id)
//					{
//						settings.setFirstLoaded(true);
//						p1.dismiss();
//					}
//			});
//			mdialog.show();
		}
	}
	
	private void resetViews()
	{
		final Settings settings=new Settings(this);

		findViewById(R.id.moddedpemainMCDPlayButton).getLayoutParams().width=getWindowManager().getDefaultDisplay().getWidth()/3;
		((TextView)findViewById(R.id.moddedpemainTextViewAppVersion)).setText(getString(R.string.app_version));
		((TextView)findViewById(R.id.moddedpemainTextViewTargetMCVersion)).setText(getString(R.string.targetMCPE));
		((TextView)findViewById(R.id.moddedpemainTextViewisSafetyMode)).setVisibility(settings.getSafeMode()?View.VISIBLE:View.GONE);
		((TextView)findViewById(R.id.moddedpemainTextViewTargetMCVersion)).setTextColor(isSupportedMinecraftPEVersion()?Color.GREEN:Color.RED);
		
		MCDBurgerButton burgerButton=new MCDBurgerButton(this);
		burgerButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					onMenuClicked(p1);
				}
				
			
		});
		setActionBarViewRight(burgerButton);
		
		prepareNews();
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		
		resetViews();
	}
	
	private Context getMcContext()
	{
		try
		{
			return createPackageContext(ModdedPEApplication.MC_PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	private boolean isSupportedMinecraftPEVersion()
	{
		if(getMcContext()==null)
			return false;
		try
		{
			return getMcContext().getPackageManager().getPackageInfo(getMcContext().getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName.equals(getString(R.string.targetMCPE));
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		return false;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		resetViews();
	}
	
	private NMod showingNMod;
	
	private void prepareNews()
	{
		Vector<NMod> nmods=NModManager.getNModManager(this).getActiveNModsHasNews();
		
		NewsLayout newsLayout=(NewsLayout) findViewById(R.id.moddedpemainNewsLayout);
		if(nmods.size()>0)
		{
			showingNMod=nmods.get(new Random(System.nanoTime()).nextInt(nmods.size()));
			newsLayout.setNewsImage(showingNMod.getVersionImage());
			newsLayout.setNewsTitle(showingNMod.getNewsTitle());
		}
		else
		{
			showingNMod=null;
			newsLayout.setNewsImage(BitmapFactory.decodeResource(getResources(),R.drawable.image_default_minecraft));
			newsLayout.setNewsTitle(getString(R.string.main_default_minecraft_title));
		}
	}
	
	public void onNewsClicked(View view)
	{
		if(showingNMod!=null)
			new NModDescriptionDialog(this,showingNMod).show();
	}
	
	public void onPlayClicked(View v)
	{
//		if(getMcContext()==null)
//		{
//			MinecraftPE did not installed
//			final MCDAlertDialog.Builder mdialog = new MCDAlertDialog.Builder(this);
//			mdialog.setTitle(getString(R.string.error));
//			mdialog.setMessage(getString(R.string.nomcpe_error));
//			mdialog.setNegativeButton(getString(R.string.ok),new DialogInterface.OnClickListener()
//				{
//
//					@Override
//					public void onClick(DialogInterface p1,int id)
//					{
//						p1.dismiss();
//					}
//				});
//			mdialog.show();
//		}
//		else if(!isSupportedMinecraftPEVersion())
//		{
//			Unavailable version of MinecraftPE
//			final MCDAlertDialog.Builder mdialog = new MCDAlertDialog.Builder(this);
//			mdialog.setTitle(getString(R.string.noavmcpe_title));
//			mdialog.setMessage(getString(R.string.noavmcpe_description));
//			mdialog.setNeutralButton(getString(R.string.ok),new DialogInterface.OnClickListener()
//				{
//
//					@Override
//					public void onClick(DialogInterface p1,int id)
//					{
//						p1.dismiss();
//					}
//				});
//			mdialog.setNegativeButton(getString(R.string.jump),new DialogInterface.OnClickListener()
//				{
//
//					@Override
//					public void onClick(DialogInterface p1,int id)
//					{
//						startMinecraft();
//					}
//
//
//				});
//			mdialog.show();
//			return;
//		}
//		else
			startMinecraft();
	}
	
	public void onMenuClicked(View v)
	{
		startActivity(new Intent(this,ModdedPEMenuActivity.class));
	}
	
	private void startMinecraft()
	{
		Intent i=null;
		if(new Settings(this).getSafeMode())
			i=new Intent(this,com.mcal.ModdedPE.app.ModdedPESafetyModeMinecraftActivity.class);
		else
			i=new Intent(this,com.mcal.ModdedPE.app.ModdedPEMinecraftActivity.class);
		startActivity(i);
		
		finish();
	}
	
}
