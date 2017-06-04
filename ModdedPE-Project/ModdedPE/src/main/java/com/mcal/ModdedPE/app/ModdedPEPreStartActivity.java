package com.mcal.ModdedPE.app;
import android.content.*;
import android.os.*;
import com.mcal.ModdedPE.*;
import com.mcal.pesdk.nmod.*;

public class ModdedPEPreStartActivity extends ModdedPEActivity
{
	private PreStartUIHandler mPreStartUIHandler = new PreStartUIHandler();
	private static final int MSG_FINISH = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_perloading);
		new PreStartThread().start();
	}
	
	private class PreStartThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();
			/*Bundle bundle = new Bundle();
			getPESdk().getGameManager().perloadForLaunch(bundle,mPreStartUIHandler);
			try
			{
				Thread.sleep(3500);
			}
			catch (InterruptedException e)
			{}
			Message msg = new Message();
			msg.obj = bundle;
			mPreStartUIHandler.sendMessage(msg);*/
			while(true);
		}
	}
	
	private class PreStartUIHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch(msg.what)
			{
				case MSG_FINISH:
					Bundle extras = (Bundle)msg.obj;
					Intent intent = new Intent(ModdedPEPreStartActivity.this,ModdedPEMinecraftActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtras(extras);
					startActivity(intent);
					finish();
					break;
				case NModAPI.MSG_COPYING_NMOD_FILES:
					break;
				case NModAPI.MSG_PERLOADING_NATIVE_LIBS:
					break;
				
			}
		}
	}
}
