package com.mcal.MCDesign.widget;

import android.content.*;
import android.view.*;
import com.mcal.ModdedPE.*;

public class MCDBurgerButton extends android.widget.Button
{
	public MCDBurgerButton(Context c)
	{
		super(c);
		init();
	}

	public MCDBurgerButton(android.content.Context context, android.util.AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

    public MCDBurgerButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) 
	{
		super(context, attrs, defStyleAttr);
		init();
	}

    public MCDBurgerButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) 
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}


	private void init()
	{
		setBackgroundResId(R.drawable.mcd_burger);
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
			setBackgroundResId(R.drawable.mcd_burger_hover);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			setBackgroundResId(R.drawable.mcd_burger);
		}
		return super.onTouchEvent(event);
	}
}
