package org.mcal.moddedpe.utils;
import org.mcal.pesdk.*;

public class DataPreloader
{
	private PreloadingFinishedListener mListener;
	private boolean mIsSleepingFinished = false;
	private boolean mIsPreloadingFinished = false;

	public DataPreloader(PreloadingFinishedListener litenser)
	{
		mListener = litenser;
	}

	public void preload(PESdk pesdk)
	{
		final PESdk finalPESdk = pesdk;
		new Thread()
		{
			public void run()
			{
				finalPESdk.init();
				mIsPreloadingFinished = true;
				checkFinish();
			}
		}.start();

		new Thread()
		{
			public void run()
			{
				try
				{
					Thread.sleep(5000);
				}
				catch (InterruptedException e)
				{}
				mIsSleepingFinished = true;
				checkFinish();
			}
		}.start();
	}

	private void checkFinish()
	{
		if (mIsPreloadingFinished && mIsSleepingFinished)
			mListener.onPreloadingFinished();
	}

	public abstract interface PreloadingFinishedListener
	{
		void onPreloadingFinished();
	}
}
