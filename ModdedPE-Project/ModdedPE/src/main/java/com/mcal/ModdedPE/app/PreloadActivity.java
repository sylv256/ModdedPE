package com.mcal.ModdedPE.app;
import android.content.*;
import android.os.*;
import com.mcal.ModdedPE.*;
import com.mcal.pesdk.nmod.*;
import android.support.v7.widget.*;
import java.util.*;
import android.text.format.*;
import com.mcal.pesdk.*;
import android.widget.*;
import android.view.*;


public class PreloadActivity extends BaseActivity
{
	private PreloadUIHandler mPreloadUIHandler = new PreloadUIHandler();
	private LinearLayout mPreloadingMessageLayout;
	private final static int MSG_START_MINECRAFT = 1;
	private final static int MSG_WRITE_TEXT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_preloading);

		AppCompatTextView tipsText = (AppCompatTextView)findViewById(R.id.moddedpe_preloading_text);
		String[] tipsArray = getResources().getStringArray(R.array.preloading_tips_text);
		Time currentTime = new Time();
		currentTime.setToNow();
		tipsText.setText(tipsArray[new Random(currentTime.toMillis(false)).nextInt(tipsArray.length)]);

		mPreloadingMessageLayout = (LinearLayout)findViewById(R.id.moddedpe_preloading_texts_adapted_layput);

		new PreloadThread().start();
	}

	private class PreloadThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();

			try
			{
				new Preloader(getPESdk(), null, new Preloader.PreloadListener()
					{
						@Override
						public void onStart()
						{
							writeNewText(getString(R.string.preloading_initing));
							if (getPESdk().getLauncherOptions().isSafeMode())
								writeNewText(getString(R.string.preloading_initing_info_safe_mode, new String[]{getPESdk().getMinecraftInfo().getMinecraftVersionName()}));
							else
								writeNewText(getString(R.string.preloading_initing_info, new String[]{getPESdk().getNModAPI().getVersionName(),getPESdk().getMinecraftInfo().getMinecraftVersionName()}));
							try
							{
								Thread.sleep(1500);
							}
							catch (InterruptedException e)
							{}
						}
						
						@Override
						public void onLoadNativeLibs()
						{
							writeNewText(getString(R.string.preloading_initing_loading_libs));
						}

						@Override
						public void onLoadSubstrateLib()
						{
							writeNewText(getString(R.string.preloading_loading_lib_substrate));
						}

						@Override
						public void onLoadGameLauncherLib()
						{
							writeNewText(getString(R.string.preloading_loading_lib_game_launcher));
						}

						@Override
						public void onLoadFModLib()
						{
							writeNewText(getString(R.string.preloading_loading_lib_fmod));
						}

						@Override
						public void onLoadMinecraftPELib()
						{
							writeNewText(getString(R.string.preloading_loading_lib_minecraftpe));
						}

						@Override
						public void onLoadNModAPILib()
						{
							writeNewText(getString(R.string.preloading_loading_lib_nmodapi));
						}
						
						@Override
						public void onFinishedLoadingNativeLibs()
						{
							writeNewText(getString(R.string.preloading_initing_loading_libs_done));
						}

						@Override
						public void onFinish(Bundle bundle)
						{
							try
							{
								Thread.sleep(1500);
							}
							catch (InterruptedException e)
							{}
							Message message = new Message();
							message.what = MSG_START_MINECRAFT;
							message.obj = bundle;
							mPreloadUIHandler.sendMessage(message);
						}

					}).preload();
			}
			catch (PreloadException e)
			{

			}
		}
	}

	@Override
	public void onBackPressed()
	{

	}

	private void writeNewText(String text)
	{
		Message message = new Message();
		message.obj = text;
		message.what = MSG_WRITE_TEXT;
		mPreloadUIHandler.sendMessage(message);
	}

	private class PreloadUIHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			if (msg.what == MSG_WRITE_TEXT)
			{
				AppCompatTextView textView = (AppCompatTextView)getLayoutInflater().inflate(R.layout.moddedpe_ui_text_small, null);
				textView.setText((CharSequence)msg.obj);
				mPreloadingMessageLayout.addView(textView);
			}
			else if (msg.what == MSG_START_MINECRAFT)
			{
				Intent intent = new Intent(PreloadActivity.this, MinecraftActivity.class);
				intent.putExtras((Bundle)msg.obj);
				startActivity(intent);
				finish();
			}
		}
	}
}
