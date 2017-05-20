package com.mcal.ModdedPE.app;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.mcal.MCDesign.utils.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nativeapi.*;
public class OpenGameLoadingDialog extends Dialog
{
	public OpenGameLoadingDialog(Context context)
	{
		super(context, R.style.FullScreenTheme);

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
		View contentView=getLayoutInflater().inflate(R.layout.moddedpe_loading, null);
		setContentView(contentView);

		setLoadingMessage(getContext().getString(R.string.opengame_loading));

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
