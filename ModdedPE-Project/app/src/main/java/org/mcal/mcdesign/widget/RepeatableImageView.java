package org.mcal.mcdesign.widget;
import android.support.v7.widget.*;
import android.content.*;
import android.util.*;
import android.graphics.drawable.*;
import android.graphics.*;
import org.mcal.mcdesign.utils.*;

public class RepeatableImageView extends AppCompatImageView
{
	public RepeatableImageView(Context context)
	{
		super(context);
	}
	public RepeatableImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	public RepeatableImageView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);

		Drawable image = getDrawable();
		if (image != null)
		{
			Bitmap bitmap=drawableToBitmap(image);
			bitmap = BitmapRepeater.repeat(w, h, bitmap);
			setImageBitmap(bitmap);
		}
	}

	private Bitmap drawableToBitmap(Drawable drawable)
	{
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
}
