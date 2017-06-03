package com.mcal.ModdedPE.app;
import com.mcal.MCDesign.app.*;
import android.os.*;

public class ModdedPEPreStartActivity extends ModdedPEActivity
{
	private PreStartUIHandler mPreStartUIHandler = new PreStartUIHandler();
	private static final int MSG_FINISH = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		new PreStartThread().start();
	}
	
	private class PreStartThread extends Thread
	{
		@Override
		public void run()
		{
			Bundle bundle = new Bundle();
			getNModAPI().perloadNMods(bundle,mPreStartUIHandler);
			mPreStartUIHandler.sendEmptyMessage(MSG_FINISH);
		}
	}
	
	private class PreStartUIHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
		}
	}
}
