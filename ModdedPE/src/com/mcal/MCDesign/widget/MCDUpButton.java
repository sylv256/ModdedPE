package com.mcal.MCDesign.widget;

import android.content.*;
import android.graphics.*;
import android.view.*;
import com.mcal.ModdedPE.*;

public class MCDUpButton extends android.widget.Button
{
	private Bitmap bitmap;

	public MCDUpButton(Context c)
	{
		super(c);
		init();
	}

	public MCDUpButton(android.content.Context context, android.util.AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

    public MCDUpButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) 
	{
		super(context, attrs, defStyleAttr);
		init();
	}

    public MCDUpButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) 
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init()
	{
		setBackgroundResId(R.drawable.mcd_expand_button4);
	}

	private void setBackgroundResId(int id)
	{
		bitmap = BitmapFactory.decodeResource(getContext().getResources(), id);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int newWidth = getWidth();
		int newHeight = getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.setScale(scaleWidth, scaleHeight);
		Bitmap newbm = null;
		try
		{
			newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		}
		catch (Exception e)
		{}
		bitmap = newbm;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if (bitmap == null)
			setBackgroundResId(R.drawable.mcd_expand_button4);
		if(!super.isClickable())
			setBackgroundResId(R.drawable.mcd_expand_button4_mo);
		canvas.drawBitmap(bitmap, 0, 0, null);
		super.onDraw(canvas);
	}

	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			setBackgroundResId(R.drawable.mcd_expand_button4_md);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			setBackgroundResId(R.drawable.mcd_expand_button4);
		}
		return super.onTouchEvent(event);
	}
}
