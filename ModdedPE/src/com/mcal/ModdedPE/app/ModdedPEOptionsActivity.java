package com.mcal.ModdedPE.app;
import android.app.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.mcal.MCDesign.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.resources.*;
import com.mcal.ModdedPE.utils.*;

public class ModdedPEOptionsActivity extends Activity
{
	private MCDSwitch switchSafetyMode;
	private MCDSwitch switchRedstoneDot;
	private MCDSwitch switchToggleDebugText;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_options);

		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_header_bg);  
			((ImageView)findViewById(R.id.moddedpeOptionsHeaderBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_bg);  
			((ImageView)findViewById(R.id.moddedpeOptionsBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		((TextView)findViewById(R.id.moddedpeOptionsTitleTextView)).setText(getTitle());
		
		switchSafetyMode=(MCDSwitch)findViewById(R.id.moddedpeoptionsMCDSwitchSafetyMode);
		switchRedstoneDot=(MCDSwitch)findViewById(R.id.moddedpeoptionsMCDSwitchRedstoneDot);
		switchToggleDebugText=(MCDSwitch)findViewById(R.id.moddedpeoptionsMCDSwitchToggleDebugText);
		
		loadOptions();
		
		View.OnClickListener switchUpdateListener=new View.OnClickListener()
		{

			@Override
			public void onClick(View p1)
			{
				refreshViews();
				saveOptions();
			}
			
			
		};
		
		switchSafetyMode.setOnClickListener(switchUpdateListener);
		switchRedstoneDot.setOnClickListener(switchUpdateListener);
		switchToggleDebugText.setOnClickListener(switchUpdateListener);
		
		refreshViews();
	}
	
	
	public void onCloseClicked(View view)
	{
		finish();
	}

	@Override
	protected void onDestroy()
	{
		saveOptions();
		super.onDestroy();
	}
	
	private void refreshViews()
	{
		if(switchSafetyMode.isChecked())
		{
			switchRedstoneDot.setChecked(false);
			switchRedstoneDot.setClickable(false);
			switchToggleDebugText.setChecked(false);
			switchToggleDebugText.setClickable(false);
		}
		else
		{
			switchRedstoneDot.setClickable(true);
			switchToggleDebugText.setClickable(true);
		}
	}
	
	private void saveOptions()
	{
		Settings settings=new Settings(this);
		settings.setRedstoneDot(switchRedstoneDot.isChecked());
		settings.setSafeMode(switchSafetyMode.isChecked());
		settings.setToggleDebugText(switchToggleDebugText.isChecked());
	}
	
	private void loadOptions()
	{
		Settings settings=new Settings(this);
		switchSafetyMode.setChecked(settings.getSafeMode());
		switchRedstoneDot.setChecked(settings.getRedstoneDot());
		switchToggleDebugText.setChecked(settings.getToggleDebugText());
	}
}
