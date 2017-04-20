package com.mcal.ModdedPE.app;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;
import com.mcal.MCDesign.widget.*;
import android.support.v7.widget.*;
import com.mcal.MCDesign.utils.*;

public class NModDescriptionDialog extends Dialog 
{
	private NMod targetNMod;
	
	public NModDescriptionDialog(Activity activity,NMod targetNMod)
	{
		super(activity,R.style.AppTheme);
		setOwnerActivity(activity);
		
		this.targetNMod=targetNMod;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_nmod_description);

		{
			Bitmap bitmap = BitmapFactory.decodeResource(getOwnerActivity().getResources(), R.drawable.mcd_header_bg);  
			((ImageView)findViewById(R.id.moddedpeDescriptionHeaderBackgroundStatus)).setImageBitmap(BitmapRepeater.repeat(getOwnerActivity().getWindowManager().getDefaultDisplay().getWidth(),getOwnerActivity().getWindowManager().getDefaultDisplay().getHeight(), bitmap));
		}
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getOwnerActivity().getResources(), R.drawable.mcd_bg);  
			((ImageView)findViewById(R.id.moddedpeDescriptionBackground)).setImageBitmap(BitmapRepeater.repeat(getOwnerActivity().getWindowManager().getDefaultDisplay().getWidth(), getOwnerActivity().getWindowManager().getDefaultDisplay().getHeight(),bitmap));
		}
		((TextView)findViewById(R.id.moddedpeDescriptionTitleTextView)).setText(targetNMod.getName());
		
		MCDBurgerButtonClose buttonClose=(MCDBurgerButtonClose)findViewById(R.id.moddedpenmoddescriptionMCDBurgerButtonClose);
		buttonClose.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					NModDescriptionDialog.this.dismiss();
				}
				
			
		});
		
		ImageView iconImage=(ImageView)findViewById(R.id.moddedpenmoddescriptionImageViewIcon);
		iconImage.setImageBitmap(targetNMod.getIcon());
		iconImage.getLayoutParams().width=iconImage.getLayoutParams().height=getOwnerActivity().getWindowManager().getDefaultDisplay().getWidth()/5;
		
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
}
