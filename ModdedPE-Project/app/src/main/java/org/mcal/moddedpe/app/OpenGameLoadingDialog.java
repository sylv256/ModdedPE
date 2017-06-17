package org.mcal.moddedpe.app;
import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.pesdk.nativeapi.*;

public class OpenGameLoadingDialog extends AppCompatDialog
{
	public OpenGameLoadingDialog(Context context)
	{
		super(context, R.style.FullScreenTheme);

		setCancelable(false);
	}

	@Override
	public void setCancelable(boolean flag)
	{
		super.setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		View contentView=getLayoutInflater().inflate(R.layout.moddedpe_loading, null);
		setContentView(contentView);

		setLoadingMessage(getContext().getString(R.string.opengame_loading));

		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					while (!NativeUtils.nativeIsGameStarted())
						Thread.sleep(100);
				}
				catch (InterruptedException e)
				{}
				dismiss();
			}
		}.start();
	}

	public void setLoadingMessage(String msg)
	{
		((TextView)findViewById(R.id.moddedpe_loading_message)).setText(msg);
	}
}
