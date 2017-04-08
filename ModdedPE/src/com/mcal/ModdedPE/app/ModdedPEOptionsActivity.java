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
	private MCDSwitch switchHideDebugText;
	private MCDSwitch switchAutoSaveLevel;
	private MCDSwitch switchSelectAllInLeft;
	
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
		switchHideDebugText=(MCDSwitch)findViewById(R.id.moddedpeoptionsMCDSwitchToggleDebugText);
		switchAutoSaveLevel=(MCDSwitch)findViewById(R.id.moddedpeoptionsMCDSwitchAutoSaveLevel);
		switchSelectAllInLeft=(MCDSwitch)findViewById(R.id.moddedpeoptionsMCDSwitchSelectAllInLeft);
		
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
		switchHideDebugText.setOnClickListener(switchUpdateListener);
		switchAutoSaveLevel.setOnClickListener(switchUpdateListener);
		switchSelectAllInLeft.setOnClickListener(switchUpdateListener);
		
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
			switchHideDebugText.setChecked(false);
			switchHideDebugText.setClickable(false);
			switchAutoSaveLevel.setChecked(false);
			switchAutoSaveLevel.setClickable(false);
			switchSelectAllInLeft.setChecked(false);
			switchSelectAllInLeft.setClickable(false);
		}
		else
		{
			switchRedstoneDot.setClickable(true);
			switchHideDebugText.setClickable(true);
			switchAutoSaveLevel.setClickable(true);
			switchSelectAllInLeft.setClickable(true);
		}
	}
	
	private void saveOptions()
	{
		Settings settings=new Settings(this);
		settings.setRedstoneDot(switchRedstoneDot.isChecked());
		settings.setSafeMode(switchSafetyMode.isChecked());
		settings.setHideDebugText(switchHideDebugText.isChecked());
		settings.setAutoSaveLevel(switchAutoSaveLevel.isChecked());
		settings.setSelectAllInLeft(switchSelectAllInLeft.isChecked());
	}
	
	private void loadOptions()
	{
		Settings settings=new Settings(this);
		switchSafetyMode.setChecked(settings.getSafeMode());
		switchRedstoneDot.setChecked(settings.getRedstoneDot());
		switchHideDebugText.setChecked(settings.getHideDebugText());
		switchAutoSaveLevel.setChecked(settings.getAutoSaveLevel());
		switchSelectAllInLeft.setChecked(settings.getSelectAllInLeft());
	}
}
