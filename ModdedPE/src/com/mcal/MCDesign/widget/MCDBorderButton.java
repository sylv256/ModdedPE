package com.mcal.MCDesign.widget;
import android.content.*;
import android.graphics.*;
import android.view.*;
import com.mcal.ModdedPE.*;

public class MCDBorderButton extends android.widget.Button
{
	private Bitmap bitmap;
	public MCDBorderButton(Context c)
	{
		super(c);
		init();
	}

	public MCDBorderButton(android.content.Context context, android.util.AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

    public MCDBorderButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) 
	{
		super(context, attrs, defStyleAttr);
		init();
	}

    public MCDBorderButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) 
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		if(!enabled)
			setBackgroundResId(R.drawable.mcd_button_not_important_border);
	}

	private void init()
	{
		setBackgroundResId(R.drawable.mcd_button_border);
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
		matrix.setScale(scaleWidth,scaleHeight);
		Bitmap newbm = null;
		try
		{
			newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		}
		catch (Exception e){}
		bitmap = newbm;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if( bitmap == null)
			setBackgroundResId(R.drawable.mcd_button_border);
		canvas.drawBitmap(bitmap, 0, 0, null);
		super.onDraw(canvas);
	}

	public boolean onTouchEvent(MotionEvent event)
	{

		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			setBackgroundResId(R.drawable.mcd_button_hover_border);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			setBackgroundResId(R.drawable.mcd_button_border);
		}
		return super.onTouchEvent(event);
	}
}
