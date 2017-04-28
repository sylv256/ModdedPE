package com.mcal.ModdedPE.app;

import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import com.mcal.MCDesign.app.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;

public class ModdedPENModLoadFailActivity extends MCDActivity
{
	private NMod targetNMod;
	public final static String KEY_INTENT_EXTRAS_PACKAGE_NAME="nmod_package_name";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_nmod_load_failed);

		String nmodPackageName=getIntent().getExtras().getString(KEY_INTENT_EXTRAS_PACKAGE_NAME);
		targetNMod = NModManager.getNModManager(this).getNModByPackageName(nmodPackageName);

		setTitle(targetNMod.getName());
		setActionBarButtonCloseRight();

		((AppCompatTextView)findViewById(R.id.moddedpeLFWarningTextView)).setText(getString(R.string.load_fail_warning, new String[]{targetNMod.getName()}));
		((AppCompatTextView)findViewById(R.id.moddedpeLFMsgTextView)).setText(targetNMod.getLoadException().getBugMessage());
		((AppCompatTextView)findViewById(R.id.moddedpeLFDealTextView)).setText(targetNMod.getLoadException().getDealMessage());
		((AppCompatTextView)findViewById(R.id.moddedpeLFFullTextView)).setText(targetNMod.getLoadException().toString());
	}

	public static void startThisActivity(Context context, NMod nmod)
	{
		Intent intent=new Intent(context, ModdedPENModLoadFailActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle=new Bundle();
		bundle.putString(KEY_INTENT_EXTRAS_PACKAGE_NAME, nmod.getPackageName());
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}
