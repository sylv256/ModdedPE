package com.mcal.ModdedPE.app;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.mcal.MCDesign.app.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.resources.*;
import com.mcal.ModdedPE.utils.*;

public class ModdedPEMainActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_main);
		
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_header_bg);  
			((ImageView)findViewById(R.id.moddedpemainHeaderBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_bg);  
			((ImageView)findViewById(R.id.moddedpemainImageViewBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		
		final Settings settings=new Settings(this);
		
		findViewById(R.id.moddedpemainMCDPlayButton).getLayoutParams().width=getWindowManager().getDefaultDisplay().getWidth()/3;
		((TextView)findViewById(R.id.moddedpemainTextViewTitle)).setText(getTitle());
		((TextView)findViewById(R.id.moddedpemainTextViewAppVersion)).setText(getString(R.string.app_version));
		((TextView)findViewById(R.id.moddedpemainTextViewTargetMCVersion)).setText(getString(R.string.targetMCPE));
		((TextView)findViewById(R.id.moddedpemainTextViewisSafetyMode)).setVisibility(settings.getSafeMode()?View.VISIBLE:View.GONE);
		((TextView)findViewById(R.id.moddedpemainTextViewTargetMCVersion)).setTextColor(isSupportedMinecraftPEVersion()?Color.GREEN:Color.RED);
		
		if(!settings.getFirstLoaded())
		{
			final MCDDialog mdialog = new MCDDialog(this);
			mdialog.setTitleText(getString(R.string.welcome_title));
			mdialog.setText(getString(R.string.welcome_text));
			mdialog.setPositiveButton(getString(R.string.ok),new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						settings.setFirstLoaded(true);
						mdialog.dismiss();
					}
					
				
			});
		}
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
		Settings settings=new Settings(this);

		((TextView)findViewById(R.id.moddedpemainTextViewTitle)).setText(getTitle());
		((TextView)findViewById(R.id.moddedpemainTextViewAppVersion)).setText(getString(R.string.app_version));
		((TextView)findViewById(R.id.moddedpemainTextViewTargetMCVersion)).setText(getString(R.string.targetMCPE));
		((TextView)findViewById(R.id.moddedpemainTextViewTargetMCVersion)).setTextColor(isSupportedMinecraftPEVersion()?Color.GREEN:Color.RED);
		((TextView)findViewById(R.id.moddedpemainTextViewisSafetyMode)).setVisibility(settings.getSafeMode()?View.VISIBLE:View.GONE);
	}
	
	public void onPlayClicked(View v)
	{
		if(getMcContext()==null)
		{
			//MinecraftPE did not installed
			final MCDDialog mdialog = new MCDDialog(this);
			mdialog.setTitleText(getString(R.string.error));
			mdialog.setText(getString(R.string.nomcpe_error));
			mdialog.setPositiveButton(getString(R.string.ok),new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						mdialog.dismiss();
					}
					
				
			});
		}
		else if(!isSupportedMinecraftPEVersion())
		{
			//Unavailable version of MinecraftPE
			final MCDDialog mdialog = new MCDDialog(this);
			mdialog.setTitleText(getString(R.string.noavmcpe_title));
			mdialog.setText(getString(R.string.noavmcpe_description)+getString(R.string.targetMCPE));
			mdialog.setNegativeButton(getString(R.string.cancel),new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						mdialog.dismiss();
					}


				});
			mdialog.setPositiveButton(getString(R.string.jump),new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						startMinecraft();
					}


				});
			return;
		}
		else
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
