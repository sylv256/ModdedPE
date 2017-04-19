package com.mcal.ModdedPE.app;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.widget.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.resources.*;
import com.mcal.MCDesign.app.*;

public class ModdedPEErrorActivity extends MCDActivity
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
	}
}
