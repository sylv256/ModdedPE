package com.mcal.ModdedPE.app;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.resources.*;

public class ModdedPEAboutActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_about);
		
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_header_bg);  
			((ImageView)findViewById(R.id.moddedpeAboutHeaderBackgroundStatus)).setImageBitmap(BitmapRepeater.createRepeaterW(getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_bg);  
			((ImageView)findViewById(R.id.moddedpeAboutBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}

		((TextView)findViewById(R.id.moddedpeAboutTitleTextView)).setText(getTitle());
	}
	
	public void onCloseClicked(View view)
	{
		finish();
	}
}
