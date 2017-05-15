package com.mcal.ModdedPE.app;
import com.mcal.MCDesign.app.*;
import android.os.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;
import android.support.v7.widget.*;
import android.widget.*;
import android.content.*;

public class ModdedPENModDescriptionActivity extends MCDActivity
{
	private NMod targetNMod;
	public final static String KEY_INTENT_EXTRAS_PACKAGE_NAME="nmod_package_name";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_nmod_description);

		String nmodPackageName=getIntent().getExtras().getString(KEY_INTENT_EXTRAS_PACKAGE_NAME);
		targetNMod = NModManager.getNModManager(this).getNMod(nmodPackageName);

		setTitle(targetNMod.getName());
		setActionBarButtonCloseRight();

		AppCompatImageView iconImage=(AppCompatImageView)findViewById(R.id.moddedpenmoddescriptionImageViewIcon);
		iconImage.setImageBitmap(targetNMod.getIcon());
		iconImage.getLayoutParams().width = iconImage.getLayoutParams().height = getWindowManager().getDefaultDisplay().getWidth() / 5;

		AppCompatTextView textViewName=(AppCompatTextView)findViewById(R.id.moddedpenmoddescriptionTextViewNModName);
		textViewName.setText(targetNMod.getName());
		AppCompatTextView textViewPackageName=(AppCompatTextView)findViewById(R.id.moddedpenmoddescriptionTextViewNModPackageName);
		textViewPackageName.setText(targetNMod.getPackageName());
		AppCompatTextView textViewDescription=(AppCompatTextView)findViewById(R.id.moddedpenmoddescriptionTextViewDescription);
		textViewDescription.setText(targetNMod.getDescription());
		AppCompatTextView textViewAuthor=(AppCompatTextView)findViewById(R.id.moddedpenmoddescriptionTextViewAuthor);
		textViewAuthor.setText(targetNMod.getAuthor());
		AppCompatTextView textViewVersionName=(AppCompatTextView)findViewById(R.id.moddedpenmoddescriptionTextViewVersionName);
		textViewVersionName.setText(targetNMod.getVersionName());
	}

	public static void startThisActivity(Context context, NMod nmod)
	{
		Intent intent=new Intent(context, ModdedPENModDescriptionActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString(KEY_INTENT_EXTRAS_PACKAGE_NAME, nmod.getPackageName());
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}
