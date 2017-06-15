package com.mcal.ModdedPE.app;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.pesdk.nmod.*;
import java.io.*;

public class NModDescriptionActivity extends BaseActivity
{
	public final static String TAG_PACKAGE_NAME="nmod_package_name";
	public final static String TAG_NAME="nmod_name";
	public final static String TAG_AUTHOR="author";
	public final static String TAG_VERSION_NAME="version_name";
	public final static String TAG_DESCRIPTION="description";
	public final static String TAG_ICON_PATH="icon_path";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_nmod_description);

		String nmodPackageName = getIntent().getExtras().getString(TAG_PACKAGE_NAME);
		Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_null_pack);
		try
		{
			String iconpath = getIntent().getExtras().getString(TAG_ICON_PATH);
			FileInputStream fileInput = new FileInputStream(iconpath);
			icon = BitmapFactory.decodeStream(fileInput);
		}
		catch (Throwable e)
		{}

		String description = getIntent().getExtras().getString(TAG_DESCRIPTION);
		String name = getIntent().getExtras().getString(TAG_NAME);
		String version_name = getIntent().getExtras().getString(TAG_VERSION_NAME);
		String author = getIntent().getExtras().getString(TAG_AUTHOR);

		setTitle(name);
		setActionBarButtonCloseRight();

		AppCompatImageView iconImage=(AppCompatImageView)findViewById(R.id.moddedpenmoddescriptionImageViewIcon);
		iconImage.setImageBitmap(icon);

		AppCompatTextView textViewName=(AppCompatTextView)findViewById(R.id.moddedpenmoddescriptionTextViewNModName);
		textViewName.setText(name);
		AppCompatTextView textViewPackageName=(AppCompatTextView)findViewById(R.id.moddedpenmoddescriptionTextViewNModPackageName);
		textViewPackageName.setText(nmodPackageName);
		AppCompatTextView textViewDescription=(AppCompatTextView)findViewById(R.id.moddedpenmoddescriptionTextViewDescription);
		textViewDescription.setText(description);
		AppCompatTextView textViewAuthor=(AppCompatTextView)findViewById(R.id.moddedpenmoddescriptionTextViewAuthor);
		textViewAuthor.setText(author);
		AppCompatTextView textViewVersionName=(AppCompatTextView)findViewById(R.id.moddedpenmoddescriptionTextViewVersionName);
		textViewVersionName.setText(version_name);
	}

	public static void startThisActivity(Context context, NMod nmod)
	{
		Intent intent=new Intent(context, NModDescriptionActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString(TAG_PACKAGE_NAME, nmod.getPackageName());
		bundle.putString(TAG_NAME, nmod.getName());
		bundle.putString(TAG_DESCRIPTION, nmod.getDescription());
		bundle.putString(TAG_AUTHOR, nmod.getAuthor());
		bundle.putString(TAG_VERSION_NAME, nmod.getVersionName());
		File iconPath = nmod.copyIconToData();
		if (iconPath != null)
			bundle.putString(TAG_ICON_PATH, iconPath.getAbsolutePath());
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}
