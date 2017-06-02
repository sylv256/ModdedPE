package com.mcal.ModdedPE.app;
import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import com.mcal.MCDesign.app.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.utils.*;
import com.mcal.ModdedPE.nativeapi.*;

public class ModdedPEStartActivity extends ModdedPEActivity 
{
	private static final int SLEEP_TIME_MIN = 5000;
	private static final int MSG_NEXT = 1;
	private static final int MSG_SET_FINISHED_MIN = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_start);

		initInstance();
	}

	private void initInstance()
	{
		new Thread()
		{
			public void run()
			{
				LibraryLoader.loadLocalLibs();
				getNModAPI().initNModDatas();
				mHandler.sendEmptyMessage(MSG_NEXT);
			}
		}.start();

		new Thread()
		{
			public void run()
			{
				try
				{
					Thread.sleep(SLEEP_TIME_MIN);
				}
				catch (InterruptedException e)
				{}
				mHandler.sendEmptyMessage(MSG_SET_FINISHED_MIN);
			}
		}.start();
	}

	Handler mHandler=new Handler()
	{
		private boolean isMinFinished = false;
		private boolean isFinishedIniting = false;

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			if (msg.what == MSG_SET_FINISHED_MIN)
			{
				isMinFinished = true;
			}
			else if (msg.what == MSG_NEXT)
			{
				isFinishedIniting = true;
			}

			if (isFinishedIniting && isMinFinished)
			{
				Intent intent=new Intent(ModdedPEStartActivity.this, ModdedPEMainActivity.class);
				startActivity(intent);
				finish();
			}
		}
	};
}
