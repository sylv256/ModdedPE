package com.mcal.ModdedPE.app;
import android.content.*;
import android.os.*;
import com.mcal.ModdedPE.*;
import com.mcal.pesdk.nmod.*;
import android.support.v7.widget.*;
import java.util.*;
import android.text.format.*;
import com.mcal.pesdk.*;


public class PreloadActivity extends BaseActivity
{
	private PreStartUIHandler mPreStartUIHandler = new PreStartUIHandler();
	private static final int MSG_FINISH = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_perloading);
		
		AppCompatTextView tipsText = (AppCompatTextView)findViewById(R.id.moddedpe_perloading_text);
		String[] tipsArray = getResources().getStringArray(R.array.perloading_tips_text);
		Time currentTime = new Time();
		currentTime.setToNow();
		tipsText.setText(tipsArray[new Random(currentTime.toMillis(false)).nextInt(tipsArray.length)]);
		
		new PreStartThread().start();
	}
	
	private class PreStartThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();
			Bundle bundle = new Bundle();
			getPESdk().getGameManager().perloadForLaunch(bundle,mPreStartUIHandler);
			try
			{
				Thread.sleep(3500);
			}
			catch (InterruptedException e)
			{}
			Message msg = new Message();
			msg.obj = bundle;
			mPreStartUIHandler.sendMessage(msg);
		}
	}

	@Override
	public void onBackPressed()
	{
		
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
					Intent intent = new Intent(PreloadActivity.this,MinecraftActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtras(extras);
					startActivity(intent);
					finish();
					break;
				case PreloadingInfo.MSG_COPYING_NMOD_FILES:
					break;
				case PreloadingInfo.MSG_PERLOADING_NATIVE_LIBS:
					break;
				
			}
		}
	}
}
