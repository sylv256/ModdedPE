package com.mcal.ModdedPE.app;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.resources.*;

public class ModdedPEMenuActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_menu);

		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_header_bg);  
			((ImageView)findViewById(R.id.moddedpemenuBackgroundImage)).setImageBitmap(BitmapRepeater.createRepeater(getWindowManager().getDefaultDisplay().getWidth(),getWindowManager().getDefaultDisplay().getHeight(), bitmap));
		}
		((TextView)findViewById(R.id.moddedpemenuTitle)).setText(getTitle());
	}

	public void onCloseClicked(View v)
	{
		finish();
	}
	
	public void onManageNModPEClicked(View v)
	{
		startActivity(new Intent(this,ModdedPEManageNModPEActivity.class));
	}
	
	public void onOptionsClicked(View v)
	{
		startActivity(new Intent(this,ModdedPEOptionsActivity.class));
	}
}
