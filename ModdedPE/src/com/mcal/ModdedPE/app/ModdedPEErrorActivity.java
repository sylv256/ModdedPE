package com.mcal.ModdedPE.app;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.widget.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.MCDesign.app.*;
import android.content.*;

public class ModdedPEErrorActivity extends MCDActivity
{
	public static final String KEY_INTENT_EXTRAS_ERROR_TEXT="error_msg";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_error_activity);
		
		final String msg = getIntent().getExtras().getString(KEY_INTENT_EXTRAS_ERROR_TEXT);
		
		if(msg!=null)
		{
			AppCompatTextView text=(AppCompatTextView)findViewById(R.id.moddedpeerroractivityAppCompatTextViewErrorMsg);
			text.setText(msg);
		}
	}
	
	public static void startThisActivity(Context context,String errorText)
	{
		Intent intent=new Intent(context,ModdedPEErrorActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString(KEY_INTENT_EXTRAS_ERROR_TEXT,errorText);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}
