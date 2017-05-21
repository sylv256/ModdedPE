package com.mcal.ModdedPE.app;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mcal.MCDesign.app.*;
import com.mcal.ModdedPE.*;
import java.io.*;
import java.util.*;


public class ModdedPENModFilePickerActivity extends MCDActivity
{
	private File currentPath;
	private Vector<File> filesInCurrentPath;

	public final static String TAG_FILE_PATH = "file_path";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nmod_picker_file);
		
		setResult(RESULT_CANCELED,new Intent());
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

	private void select(File file)
	{
		if (file.isDirectory())
			openDirectory(file);
		else
			selectFile(file);
	}

	private void openDirectory(File directory)
	{
		currentPath = directory;
		filesInCurrentPath = new Vector<File>();

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
								if (currentPath.getPath().lastIndexOf(File.separator) != -1 || currentPath.getPath().equals(File.separator))
								{
									String pathStr = currentPath.getPath().substring(0, currentPath.getPath().lastIndexOf(File.separator));
									if (pathStr.isEmpty())
										pathStr = File.separator;
									File lastFile = new File(pathStr);
									openDirectory(lastFile);
								}
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

	public static void startThisActivity(Activity context, File path)
	{
		startThisActivity(context, path.getPath());
	}

	public static void startThisActivity(Activity context, String path)
	{
		Intent intent = new Intent(context, ModdedPENModFilePickerActivity.class);
		Bundle extras = new Bundle();
		extras.putString(TAG_FILE_PATH, path);
		intent.putExtras(extras);
		context.startActivityForResult(intent, 0);
	}

	public static void startThisActivity(Activity context)
	{
		startThisActivity(context, Environment.getExternalStorageDirectory().getPath());
	}
}
