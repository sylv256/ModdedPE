package com.mcal.MCDesign.widget;

import android.content.*;
import android.view.*;
import com.mcal.ModdedPE.*;

public class MCDCostButton extends android.widget.Button
{
	public MCDCostButton(Context c)
	{
		super(c);
		init();
	}

	public MCDCostButton(android.content.Context context, android.util.AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

    public MCDCostButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) 
	{
		super(context, attrs, defStyleAttr);
		init();
	}

    public MCDCostButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) 
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}


	private void init()
	{
		setBackgroundResId(R.drawable.mcd_cost);
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
			setBackgroundResId(R.drawable.mcd_cost_hover);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			setBackgroundResId(R.drawable.mcd_cost);
		}
		return super.onTouchEvent(event);
	}
}
