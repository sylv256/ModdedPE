package com.mcal.ModdedPE.app;
import com.mcal.MCDesign.app.*;
import android.os.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;
import android.support.v7.widget.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import java.io.*;
import com.mcal.ModdedPE.utils.*;

public class ModdedPENModDescriptionActivity extends ModdedPEActivity
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
		String iconpath = getIntent().getExtras().getString(TAG_ICON_PATH);
		Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.mcd_null_pack);
		try
		{
			FileInputStream fileInput = new FileInputStream(iconpath);
			icon = BitmapFactory.decodeStream(fileInput);
		}
		catch (FileNotFoundException e)
		{}
		
		String description = getIntent().getExtras().getString(TAG_DESCRIPTION);
		String name = getIntent().getExtras().getString(TAG_NAME);
		String version_name = getIntent().getExtras().getString(TAG_VERSION_NAME);
		String author = getIntent().getExtras().getString(TAG_AUTHOR);

		setTitle(name);
		setActionBarButtonCloseRight();

		AppCompatImageView iconImage=(AppCompatImageView)findViewById(R.id.moddedpenmoddescriptionImageViewIcon);
		iconImage.setImageBitmap(icon);
		iconImage.getLayoutParams().width = iconImage.getLayoutParams().height = getWindowManager().getDefaultDisplay().getWidth() / 5;

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
		Intent intent=new Intent(context, ModdedPENModDescriptionActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString(TAG_PACKAGE_NAME, nmod.getPackageName());
		bundle.putString(TAG_NAME, nmod.getName());
		bundle.putString(TAG_DESCRIPTION, nmod.getDescription());
		bundle.putString(TAG_AUTHOR, nmod.getAuthor());
		bundle.putString(TAG_VERSION_NAME, nmod.getVersionName());
		File file = new File(context.getFilesDir() + File.separator + "nmod_icons" + File.separator + nmod.getPackageName());
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			nmod.getIcon().compress(Bitmap.CompressFormat.PNG, 100, baos);  
			file.createNewFile();
			FileOutputStream outfile = new FileOutputStream(file);
			outfile.write(baos.toByteArray());
			outfile.close();
		}
		catch (IOException ioe)
		{}
		bundle.putString(TAG_ICON_PATH, file.getPath());
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}
