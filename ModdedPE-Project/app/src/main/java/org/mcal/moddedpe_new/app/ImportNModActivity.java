package org.mcal.moddedpe_new.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import org.mcal.moddedpe_new.R;
import org.mcal.pesdk.nmod.ExtractFailedException;
import org.mcal.pesdk.nmod.NMod;
import org.mcal.pesdk.nmod.ZippedNMod;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class ImportNModActivity extends BaseActivity
{
	private UIHandler mUIHandler = new UIHandler();
	private NMod mTargetNMod;
	private ExtractFailedException mFailedInfo;
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

	private void onViewMoreClicked()
	{
		NModDescriptionActivity.startThisActivity(this, mTargetNMod);
	}
	
	private void onFailedViewMoreClicked()
	{
		setContentView(R.layout.nmod_importer_failed);
		AppCompatTextView errorText = (AppCompatTextView)findViewById(R.id.nmod_importer_failed_text_view);
		errorText.setText(getString(R.string.nmod_import_failed_full_info_message, new Object[]{mFailedInfo.toTypeString(),mFailedInfo.getCause().toString()}));
	}
	

	private class ImportThread extends Thread
	{
		private File mTargetFile;

		ImportThread(File file)
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
			catch (ExtractFailedException archiveFailedException)
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
				findViewById(R.id.import_succeed_view_more_button).setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						onViewMoreClicked();
					}


				});
				mTargetNMod = (NMod)msg.obj;
			}
			else if (msg.what == MSG_FAILED)
			{
				setContentView(R.layout.nmod_importer_failed_msg);
				findViewById(R.id.import_failed_view_more_button).setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						onFailedViewMoreClicked();
					}


				});
				mFailedInfo = (ExtractFailedException)msg.obj;
				setTitle(R.string.nmod_import_failed);
				AppCompatTextView errorText = (AppCompatTextView)findViewById(R.id.nmod_import_failed_title_text_view);
				switch (mFailedInfo.getType())
				{
					case ExtractFailedException.TYPE_DECODE_FAILED:
						errorText.setText(R.string.nmod_import_failed_message_decode);
						break;
					case ExtractFailedException.TYPE_INEQUAL_PACKAGE_NAME:
						errorText.setText(R.string.nmod_import_failed_message_inequal_package_name);
						break;
					case ExtractFailedException.TYPE_INVAILD_PACKAGE_NAME:
						errorText.setText(R.string.nmod_import_failed_message_invalid_package_name);
						break;
					case ExtractFailedException.TYPE_IO_EXCEPTION:
						errorText.setText(R.string.nmod_import_failed_message_io_exception);
						break;
					case ExtractFailedException.TYPE_JSON_SYNTAX_EXCEPTION:
						errorText.setText(R.string.nmod_import_failed_message_manifest_json_syntax_error);
						break;
					case ExtractFailedException.TYPE_NO_MANIFEST:
						errorText.setText(R.string.nmod_import_failed_message_no_manifest);
						break;
					case ExtractFailedException.TYPE_UNDEFINED_PACKAGE_NAME:
						errorText.setText(R.string.nmod_import_failed_message_no_package_name);
						break;
					case ExtractFailedException.TYPE_REDUNDANT_MANIFEST:
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
