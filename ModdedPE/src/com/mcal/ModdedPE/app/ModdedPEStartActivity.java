package com.mcal.ModdedPE.app;
import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import com.mcal.MCDesign.app.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.utils.*;

public class ModdedPEStartActivity extends MCDActivity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_start);
		
		final UtilsSettings settings=new UtilsSettings(this);

		Context xposedContext = null;
		try
		{
			xposedContext = createPackageContext("de.robv.android.xposed.installer", Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
		}
		catch (Throwable e)
		{}
		if (xposedContext == null)
		{
			initInstance();
			return;
		}

		//Found Xposed
		if (settings.getDoNotShowXposedWarning())
		{
			initInstance();
			return;
		}
		else
		{
			new AlertDialog.Builder(this).setTitle(R.string.start_warning_xposed_title)
				.setMessage(R.string.start_warning_xposed_message)
				.setPositiveButton(R.string.start_warning_xposed_continue, new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						p1.dismiss();
						initInstance();
					}


				})
				.setNeutralButton(R.string.start_warning_xposed_do_not_show_again, new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						p1.dismiss();
						initInstance();
						settings.setDoNotShowXposedWarning(true);
					}


				}).setOnCancelListener(new DialogInterface.OnCancelListener()
				{

					@Override
					public void onCancel(DialogInterface p1)
					{
						p1.dismiss();
						initInstance();
					}


				}).show();
		}
	}

	private void initInstance()
	{
		new Thread()
		{
			public void run()
			{
				ModdedPEApplication.instance.init();
				mHandler.sendEmptyMessage(0);
			}
		}.start();
	}

	Handler mHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			Intent intent=new Intent(ModdedPEStartActivity.this, ModdedPEMainActivity.class);
			startActivity(intent);

			finish();
		}
	};
}
