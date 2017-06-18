package org.mcal.moddedpe.app;

import android.content.*;
import android.os.*;
import android.view.*;
import java.util.*;
import org.mcal.moddedpe.*;
import org.mcal.moddedpe.utils.*;

public class SplashesActivity extends BaseActivity implements DataPreloader.PreloadingFinishedListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_splashes);

		initInstance();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && Build.VERSION.SDK_INT >= 19)
		{
			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}

	private void initInstance()
	{
		new DataPreloader(this).preload(this);
		UtilsSettings settings = new UtilsSettings(this);
		switch (settings.getLanguageType())
		{
			case 0:
				getResources().getConfiguration().locale = Locale.getDefault();
				break;
			case 1:
				getResources().getConfiguration().locale = Locale.ENGLISH;
				break;
			case 2:
				getResources().getConfiguration().locale = Locale.SIMPLIFIED_CHINESE;
				break;
		}
		getResources().updateConfiguration(getResources().getConfiguration(), getResources().getDisplayMetrics());
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
