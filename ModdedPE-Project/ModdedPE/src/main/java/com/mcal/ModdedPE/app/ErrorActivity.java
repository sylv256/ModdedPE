package com.mcal.ModdedPE.app;

import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import com.mcal.MCDesign.app.*;
import com.mcal.ModdedPE.*;

public class ErrorActivity extends MCDActivity
{
	public static final String KEY_INTENT_EXTRAS_ERROR_TEXT="error_msg";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_error_activity);

		String msg = getIntent().getExtras().getString(KEY_INTENT_EXTRAS_ERROR_TEXT);

		if (msg != null)
		{
			AppCompatTextView text=(AppCompatTextView)findViewById(R.id.moddedpeerroractivityAppCompatTextViewErrorMsg);
			text.setText(msg);
		}
	}

	public static void startThisActivity(Context context, String errorText)
	{
		Intent intent=new Intent(context, ErrorActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString(KEY_INTENT_EXTRAS_ERROR_TEXT, errorText);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}
}
