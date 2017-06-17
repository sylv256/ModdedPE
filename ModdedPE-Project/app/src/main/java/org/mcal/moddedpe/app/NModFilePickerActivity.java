package org.mcal.moddedpe.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.mcal.moddedpe.R;

import java.io.File;
import java.util.ArrayList;


public class NModFilePickerActivity extends BaseActivity
{
	private File currentPath;
	private ArrayList<File> filesInCurrentPath;
	private SelectHandler mSelectHandler = new SelectHandler();

	private static final int MSG_SELECT = 1;
	public static final int REQUEST_PICK_FILE = 2;
	public final static String TAG_FILE_PATH = "file_path";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nmod_picker_file);

		setResult(RESULT_CANCELED, new Intent());
		setActionBarButtonCloseRight();

		String pathString = null;
		try
		{
			pathString = getIntent().getExtras().getString(TAG_FILE_PATH);
		}
		catch (Throwable t)
		{}
		if (pathString == null)
			pathString = Environment.getExternalStorageDirectory().toString();
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
			for (File fileItem : unmanagedFilesInCurrentDirectory)
			{
				if (!fileItem.isDirectory())
					filesInCurrentPath.add(fileItem);
			}
		}

		ListView fileListView = (ListView) findViewById(R.id.nmod_picker_file_list_view);
		fileListView.setAdapter(new FileAdapter());
	}

	private void selectFile(File file)
	{
		Intent data = new Intent();
		Bundle extras = new Bundle();
		extras.putString(TAG_FILE_PATH, file.getPath());
		data.putExtras(extras);
		setResult(RESULT_OK, data);
		finish();
	}

	private class FileAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			if (!currentPath.getPath().endsWith(File.separator))
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
					if (currentCardViewFile.isDirectory())
						fileImage.setImageResource(R.drawable.ic_folder);
					else
						fileImage.setImageResource(R.drawable.ic_file);

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
				if (filesInCurrentPath.size() > 0)
				{
					final File currentCardViewFile = filesInCurrentPath.get(p1);
					AppCompatImageView fileImage = (AppCompatImageView) cardView.findViewById(R.id.nmod_picker_item_card_view_image_view);
					if (currentCardViewFile.isDirectory())
						fileImage.setImageResource(R.drawable.ic_folder);
					else
						fileImage.setImageResource(R.drawable.ic_file);

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
				else
				{
					AppCompatImageView fileImage = (AppCompatImageView) cardView.findViewById(R.id.nmod_picker_item_card_view_image_view);
					fileImage.setImageResource(R.drawable.ic_folder_outline);

					AppCompatTextView textFileName = (AppCompatTextView) cardView.findViewById(R.id.nmod_picker_item_card_view_text_name);
					textFileName.setText(android.R.string.cancel);

					cardView.setOnClickListener(new View.OnClickListener()
						{

							@Override
							public void onClick(View p1)
							{
								openDirectory(Environment.getExternalStorageDirectory());
							}


						});
				}
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
					if (currentPath.getPath().lastIndexOf(File.separator) != -1 || currentPath.getPath().equals(File.separator))
					{
						String pathStr = currentPath.getPath().substring(0, currentPath.getPath().lastIndexOf(File.separator));
						if (pathStr.isEmpty())
							pathStr = File.separator;
						File lastFile = new File(pathStr);
						openDirectory(lastFile);
					}
				}
				else if (file.isDirectory())
					openDirectory(file);
				else
					selectFile(file);
			}
		}
	}

	public static void startThisActivity(Activity context, File path)
	{
		startThisActivity(context, path.getPath());
	}

	public static void startThisActivity(Activity context, String path)
	{
		Intent intent = new Intent(context, NModFilePickerActivity.class);
		Bundle extras = new Bundle();
		extras.putString(TAG_FILE_PATH, path);
		intent.putExtras(extras);
		context.startActivityForResult(intent, REQUEST_PICK_FILE);
	}

	public static void startThisActivity(Activity context)
	{
		startThisActivity(context, Environment.getExternalStorageDirectory().getPath());
	}
}
