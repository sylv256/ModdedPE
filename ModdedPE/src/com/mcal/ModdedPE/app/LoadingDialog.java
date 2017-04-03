package com.mcal.ModdedPE.app;
import android.app.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nativeapi.*;
import com.mcal.ModdedPE.resources.*;
public class LoadingDialog extends Dialog
{
	private Activity mActivity = null;
	
	public LoadingDialog(Activity context)
	{
		super(context, R.style.FullScreenTheme);
		mActivity=context;
		setCancelable(false);
	}

	public void setLoadingMessageColor(int color)
	{
		((TextView)findViewById(R.id.moddedpeloadingMessageTextView)).setTextColor(color);
	}

	@Override
	public void setCancelable(boolean flag)
	{
		super.setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_loading);
		
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.mcd_bg);  
			((ImageView)findViewById(R.id.moddedpeloadingImageViewBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(mActivity.getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		
		setLoadingMessage(getContext().getString(R.string.loading));
		
		new Thread()
		{
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						Thread.sleep(50);
					}
					catch (Exception e)
					{}

					if (Utils.nativeIsGameStarted())
						dismiss();
				}
			}
		}.start();
	}
	
	public void setLoadingMessage(String msg)
	{
		((TextView)findViewById(R.id.moddedpeloadingMessageTextView)).setText(msg);
	}
}
