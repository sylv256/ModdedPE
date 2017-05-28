package com.mcal.ModdedPE.widget;
import android.widget.*;
import android.annotation.*;
import android.view.*;
import android.support.v4.view.*;
import java.util.*;
import android.content.*;
import android.util.*;
import android.os.*;
import com.mcal.ModdedPE.nmod.*;
import com.mcal.ModdedPE.*;
import android.support.v7.widget.*;
import com.mcal.ModdedPE.app.*;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class NModBanner extends RelativeLayout
{
	private ViewPager mPager;
	private BannerHandler mHandler;
	private BannerThread mBannerThread = new BannerThread();
	private Vector<NMod> mNModVector = new Vector<NMod>();
	
	private static final int SLEEP_TIME = 3500;
	private static final int MSG_UPDATE_NMODS = 1;
	private static final int MSG_DO_SCROLL = 2;

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

	public NModBanner(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init();
	}

	private void updateNModList()
	{
		Vector<NMod> newNModList = NModAPI.getInstance(this.getContext()).getLoadedEnabledNModsHaveBanners();
		if (mNModVector.equals(newNModList))
		{
			mNModVector.clear();
			mNModVector.addAll(newNModList);
		}
		mPager.setAdapter(new BannerItemAdapater());
	}

	private void init()
	{
		mPager = new ViewPager(getContext());
		updateNModList();
		addView(mPager);
	}

	@SuppressLint("HandlerLeak")
	private class BannerHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == MSG_UPDATE_NMODS)
			{
				updateNModList();
			}
			else if (msg.what == MSG_DO_SCROLL)
			{
				scrollToNext();
			}
		}
	}

	public void scrollToNext()
	{
		int index = mPager.getCurrentItem() + 1;
		mPager.setCurrentItem(index % (Math.max(mNModVector.size(), 1)));
	}

	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		mBannerThread.start();
	}

	@Override
	protected void onDetachedFromWindow()
	{
		mBannerThread.setStopped();
		super.onDetachedFromWindow();
	}

	private RelativeLayout createEmptyBannerItem()
	{
		return (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_nmod_banner_item, null);
	}

	private RelativeLayout createBannerItemFor(NMod nmod_for)
	{
		final NMod nmod = nmod_for;
		RelativeLayout view = createEmptyBannerItem();
		AppCompatImageView image = (AppCompatImageView)view.findViewById(R.id.moddedpe_nmod_banner_item_image_view);
		image.setImageBitmap(nmod.getBannerImage());
		AppCompatTextView bannerTitle = (AppCompatTextView)view.findViewById(R.id.moddedpe_nmod_banner_item_text_view_title);
		bannerTitle.setText(nmod.getBannerTitle());
		view.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					ModdedPENModDescriptionActivity.startThisActivity(getContext(),nmod);
				}
				
			
		});
		return view;
	}


	private class BannerItemAdapater extends PagerAdapter
	{
		@Override
		public int getCount()
		{
			return Math.max(mNModVector.size(), 1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View)object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			if (mNModVector.size() <= 0)
			{
				View view = createEmptyBannerItem();
				container.addView(view);
				return view;
			}
			View view = createBannerItemFor(mNModVector.get(position));
			container.addView(view);
			return view;
		}
	}

	private class BannerThread extends Thread
	{
		private boolean stopped = false;

		@Override
		public void run()
		{
			while (!stopped)
			{
				try
				{
					Thread.sleep(SLEEP_TIME);
				}
				catch (InterruptedException e)
				{}

				try
				{
					if (!stopped)
						mHandler.sendEmptyMessage(MSG_UPDATE_NMODS);
					if (!stopped)
						mHandler.sendEmptyMessage(MSG_DO_SCROLL);
				}
				catch (Throwable t)
				{}
			}
		}

		public void setStopped()
		{
			stopped = true;
		}
	}
}
