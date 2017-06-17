package org.mcal.moddedpe.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.mcal.moddedpe.utils.DataPreloader;
import org.mcal.moddedpe.R;

public class SplashesActivity extends BaseActivity implements DataPreloader.PreloadingFinishedListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_splashes);

		initInstance();
	}

	private void initInstance()
	{
		new DataPreloader(this).preload(getPESdk());
	}

	Handler mHandler=new Handler()
	{
		
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			Intent intent=new Intent(SplashesActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	};

	@Override
	public void onPreloadingFinished()
	{
		mHandler.sendEmptyMessage(0);
	}
}
