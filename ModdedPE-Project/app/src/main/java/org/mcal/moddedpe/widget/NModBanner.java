package org.mcal.moddedpe.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.mcal.moddedpe.ModdedPEApplication;
import org.mcal.moddedpe.R;
import org.mcal.moddedpe.app.NModDescriptionActivity;
import org.mcal.pesdk.nmod.NMod;

import java.util.ArrayList;
import java.util.Random;
import android.view.ViewGroup.*;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class NModBanner extends RelativeLayout
{
	private RelativeLayout mBannerView;
	private Random mRandom = new Random();
	private ArrayList<NMod> mNModArrayList = new ArrayList<NMod>();

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
		ArrayList<NMod> newNModList = ModdedPEApplication.mPESdk.getNModAPI().getImportedEnabledNModsHaveBanners();
		if (mNModArrayList.isEmpty() || !mNModArrayList.equals(newNModList))
		{
			mNModArrayList.clear();
			mNModArrayList.addAll(newNModList);
			if (newNModList.size() > 0)
				mBannerView = createBannerItemFor(newNModList.get(mRandom.nextInt(newNModList.size())));
			removeAllViews();
			addView(mBannerView);
		}
		invalidate();
	}

	private void init()
	{
		mBannerView = createEmptyBannerItem();
		addView(mBannerView);
		updateNModList();
		setWillNotDraw(false);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		updateNModList();
	}

	@Override
	public void addView(View child)
	{
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		super.addView(child, params);
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
					NModDescriptionActivity.startThisActivity(getContext(), nmod);
				}


			});
		return view;
	}
}
