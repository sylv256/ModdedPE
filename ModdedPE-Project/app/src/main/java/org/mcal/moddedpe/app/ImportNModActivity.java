package org.mcal.moddedpe.app;
import android.content.*;
import android.net.*;
import android.os.*;
import org.mcal.moddedpe.*;
import java.io.*;
import java.net.*;
import org.mcal.pesdk.nmod.*;
import android.view.*;
import android.support.v7.widget.*;

public class ImportNModActivity extends BaseActivity
{
	private UIHandler mUIHandler = new UIHandler();
	private NMod mTargetNMod;
	private static final int MSG_SUCCEED = 1;
	private static final int MSG_FAILED = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nmod_importer_loading);
		setActionBarButtonCloseRight();
		setTitle(R.string.import_nmod_title);

		File targetFile = getTargetNModFile();
		new ImportThread(targetFile).start();
	}

	private File getTargetNModFile()
	{
		try
		{
			Intent intent = getIntent();
			Uri uri = intent.getData();
			return new File(new URI(uri.toString()));
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void onViewMoreClicked(View view)
	{
		NModDescriptionActivity.startThisActivity(this, mTargetNMod);
	}
	
	public void onViewMoreErrorClicked(View view)
	{
		
	}
	

	private class ImportThread extends Thread
	{
		private File mTargetFile;

		public ImportThread(File file)
		{
			mTargetFile = file;
		}

		@Override
		public void run()
		{
			super.run();

			try
			{
				ZippedNMod zippedNMod = getPESdk().getNModAPI().archiveZippedNMod(mTargetFile.getAbsolutePath());
				getPESdk().getNModAPI().importNMod(zippedNMod);
				Message msg = new Message();
				msg.what = MSG_SUCCEED;
				msg.obj = zippedNMod;
				mUIHandler.sendMessage(msg);
			}
			catch (ArchiveFailedException archiveFailedException)
			{
				Message msg = new Message();
				msg.what = MSG_FAILED;
				msg.obj = archiveFailedException;
				mUIHandler.sendMessage(msg);
			}
		}
	}

	private class UIHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == MSG_SUCCEED)
			{
				setContentView(R.layout.nmod_importer_succeed);
				mTargetNMod = (NMod)msg.obj;
			}
			else if (msg.what == MSG_FAILED)
			{
				setContentView(R.layout.nmod_importer_failed);
				ArchiveFailedException archiveFailedException = (ArchiveFailedException)msg.obj;
				setTitle(R.string.nmod_import_failed);
				AppCompatTextView errorText = (AppCompatTextView)findViewById(R.id.nmod_importer_failed_text_view);
				errorText.setText(getString(R.string.nmod_import_failed_full_info_message, new Object[]{archiveFailedException.toTypeString(),archiveFailedException.getCause().toString()}));
			}
		}
	}
}
