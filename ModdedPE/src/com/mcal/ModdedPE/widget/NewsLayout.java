package com.mcal.ModdedPE.widget;
import android.widget.*;
import com.mcal.ModdedPE.*;

public class NewsLayout extends RelativeLayout
{
	public NewsLayout(android.content.Context context)
	{
		super(context);
	}

    public NewsLayout(android.content.Context context, android.util.AttributeSet attrs)
	{
		super(context,attrs);
	}

    public NewsLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
	}

    public NewsLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes)
	{
		super(context,attrs,defStyleAttr,defStyleRes);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		float dividedWidth=((float)w)/1024F;
		float dividedHeight=((float)h)/500F;
		float standardSize=Math.min(dividedWidth,dividedHeight);
		getLayoutParams().width=(int)(standardSize*1024F);
		getLayoutParams().height=(int)(standardSize*500F);
		
		findViewById(R.id.moddedpemainTextViewNewsNModImage).getLayoutParams().width=getLayoutParams().width;
		findViewById(R.id.moddedpemainTextViewNewsNModTitle).getLayoutParams().width=getLayoutParams().width;
		findViewById(R.id.moddedpemainTextViewNewsNModTitleBG).getLayoutParams().width=getLayoutParams().width;
	}
}
