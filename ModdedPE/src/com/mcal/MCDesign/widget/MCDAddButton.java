package com.mcal.MCDesign.widget;

import android.content.*;
import android.view.*;
import com.mcal.ModdedPE.*;

public class MCDAddButton extends android.widget.Button
{
	public MCDAddButton(Context c)
	{
		super(c);
		init();
	}

	public MCDAddButton(android.content.Context context, android.util.AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

    public MCDAddButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) 
	{
		super(context, attrs, defStyleAttr);
		init();
	}

    public MCDAddButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) 
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}


	private void init()
	{
		setBackgroundResId(R.drawable.mcd_add);
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
			setBackgroundResId(R.drawable.mcd_add_hover);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			setBackgroundResId(R.drawable.mcd_add);
		}
		return super.onTouchEvent(event);
	}
}
