package com.mcal.MCDesign.widget;

import android.content.*;
import android.support.v7.widget.*;
import android.view.*;
import com.mcal.ModdedPE.*;

public class MCDBurgerButton extends AppCompatButton
{
	public MCDBurgerButton(Context c)
	{
		super(c);
		setBackgroundResource(R.drawable.mcd_burger);
	}

	public MCDBurgerButton(android.content.Context context, android.util.AttributeSet attrs)
	{
		super(context, attrs);
		setBackgroundResource(R.drawable.mcd_burger);
	}

    public MCDBurgerButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) 
	{
		super(context, attrs, defStyleAttr);
		setBackgroundResource(R.drawable.mcd_burger);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			setBackgroundResource(R.drawable.mcd_burger_hover);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			setBackgroundResource(R.drawable.mcd_burger);
		}
		return super.onTouchEvent(event);
	}
}
