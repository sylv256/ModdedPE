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
	private ArchiveFailedException mFailedInfo;
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
	
	public void onFailedViewMoreClicked(View view)
	{
		setContentView(R.layout.nmod_importer_failed);
		AppCompatTextView errorText = (AppCompatTextView)findViewById(R.id.nmod_importer_failed_text_view);
		errorText.setText(getString(R.string.nmod_import_failed_full_info_message, new Object[]{mFailedInfo.toTypeString(),mFailedInfo.getCause().toString()}));
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
				setContentView(R.layout.nmod_importer_failed_msg);
				mFailedInfo = (ArchiveFailedException)msg.obj;
				setTitle(R.string.nmod_import_failed);
				AppCompatTextView errorText = (AppCompatTextView)findViewById(R.id.nmod_import_failed_title_text_view);
				switch (mFailedInfo.getType())
				{
					case ArchiveFailedException.TYPE_DECODE_FAILED:
						errorText.setText(R.string.nmod_import_failed_message_decode);
						break;
					case ArchiveFailedException.TYPE_INEQUAL_PACKAGE_NAME:
						errorText.setText(R.string.nmod_import_failed_message_inequal_package_name);
						break;
					case ArchiveFailedException.TYPE_INVAILD_PACKAGE_NAME:
						errorText.setText(R.string.nmod_import_failed_message_invalid_package_name);
						break;
					case ArchiveFailedException.TYPE_IO_EXCEPTION:
						errorText.setText(R.string.nmod_import_failed_message_io_exception);
						break;
					case ArchiveFailedException.TYPE_JSON_SYNTAX_EXCEPTION:
						errorText.setText(R.string.nmod_import_failed_message_manifest_json_syntax_error);
						break;
					case ArchiveFailedException.TYPE_NO_MANIFEST:
						errorText.setText(R.string.nmod_import_failed_message_no_manifest);
						break;
					case ArchiveFailedException.TYPE_UNDEFINED_PACKAGE_NAME:
						errorText.setText(R.string.nmod_import_failed_message_no_package_name);
						break;
					case ArchiveFailedException.TYPE_REDUNDANT_MANIFEST:
						errorText.setText(R.string.nmod_import_failed_message_no_package_name);
						break;
					default:
						errorText.setText(R.string.nmod_import_failed_message_unexpected);
						break;
				}
			}
		}
	}
}
