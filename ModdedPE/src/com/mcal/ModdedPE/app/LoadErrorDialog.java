package com.mcal.ModdedPE.app;
import android.app.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;
import com.mcal.MCDesign.utils.*;

public class LoadErrorDialog extends Dialog
{
	private NMod errorNMod;
	private Activity parentActivity;
	
	public LoadErrorDialog(Activity context,NMod nmod)
	{
		super(context,R.style.AppTheme);
		
		errorNMod=nmod;
		parentActivity=context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.moddedpe_nmod_load_failed);
		
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.mcd_header_bg);  
			((ImageView)findViewById(R.id.moddedpeLFHeaderBackgroundStatus)).setImageBitmap(BitmapRepeater.repeatW(parentActivity.getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.mcd_bg);  
			((ImageView)findViewById(R.id.moddedpeLFBackground)).setImageBitmap(BitmapRepeater.repeat(parentActivity.getWindowManager().getDefaultDisplay().getWidth(),parentActivity.getWindowManager().getDefaultDisplay().getHeight(),bitmap));
		}
		
		((AppCompatTextView)findViewById(R.id.moddedpeLFTitleTextView)).setText(parentActivity.getString(R.string.nmod_loadFailed,new String[]{errorNMod.getName()}));
		findViewById(R.id.moddedpenmodloadfailedMCDBurgerButtonClose).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					dismiss();
				}
				
			
		});
		
		((AppCompatTextView)findViewById(R.id.moddedpeLFWarningTextView)).setText(parentActivity.getString(R.string.load_fail_warning,new String[]{errorNMod.getName()}));
		((AppCompatTextView)findViewById(R.id.moddedpeLFMsgTextView)).setText(errorNMod.getLoadException().getBugMessage());
		((AppCompatTextView)findViewById(R.id.moddedpeLFDealTextView)).setText(errorNMod.getLoadException().getDealMessage());
		((AppCompatTextView)findViewById(R.id.moddedpeLFFullTextView)).setText(errorNMod.getLoadException().toString());
	}
}
