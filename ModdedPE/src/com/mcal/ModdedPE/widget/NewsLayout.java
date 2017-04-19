package com.mcal.ModdedPE.widget;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;
import android.support.v7.widget.*;
import android.graphics.*;
import android.view.*;

public class NewsLayout extends RelativeLayout
{
	public NewsLayout(android.content.Context context)
	{
		super(context);
		
		addView(LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_news,null));
	}

    public NewsLayout(android.content.Context context, android.util.AttributeSet attrs)
	{
		super(context,attrs);
		
		addView(LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_news,null));
	}

    public NewsLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
		
		addView(LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_news,null));
	}

    public NewsLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes)
	{
		super(context,attrs,defStyleAttr,defStyleRes);
		
		addView(LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_news,null));
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		float dividedWidth=((float)w)/1024F;
		float dividedHeight=((float)h)/500F;
		float standardSize=Math.min(dividedWidth,dividedHeight);
		getLayoutParams().width=(int)((standardSize*1024F)*0.95F);
		getLayoutParams().height=(int)((standardSize*500F)*0.95F);
		
		findViewById(R.id.moddedpeNewsNModImage).getLayoutParams().width=getLayoutParams().width;
		findViewById(R.id.moddedpeTextViewNewsNModTitle).getLayoutParams().width=getLayoutParams().width;
		findViewById(R.id.moddedpeNewsNModTitleBG).getLayoutParams().width=getLayoutParams().width;
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
