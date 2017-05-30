package com.mcal.MCDesign.widget;

import android.content.*;
import android.view.*;
import com.mcal.ModdedPE.*;

public class MCDBurgerButtonClose extends android.widget.Button
{
	public MCDBurgerButtonClose(Context c)
	{
		super(c);
		init();
	}

	public MCDBurgerButtonClose(android.content.Context context, android.util.AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

    public MCDBurgerButtonClose(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) 
	{
		super(context, attrs, defStyleAttr);
		init();
	}

    public MCDBurgerButtonClose(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) 
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}


	private void init()
	{
		setBackgroundResId(R.drawable.mcd_burger_close);
	}

	private void setBackgroundResId(int id)
	{
		super.setBackgroundResource(id);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			setBackgroundResId(R.drawable.mcd_burger_close_hover);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			setBackgroundResId(R.drawable.mcd_burger_close);
		}
		return super.onTouchEvent(event);
	}
}
