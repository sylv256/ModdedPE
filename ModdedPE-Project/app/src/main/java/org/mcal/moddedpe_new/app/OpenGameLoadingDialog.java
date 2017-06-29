package org.mcal.moddedpe_new.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.mcal.moddedpe_new.R;
import org.mcal.pesdk.nativeapi.NativeUtils;

class OpenGameLoadingDialog extends AppCompatDialog
{
	OpenGameLoadingDialog(Context context)
	{
		super(context, R.style.FullScreenTheme);

		setCancelable(false);
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
				try
				{
					while (!NativeUtils.nativeIsGameStarted())
						Thread.sleep(100);
				}
				catch (InterruptedException e)
				{}
				dismiss();
			}
		}.start();
	}

	void setLoadingMessage(String msg)
	{
		((TextView)findViewById(R.id.moddedpe_loading_message)).setText(msg);
	}
}
