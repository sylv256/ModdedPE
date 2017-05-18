package com.mcal.ModdedPE.widget;
import android.graphics.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;

public class NewsView extends RelativeLayout
{
	public NewsView(android.content.Context context)
	{
		super(context);

		addView(LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_news, null));
		NModManager.getNModManager(getContext()).resetShowingNMod();
		NModManager.getNModManager(getContext()).refreshShowingNModView(this);
	}

    public NewsView(android.content.Context context, android.util.AttributeSet attrs)
	{
		super(context, attrs);

		addView(LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_news, null));
		NModManager.getNModManager(getContext()).resetShowingNMod();
		NModManager.getNModManager(getContext()).refreshShowingNModView(this);
	}

    public NewsView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		addView(LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_news, null));
		NModManager.getNModManager(getContext()).resetShowingNMod();
		NModManager.getNModManager(getContext()).refreshShowingNModView(this);
	}

    public NewsView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes)
	{
		super(context, attrs, defStyleAttr, defStyleRes);

		addView(LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_news, null));
		NModManager.getNModManager(getContext()).resetShowingNMod();
		NModManager.getNModManager(getContext()).refreshShowingNModView(this);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		float dividedWidth=((float)getWidth()) / 1024F;
		float dividedHeight=((float)getHeight()) / 500F;
		float standardSize=Math.min(dividedWidth, dividedHeight);
		getLayoutParams().width = (int)(standardSize * 1024F);
		getLayoutParams().height = (int)(standardSize * 500F);
		super.onDraw(canvas);
	}
	public void setNewsTitle(String title)
	{
		((AppCompatTextView)findViewById(R.id.moddedpeTextViewNewsNModTitle)).setText(title);
		
	}

	public void setNewsImage(Bitmap image)
	{
		((AppCompatImageView)findViewById(R.id.moddedpeNewsNModImage)).setImageBitmap(image);
		
	}
}
