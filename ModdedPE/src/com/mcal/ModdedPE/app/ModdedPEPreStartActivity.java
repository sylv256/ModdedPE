package com.mcal.ModdedPE.app;
import com.mcal.MCDesign.app.*;
import android.os.*;

public class ModdedPEPreStartActivity extends ModdedPEActivity
{
	private static final int MSG_START_MINECRAFT = 1;
	private static final int MSG_MERGING_ASSETS = 2;
	private static final int MSG_COPYING_LIBS = 3;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}
	
	private class PreStartThread extends Thread
	{
		@Override
		public void run()
		{
			
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
