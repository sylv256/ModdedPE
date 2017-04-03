package com.mcal.ModdedPE.app;
import android.app.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmodpe.*;
import com.mcal.ModdedPE.resources.*;

public class LoadErrorDialog extends Dialog
{
	private NModPE errorNModPE;
	private ModdedPEMinecraftActivity parentActivity;
	private Throwable errorThrowable;
	public LoadErrorDialog(ModdedPEMinecraftActivity context,Throwable t,NModPE nmodpe)
	{
		super(context,R.style.AppTheme);
		
		errorNModPE=nmodpe;
		parentActivity=context;
		errorThrowable=t;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.moddedpe_nmodpe_load_failed);
		
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.mcd_header_bg);  
			((ImageView)findViewById(R.id.moddedpeLFHeaderBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(parentActivity.getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.mcd_bg);  
			((ImageView)findViewById(R.id.moddedpeLFBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(parentActivity.getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		
		((AppCompatTextView)findViewById(R.id.moddedpeLFTitleTextView)).setText(errorNModPE.getName()+"  "+parentActivity.getString(R.string.loadFailed));
		findViewById(R.id.moddedpenmodpeloadfailedMCDBurgerButtonClose).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					dismiss();
				}
				
			
		});
		
		((AppCompatTextView)findViewById(R.id.moddedpeLFMsgTextView)).setText(errorThrowable.toString());
	}
}
