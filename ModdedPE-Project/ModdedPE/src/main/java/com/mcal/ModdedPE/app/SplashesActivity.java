package com.mcal.ModdedPE.app;
import android.content.*;
import android.os.*;
import com.mcal.ModdedPE.*;
import com.mcal.pesdk.nativeapi.*;
import com.mcal.ModdedPE.utils.*;

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
