package org.mcal.moddedpe_new.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.mcal.moddedpe_new.R;
import org.mcal.pesdk.utils.LauncherOptions;

import java.io.File;
import java.util.ArrayList;

public class DirPickerActivity extends BaseActivity
{
	private File currentPath;
	private ArrayList<File> filesInCurrentPath;
	private SelectHandler mSelectHandler = new SelectHandler();

	private static final int MSG_SELECT = 1;

	public static final int REQUEST_PICK_DIR = 3;
	public final static String TAG_DIR_PATH = "dir_path";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_dir_picker);
		
		findViewById(R.id.moddedpe_dir_picker_fab_reset).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					onResetClicked();
				}
				
			
		});
		findViewById(R.id.moddedpe_dir_picker_fab_select).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					onSelectThisClicked();
				}
				
			
		});

		setResult(RESULT_CANCELED, new Intent());
		setActionBarButtonCloseRight();

		String pathString = null;
		try
		{
			pathString = getIntent().getExtras().getString(TAG_DIR_PATH);
		}
		catch (Throwable t)
		{}
		if (pathString == null)
			pathString = Environment.getExternalStorageDirectory().getAbsolutePath();
		currentPath = new File(pathString);

		openDirectory(currentPath);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}

	private boolean isValidParent()
	{
		return currentPath.getParentFile() != null && currentPath.getParentFile().exists() && currentPath.getParentFile().listFiles() != null && currentPath.getParentFile().listFiles().length > 0;
	}

	private void onSelectThisClicked()
	{
		Intent data = new Intent();
		Bundle extras = new Bundle();
		extras.putString(TAG_DIR_PATH, currentPath.getAbsolutePath());
		data.putExtras(extras);
		setResult(RESULT_OK, data);
		finish();
	}

	private void onResetClicked()
	{
		new AlertDialog.Builder(this).setTitle(R.string.dir_picker_reset_warning_title).setMessage(R.string.dir_picker_reset_warning_message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					p1.dismiss();
					Intent data = new Intent();
					Bundle extras = new Bundle();
					extras.putString(TAG_DIR_PATH, LauncherOptions.STRING_VALUE_DEFAULT);
					data.putExtras(extras);
					setResult(RESULT_OK, data);
					finish();
				}


			}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					p1.dismiss();
				}


			}).show();
	}

	private void select(File file_arg)
	{
		final File file = file_arg;
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(450);
				}
				catch (InterruptedException e)
				{}
				Message mMessage = new Message();
				mMessage.what = MSG_SELECT;
				mMessage.obj = file;
				mSelectHandler.sendMessage(mMessage);
			}
		}.start();
	}

	private void openDirectory(File directory)
	{
		currentPath = directory;
		filesInCurrentPath = new ArrayList<File>();

		File[] unmanagedFilesInCurrentDirectory = currentPath.listFiles();
		if (unmanagedFilesInCurrentDirectory != null)
		{
			for (File fileItem : unmanagedFilesInCurrentDirectory)
			{
				if (fileItem.isDirectory())
					filesInCurrentPath.add(fileItem);
			}
		}

		ListView fileListView = (ListView) findViewById(R.id.picker_dir_list_view);
		fileListView.setAdapter(new FileAdapter());
	}

	private class FileAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			if (isValidParent())
				return filesInCurrentPath.size() + 1;
			if (filesInCurrentPath.size() == 0)
				return 1;
			return filesInCurrentPath.size();
		}

		@Override
		public Object getItem(int p1)
		{
			return p1;
		}

		@Override
		public long getItemId(int p1)
		{
			return p1;
		}

		@Override
		public View getView(int p1, View p2, ViewGroup p3)
		{
			CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.nmod_picker_file_item, null);

			if (!currentPath.getPath().endsWith(File.separator))
			{
				if (p1 == 0)
				{
					AppCompatImageView fileImage = (AppCompatImageView) cardView.findViewById(R.id.nmod_picker_item_card_view_image_view);
					fileImage.setImageResource(R.drawable.ic_folder_outline);

					AppCompatTextView textFileName = (AppCompatTextView) cardView.findViewById(R.id.nmod_picker_item_card_view_text_name);
					textFileName.setText("...");

					cardView.setOnClickListener(new View.OnClickListener()
						{

							@Override
							public void onClick(View p1)
							{
								select(null);
							}


						});
				}
				else
				{
					final File currentCardViewFile = filesInCurrentPath.get(--p1);
					AppCompatImageView fileImage = (AppCompatImageView) cardView.findViewById(R.id.nmod_picker_item_card_view_image_view);
					fileImage.setImageResource(R.drawable.ic_folder);

					AppCompatTextView textFileName = (AppCompatTextView) cardView.findViewById(R.id.nmod_picker_item_card_view_text_name);
					textFileName.setText(currentCardViewFile.getName());

					cardView.setOnClickListener(new View.OnClickListener()
						{

							@Override
							public void onClick(View p1)
							{
								select(currentCardViewFile);
							}


						});
				}
			}
			else
			{
				final File currentCardViewFile = filesInCurrentPath.get(p1);
				AppCompatImageView fileImage = (AppCompatImageView) cardView.findViewById(R.id.nmod_picker_item_card_view_image_view);
				fileImage.setImageResource(R.drawable.ic_folder);

				AppCompatTextView textFileName = (AppCompatTextView) cardView.findViewById(R.id.nmod_picker_item_card_view_text_name);
				textFileName.setText(currentCardViewFile.getName());

				cardView.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							select(currentCardViewFile);
						}


					});
			}
			return cardView;
		}

	}

	private class SelectHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == MSG_SELECT)
			{
				File file = (File)msg.obj;
				if (file == null)
				{
					File lastFile = currentPath.getParentFile();
					if (isValidParent())
						openDirectory(lastFile);
				}
				else if (file.isDirectory())
					openDirectory(file);
			}
		}
	}

	public static void startThisActivity(Activity context, File path)
	{
		startThisActivity(context, path.getPath());
	}

	public static void startThisActivity(Activity context, String path)
	{
		Intent intent = new Intent(context, DirPickerActivity.class);
		Bundle extras = new Bundle();
		extras.putString(TAG_DIR_PATH, path);
		intent.putExtras(extras);
		context.startActivityForResult(intent, REQUEST_PICK_DIR);
	}

	public static void startThisActivity(Activity context)
	{
		startThisActivity(context, Environment.getExternalStorageDirectory().getPath());
	}
}
