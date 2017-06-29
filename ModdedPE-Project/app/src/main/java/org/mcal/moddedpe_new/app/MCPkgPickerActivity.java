package org.mcal.moddedpe_new.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.mcal.moddedpe_new.R;
import org.mcal.pesdk.utils.LauncherOptions;

import java.util.List;

public class MCPkgPickerActivity extends BaseActivity
{
	private UIHandler mUIHandler = new UIHandler();
	private List<PackageInfo> mInstalledPackages = null;

	public static final String TAG_PACKAGE_NAME = "package_name";
	public static final int REQUEST_PICK_PACKAGE = 5;

	private static final int MSG_SHOW_LIST_VIEW = 1;
	private static final int MSG_SHOW_UNFOUND_VIEW = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pkg_picker);
		setResult(RESULT_CANCELED);
		setActionBarButtonCloseRight();

		View loading_view = findViewById(R.id.pkg_picker_package_loading_view);
		loading_view.setVisibility(View.VISIBLE);

		findViewById(R.id.pkg_picker_reset_button).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					onResetClicked();
				}


			});
		
		new LoadingThread().start();
	}

	private void showListView()
	{
		View loading_view = findViewById(R.id.pkg_picker_package_loading_view);
		loading_view.setVisibility(View.GONE);

		View list_view = findViewById(R.id.pkg_picker_package_list_view);
		list_view.setVisibility(View.VISIBLE);

		ListView list = (ListView) list_view;
		list.setAdapter(new PackageListAdapter());
	}

	private void showUnfoundView()
	{
		View loading_view = findViewById(R.id.pkg_picker_package_loading_view);
		loading_view.setVisibility(View.GONE);

		View view = findViewById(R.id.pkg_picker_package_unfound_view);
		view.setVisibility(View.VISIBLE);
	}

	private void onResetClicked()
	{
		new AlertDialog.Builder(this).setTitle(R.string.pick_tips_title).setMessage(R.string.pick_tips_reset_message).setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					p1.dismiss();
					Intent intent = new Intent();
					Bundle extras = new Bundle();
					extras.putString(TAG_PACKAGE_NAME, LauncherOptions.STRING_VALUE_DEFAULT);
					intent.putExtras(extras);
					setResult(RESULT_OK, intent);
					finish();
				}
				
			
		}).setNegativeButton(android.R.string.cancel,new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					p1.dismiss();
				}
				
			
		}).show();
	}

	private class UIHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == MSG_SHOW_LIST_VIEW)
			{
				showListView();
			}
			else if (msg.what == MSG_SHOW_UNFOUND_VIEW)
			{
				showUnfoundView();
			}
		}
	}

	private class LoadingThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				Thread.sleep(2500);
			}
			catch (InterruptedException e)
			{}
			mInstalledPackages = getPackageManager().getInstalledPackages(PackageManager.GET_CONFIGURATIONS);
			if (mInstalledPackages != null && mInstalledPackages.size() > 0)
				mUIHandler.sendEmptyMessage(MSG_SHOW_LIST_VIEW);
			else
				mUIHandler.sendEmptyMessage(MSG_SHOW_UNFOUND_VIEW);
		}
	}

	private class PackageListAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return mInstalledPackages.size();
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
			final PackageInfo pkg= mInstalledPackages.get(p1);
			View baseCardView = getLayoutInflater().inflate(R.layout.pkg_picker_item, null);
			AppCompatImageView imageView = (AppCompatImageView)baseCardView.findViewById(R.id.pkg_picker_package_item_card_view_image_view);
			try
			{
				Bitmap appIcon = BitmapFactory.decodeResource(createPackageContext(pkg.packageName, 0).getResources(), pkg.applicationInfo.icon);
				if (appIcon == null)
					appIcon = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_null_pack);
				imageView.setImageBitmap(appIcon);

			}
			catch (PackageManager.NameNotFoundException e)
			{}

			AppCompatTextView name = (AppCompatTextView)baseCardView.findViewById(R.id.pkg_picker_package_item_card_view_text_name);
			name.setText(pkg.applicationInfo.loadLabel(getPackageManager()));
			AppCompatTextView pkgname = (AppCompatTextView)baseCardView.findViewById(R.id.pkg_picker_package_item_card_view_text_package_name);
			pkgname.setText(pkg.packageName);
			baseCardView.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						new AlertDialog.Builder(MCPkgPickerActivity.this).setTitle(R.string.pick_tips_title).setMessage(getString(R.string.pick_tips_message,new Object[]{pkg.packageName,pkg.applicationInfo.loadLabel(getPackageManager())})).setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									p1.dismiss();
									Intent intent = new Intent();
									Bundle extras = new Bundle();
									extras.putString(TAG_PACKAGE_NAME, pkg.packageName);
									intent.putExtras(extras);
									setResult(RESULT_OK, intent);
									finish();
								}
								
							
						}).setNegativeButton(android.R.string.cancel,new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									p1.dismiss();
								}
								
							
						}).show();
					}


				});
			return baseCardView;
		}


	}

	public static void startThisActivity(Activity context)
	{
		Intent intent = new Intent(context, MCPkgPickerActivity.class);
		context.startActivityForResult(intent, REQUEST_PICK_PACKAGE);
	}
}
