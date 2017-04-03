package com.mcal.ModdedPE.app;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.widget.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.resources.*;

public class ModdedPEErrorActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_error_activity);
		
		final String msg = getIntent().getExtras().getString("msg");
		
		if(msg!=null)
		{
			AppCompatTextView text=(AppCompatTextView)findViewById(R.id.moddedpeerroractivityAppCompatTextViewErrorMsg);
			text.setText(msg);
		}
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_header_bg);  
			((ImageView)findViewById(R.id.moddedpeErrorHeaderBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_bg);  
			((ImageView)findViewById(R.id.moddedpeErrorBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		((TextView)findViewById(R.id.moddedpeErrorTitleTextView)).setText(getTitle());
	}
}
