package com.mcal.ModdedPE.app;

import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.pesdk.nmod.*;

public class ModdedPENModLoadFailActivity extends ModdedPEActivity
{
	public final static String KEY_INTENT_EXTRAS_NMOD_NAME="nmod_name";
	public final static String KEY_INTENT_EXTRAS_NMOD_LOAD_FAIL_BUG_MESSAGE="nmod_bug_message";
	public final static String KEY_INTENT_EXTRAS_NMOD_LOAD_FAIL_FULL_MESSAGE="nmod_full_message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_nmod_load_failed);

		String nmodName=getIntent().getExtras().getString(KEY_INTENT_EXTRAS_NMOD_NAME);
		String nmodBugMessage=getIntent().getExtras().getString(KEY_INTENT_EXTRAS_NMOD_LOAD_FAIL_BUG_MESSAGE);
		String nmodFullMessage=getIntent().getExtras().getString(KEY_INTENT_EXTRAS_NMOD_LOAD_FAIL_FULL_MESSAGE);
		
		setTitle(nmodName);
		setActionBarButtonCloseRight();

		((AppCompatTextView)findViewById(R.id.moddedpeLFWarningTextView)).setText(getString(R.string.load_fail_warning, new String[]{nmodName}));
		((AppCompatTextView)findViewById(R.id.moddedpeLFMsgTextView)).setText(nmodBugMessage);
		((AppCompatTextView)findViewById(R.id.moddedpeLFFullTextView)).setText(nmodFullMessage);
	}

	public static void startThisActivity(Context context, NMod nmod)
	{
		Intent intent=new Intent(context, ModdedPENModLoadFailActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle=new Bundle();
		bundle.putString(KEY_INTENT_EXTRAS_NMOD_NAME, nmod.getName());
		bundle.putString(KEY_INTENT_EXTRAS_NMOD_LOAD_FAIL_BUG_MESSAGE, nmod.getLoadException().getNModBugMessage());
		bundle.putString(KEY_INTENT_EXTRAS_NMOD_LOAD_FAIL_FULL_MESSAGE, nmod.getLoadException().toString());
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}
