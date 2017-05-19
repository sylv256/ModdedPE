package com.mcal.ModdedPE.widget;
import android.widget.*;
import android.annotation.*;
import android.view.*;
import android.support.v4.view.*;
import java.util.*;
import android.content.*;
import android.util.*;
import android.os.*;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class NModBanner extends RelativeLayout
{
	private ViewPager mPager;
	private List<ImageView> mViewList = new ArrayList<ImageView>();
	private RadioGroup mGroup;
	private int mCount;
	private LayoutInflater mInflater;
	private BannerHandler mHandler;
	private int dip_13;
	private static int mInterval = 3000;

	public NModBanner(Context context)
	{
		super(context);
		init();
	}

	public NModBanner(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}
	
	public void start()
	{
		mHandler.sendEmptyMessageDelayed(0, mInterval);
	}

	public void setImage(ArrayList<Integer> imageList)
	{
		/*for (int i = 0; i < imageList.size(); i++)
		{
			Integer imageID = imageList.get(i).intValue();
			ImageView ivNew = new ImageView(getContext());
			ivNew.setLayoutParams(new LayoutParams(
									  LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			ivNew.setScaleType(ImageView.ScaleType.FIT_XY);
			ivNew.setImageResource(imageID);
			ivNew.setOnClickListener(this);
			mViewList.add(ivNew);
		}
		mPager.setAdapter(new ImageAdapater());
		mPager.setOnPageChangeListener(new BannerChangeListener(this));

		mCount = imageList.size();
		for (int i = 0; i < mCount; i++)
		{
			RadioButton radio = new RadioButton(mContext);
			radio.setLayoutParams(new RadioGroup.LayoutParams(dip_13, dip_13));
			radio.setGravity(Gravity.CENTER);
			radio.setButtonDrawable(R.drawable.rbt_selector);
			mGroup.addView(radio);
		}

		mPager.setCurrentItem(0);
		setButton(0);
		mHandler = new BannerHandler();
		start();*/
	}

	private void setButton(int position)
	{
		((RadioButton) mGroup.getChildAt(position)).setChecked(true);
	}

	private void init()
	{
		/*
		mInflater = LayoutInflater.from(getContext());
		View view = mInflater.inflate(R.layout.banner_pager, null);
		mPager = (ViewPager) view.findViewById(R.id.banner_pager);
		mGroup = (RadioGroup) view.findViewById(R.id.banner_pager_group);
		addView(view);
		dip_13 = Utils.dip2px(mContext, 13);
		*/
	}

	public boolean dispatchTouchEvent(MotionEvent event)
	{
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(event);
	}

	@SuppressLint("HandlerLeak")
	private class BannerHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg)
		{
			scrollToNext();
			sendEmptyMessageDelayed(0, mInterval);
		}
	}

	public void scrollToNext()
	{
		int index = mPager.getCurrentItem() + 1;
		if (mViewList.size() <= index)
		{
			index = 0;
		}
		mPager.setCurrentItem(index);
	}

	private class ImageAdapater extends PagerAdapter
	{

		@Override
		public int getCount()
		{
			return mViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView(mViewList.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			container.addView(mViewList.get(position));
			return mViewList.get(position);
		}

	}
}
