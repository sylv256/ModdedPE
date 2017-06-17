package com.mcal.MCDesign.widget;

import android.graphics.*;
import android.widget.*;

public class MCDProgressBar extends ProgressBar
{
	public MCDProgressBar(android.content.Context context) 
	{
		super(context);
	}

    public MCDProgressBar(android.content.Context context, android.util.AttributeSet attrs)
	{
		super(context, attrs);
	}

    public MCDProgressBar(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

    public MCDProgressBar(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes)
	{
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	private int mWidth = 0;
	private int mHeight = 0;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;
	}

	private static final float mDefaultSpeed = 0.075F;
	private float mSpeed = mDefaultSpeed;

	public void setSpeed(float s)
	{
		mSpeed = s > 1 ? 1 : s;
		if (mSpeed <= 0)
			mSpeed = mDefaultSpeed;
	}

	public float getSpeed()
	{
		return mSpeed;
	}

	private float mBlockDrawingProgress =  0;
	private int mShowedBlocks = 1;
	private boolean mIsScaling = true;

	@Override
	protected void onDraw(Canvas canvas)
	{
		Paint mPaint=new Paint();
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.parseColor("#66BA44"));

		if (mIsScaling)
			mBlockDrawingProgress += (mSpeed / 2);
		else
			mBlockDrawingProgress += mSpeed;
		if (mBlockDrawingProgress >= 1 && !mIsScaling)
		{
			mBlockDrawingProgress = 0;
			++mShowedBlocks;
			if (mShowedBlocks > 4)
			{
				mShowedBlocks = 1;
				mIsScaling = true;
			}
		}
		else if (mBlockDrawingProgress >= 0.5 && mIsScaling)
		{
			mIsScaling = false;
			mBlockDrawingProgress = 0;
			mShowedBlocks = 2;
		}

		switch (mShowedBlocks)
		{
			case 1:
				{
					int drawWidth = (int) (((float)mWidth) * mBlockDrawingProgress);
					int drawHeight = (int) (((float)mHeight) * mBlockDrawingProgress);
					canvas.drawRect(0, drawHeight, mWidth - drawWidth, mHeight, mPaint);
					break;
				}
			case 2:
				{
					canvas.drawRect(0, mHeight / 2, mWidth / 2, mHeight , mPaint);
					int blockDrawHeight=(int) (((float)mHeight / 2) * mBlockDrawingProgress);
					canvas.drawRect(mWidth / 2 , blockDrawHeight, mWidth, blockDrawHeight + mHeight / 2, mPaint);
					break;
				}
			case 3:
				{
					canvas.drawRect(0, mHeight / 2, mWidth , mHeight , mPaint);
					int blockDrawHeight=(int) (((float)mHeight / 2) * mBlockDrawingProgress);
					canvas.drawRect(0, 0 , mWidth / 2, blockDrawHeight + 1 , mPaint);
					break;
				}
			case 4:
				{
					canvas.drawRect(0, mHeight / 2, mWidth , mHeight , mPaint);
					canvas.drawRect(0, 0, mWidth / 2 , mHeight / 2, mPaint);
					int blockDrawHeight=(int) (((float)mHeight / 2) * mBlockDrawingProgress);
					canvas.drawRect(mWidth / 2, 0 , mWidth , blockDrawHeight + 1 , mPaint);
					break;
				}
		}
		invalidate();
	}
}
