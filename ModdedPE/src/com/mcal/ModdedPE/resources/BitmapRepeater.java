package com.mcal.ModdedPE.resources;

import android.graphics.*;

public class BitmapRepeater
{
	public static Bitmap createRepeaterW(int width, Bitmap src)
	{
		int count = (width + src.getWidth() - 1) / src.getWidth() + 1;
		Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		for (int idx = 0; idx < count; ++idx)
		{
			if (idx + 1 == count)
				canvas.drawBitmap(src, width, 0, null);
			else
				canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
		}
		return bitmap;
	}

	public static Bitmap createRepeaterH(int height, Bitmap src)
	{
		int count = (height + src.getHeight() - 1) / src.getHeight() + 1;
		Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		for (int idx = 0; idx < count; ++idx)
		{
			if (idx + 1 == count)
				canvas.drawBitmap(src, 0, height, null);
			else
				canvas.drawBitmap(src, 0, idx * src.getHeight(), null);
		}
		return bitmap;
	}

	public static Bitmap createRepeater(int width, int height, Bitmap src)
	{
		Bitmap ret = createRepeaterW(width, src);
		ret = createRepeaterH(height, ret);
		return ret;
	}
}
