package com.mcal.MCDesign.widget;

import android.graphics.*;
import android.widget.*;
import com.mcal.ModdedPE.*;

public class MCDSwitch extends Switch
{
	Bitmap bitmap;
	Bitmap bitmapClicked;
	Bitmap bitmapNI;
	
	public MCDSwitch(android.content.Context context) 
	{
		super(context);
	}

    public MCDSwitch(android.content.Context context, android.util.AttributeSet attrs) 
	{
		super(context,attrs);
	}

    public MCDSwitch(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) 
	{
		super(context,attrs,defStyleAttr);
	}

    public MCDSwitch(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes)
	{
		super(context,attrs,defStyleAttr,defStyleRes);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if(bitmap==null||bitmapClicked==null)
		{
			bitmap=BitmapFactory.decodeResource(getContext().getResources(),R.drawable.mcd_checkbox_default);
			bitmapClicked=BitmapFactory.decodeResource(getContext().getResources(),R.drawable.mcd_checkbox_checked);
			bitmapNI=BitmapFactory.decodeResource(getContext().getResources(),R.drawable.mcd_checkbox_not_important);
			
		}
		if(!super.isClickable())
			canvas.drawBitmap(bitmapNI, 0, 0, null);
		else if (super.isChecked())
			canvas.drawBitmap(bitmapClicked, 0, 0, null);
		else
			canvas.drawBitmap(bitmap, 0, 0, null);
		invalidate();
	}
	
	
}
