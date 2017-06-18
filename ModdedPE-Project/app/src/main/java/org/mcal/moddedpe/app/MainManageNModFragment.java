package org.mcal.moddedpe.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.mcal.moddedpe.R;
import org.mcal.moddedpe.utils.DataPreloader;
import org.mcal.pesdk.nmod.ArchiveFailedException;
import org.mcal.pesdk.nmod.NMod;
import org.mcal.pesdk.nmod.PackagedNMod;
import org.mcal.pesdk.nmod.ZippedNMod;

import java.util.ArrayList;

public class MainManageNModFragment extends BaseFragment implements DataPreloader.PreloadingFinishedListener
{
	private ListView mListView;
	private View mRootView;
	private NModProcesserHandler mNModProcesserHandler = new NModProcesserHandler();
	private AlertDialog mProcessingDialog = null;
	private ReloadHandler mReloadHandler = new ReloadHandler();
	private AlertDialog mReloadDialog = null;
	private DataPreloader mDataPreloader = null;
	
	private static final int MSG_SHOW_PROGRESS_DIALOG = 1;
	private static final int MSG_HIDE_PROGRESS_DIALOG = 2;
	private static final int MSG_SHOW_SUCCEED_DIALOG = 3;
	private static final int MSG_SHOW_REPLACED_DIALOG = 4;
	private static final int MSG_SHOW_FAILED_DIALOG = 5;
	private static final int MSG_REFRESH_NMOD_DATA = 6;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mRootView = inflater.inflate(R.layout.moddedpe_manage_nmod, null);

		mListView = (ListView)mRootView.findViewById(R.id.moddedpe_manage_nmod_list_view);

		refreshNModDatas();

		FloatingActionButton addBtn = (FloatingActionButton)mRootView.findViewById(R.id.moddedpe_manage_nmod_add_new);
		addBtn.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					onAddNewNMod();
				}


			});
		return mRootView;
	}

	private class ReloadHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (mReloadDialog != null)
			{
				refreshNModDatas();
				mReloadDialog.dismiss();
				mReloadDialog = null;
			}
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (mDataPreloader == null && !getPESdk().isInited())
		{
			mReloadDialog = new AlertDialog.Builder(getActivity()).setTitle(R.string.main_reloading_title).setView(R.layout.moddedpe_main_reload_dialog).setCancelable(false).create();
			mReloadDialog.show();
			mDataPreloader = new DataPreloader(this);
			mDataPreloader.preload(getActivity());
		}
	}

	@Override
	public void onPreloadingFinished()
	{
		mReloadHandler.sendEmptyMessage(0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (resultCode == Activity.RESULT_OK)
		{
			if (requestCode == NModPackagePickerActivity.REQUEST_PICK_PACKAGE)
			{
				//picked from package
				onPickedNModFromPackage(data.getExtras().getString(NModPackagePickerActivity.TAG_PACKAGE_NAME));
			}
			else if (requestCode == NModFilePickerActivity.REQUEST_PICK_FILE)
			{
				//picked from storage
				onPickedNModFromStorage(data.getExtras().getString(NModFilePickerActivity.TAG_FILE_PATH));
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onPickedNModFromStorage(String path)
	{
		final String finalPath = path;
		new Thread()
		{
			@Override
			public void run()
			{
				mNModProcesserHandler.sendEmptyMessage(MSG_SHOW_PROGRESS_DIALOG);
				try
				{
					ZippedNMod zippedNMod = getPESdk().getNModAPI().archiveZippedNMod(finalPath);
					if (getPESdk().getNModAPI().importNMod(zippedNMod))
					{
						//replaced
						mNModProcesserHandler.sendEmptyMessage(MSG_HIDE_PROGRESS_DIALOG);
						mNModProcesserHandler.sendEmptyMessage(MSG_SHOW_REPLACED_DIALOG);
					}
					else
					{
						mNModProcesserHandler.sendEmptyMessage(MSG_HIDE_PROGRESS_DIALOG);
						mNModProcesserHandler.sendEmptyMessage(MSG_SHOW_SUCCEED_DIALOG);
					}
					mNModProcesserHandler.sendEmptyMessage(MSG_REFRESH_NMOD_DATA);

				}
				catch (ArchiveFailedException archiveFailedException)
				{
					mNModProcesserHandler.sendEmptyMessage(MSG_HIDE_PROGRESS_DIALOG);
					Message message = new Message();
					message.what = MSG_SHOW_FAILED_DIALOG;
					message.obj = archiveFailedException;
					mNModProcesserHandler.sendMessage(message);
				}

			}
		}.start();
	}

	public void onPickedNModFromPackage(String packageName)
	{
		final String finalPkgName = packageName;
		new Thread()
		{
			@Override
			public void run()
			{
				mNModProcesserHandler.sendEmptyMessage(MSG_SHOW_PROGRESS_DIALOG);
				try
				{
					PackagedNMod packagedNMod = getPESdk().getNModAPI().archivePackagedNMod(finalPkgName);
					if (getPESdk().getNModAPI().importNMod(packagedNMod))
					{
						//replaced
						mNModProcesserHandler.sendEmptyMessage(MSG_HIDE_PROGRESS_DIALOG);
						mNModProcesserHandler.sendEmptyMessage(MSG_SHOW_REPLACED_DIALOG);
					}
					else
					{
						mNModProcesserHandler.sendEmptyMessage(MSG_HIDE_PROGRESS_DIALOG);
						mNModProcesserHandler.sendEmptyMessage(MSG_SHOW_SUCCEED_DIALOG);
					}
					mNModProcesserHandler.sendEmptyMessage(MSG_REFRESH_NMOD_DATA);

				}
				catch (ArchiveFailedException archiveFailedException)
				{
					mNModProcesserHandler.sendEmptyMessage(MSG_HIDE_PROGRESS_DIALOG);
					Message message = new Message();
					message.what = MSG_SHOW_FAILED_DIALOG;
					message.obj = archiveFailedException;
					mNModProcesserHandler.sendMessage(message);
				}
			}
		}.start();
	}

	public void showPickNModFailedDialog(ArchiveFailedException archiveFailedException)
	{
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity()).setTitle(R.string.nmod_import_failed).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					p1.dismiss();
				}


			});
		switch (archiveFailedException.getType())
		{
			case ArchiveFailedException.TYPE_DECODE_FAILED:
				alertBuilder.setMessage(R.string.nmod_import_failed_message_decode);
				break;
			case ArchiveFailedException.TYPE_INEQUAL_PACKAGE_NAME:
				alertBuilder.setMessage(R.string.nmod_import_failed_message_inequal_package_name);
				break;
			case ArchiveFailedException.TYPE_INVAILD_PACKAGE_NAME:
				alertBuilder.setMessage(R.string.nmod_import_failed_message_invalid_package_name);
				break;
			case ArchiveFailedException.TYPE_IO_EXCEPTION:
				alertBuilder.setMessage(R.string.nmod_import_failed_message_io_exception);
				break;
			case ArchiveFailedException.TYPE_JSON_SYNTAX_EXCEPTION:
				alertBuilder.setMessage(R.string.nmod_import_failed_message_manifest_json_syntax_error);
				break;
			case ArchiveFailedException.TYPE_NO_MANIFEST:
				alertBuilder.setMessage(R.string.nmod_import_failed_message_no_manifest);
				break;
			case ArchiveFailedException.TYPE_UNDEFINED_PACKAGE_NAME:
				alertBuilder.setMessage(R.string.nmod_import_failed_message_no_package_name);
				break;
			case ArchiveFailedException.TYPE_REDUNDANT_MANIFEST:
				alertBuilder.setMessage(R.string.nmod_import_failed_message_no_package_name);
				break;
			default:
				alertBuilder.setMessage(R.string.nmod_import_failed_message_unexpected);
				break;
		}
		if (archiveFailedException.getCause() != null)
		{
			final ArchiveFailedException fArvhiveFailedException = archiveFailedException;
			alertBuilder.setNegativeButton(R.string.nmod_import_failed_button_full_info, new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						p1.dismiss();
						new AlertDialog.Builder(getActivity()).setTitle(R.string.nmod_import_failed_full_info_title).setMessage(getContext().getResources().getString(R.string.nmod_import_failed_full_info_message, new Object[]{fArvhiveFailedException.toTypeString(),fArvhiveFailedException.getCause().toString()})).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface p1_, int p2)
								{
									p1_.dismiss();
								}


							}).show();
					}


				});
		}
		alertBuilder.show();
	}

	private class NModProcesserHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case MSG_SHOW_PROGRESS_DIALOG:
					mProcessingDialog = new AlertDialog.Builder(getActivity()).setTitle(R.string.nmod_importing_title).setView(R.layout.moddedpe_manage_nmod_progress_dialog_view).setCancelable(false).show();
					break;
				case MSG_HIDE_PROGRESS_DIALOG:
					if (mProcessingDialog != null)
						mProcessingDialog.hide();
					mProcessingDialog = null;
					break;
				case MSG_SHOW_SUCCEED_DIALOG:

					new AlertDialog.Builder(getActivity()).setTitle(R.string.nmod_import_succeed_title).setMessage(R.string.nmod_import_succeed_message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								p1.dismiss();
							}


						}).show();
					break;
				case MSG_SHOW_REPLACED_DIALOG:
					new AlertDialog.Builder(getActivity()).setTitle(R.string.nmod_import_replaced_title).setMessage(R.string.nmod_import_replaced_message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								p1.dismiss();
							}


						}).show();
					break;
				case MSG_SHOW_FAILED_DIALOG:
					showPickNModFailedDialog((ArchiveFailedException)msg.obj);
					break;
				case MSG_REFRESH_NMOD_DATA:
					refreshNModDatas();
					break;
			}
		}
	}

	public void refreshNModDatas()
	{
		if (getPESdk().getNModAPI().getImportedEnabledNMods().isEmpty() && getPESdk().getNModAPI().getImportedDisabledNMods().isEmpty())
		{
			mRootView.findViewById(R.id.moddedpe_manage_nmod_layout_nmods).setVisibility(View.GONE);
			mRootView.findViewById(R.id.moddedpe_manage_nmod_layout_no_found).setVisibility(View.VISIBLE);
		}
		else
		{
			mRootView.findViewById(R.id.moddedpe_manage_nmod_layout_nmods).setVisibility(View.VISIBLE);
			mRootView.findViewById(R.id.moddedpe_manage_nmod_layout_no_found).setVisibility(View.GONE);
		}
		
		NModListAdapter adapterList = new NModListAdapter();
		mListView.setAdapter(adapterList);
	}

	private void showBugDialog(NMod nmod)
	{
		if(!nmod.isBugPack())
			return;
		new AlertDialog.Builder(getActivity()).setTitle(R.string.load_fail_title).setMessage(getString(R.string.load_fail_msg,new Object[]{nmod.getLoadException().toTypeString(),nmod.getLoadException().getCause().toString()})).setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					p1.dismiss();
				}
				
			
		}).show();
	}
	
	public class NModListAdapter extends BaseAdapter 
    {
		private ArrayList<NMod> mImportedEnabledNMods = new ArrayList<NMod>();
		private ArrayList<NMod> mImportedDisabledNMods = new ArrayList<NMod>();

		public NModListAdapter()
		{
			mImportedEnabledNMods.addAll(getPESdk().getNModAPI().getImportedEnabledNMods());
			mImportedDisabledNMods.addAll(getPESdk().getNModAPI().getImportedDisabledNMods());
		}

		@Override 
		public int getCount()
		{
			int count = mImportedEnabledNMods.size() + mImportedDisabledNMods.size() + 2;
			if (mImportedEnabledNMods.size() > 0)
				++count;
			return count;
		}

		@Override 
		public Object getItem(int position)
		{
			return position;
		}

		@Override 
		public long getItemId(int position)
		{
			return position;
		}

		@Override 
		public View getView(int position, View convertView, ViewGroup parent)
		{
			boolean shouldShowEnabledList = mImportedEnabledNMods.size() > 0 && (position  < mImportedEnabledNMods.size() + 1);
			if (shouldShowEnabledList)
			{
				if (position == 0)
				{
					return createCutlineView(R.string.nmod_enabled_title);
				}
				else
				{
					int nmodIndex = position - 1;
					return createEnabledNModView(mImportedEnabledNMods.get(nmodIndex));
				}
			}
			int disableStartPosition = mImportedEnabledNMods.size() > 0 ? mImportedEnabledNMods.size() + 1: 0;
			if (position == disableStartPosition)
			{
				return createCutlineView(R.string.nmod_disabled_title);
			}
			int itemInListPosition = position - 1 - disableStartPosition;
			if (itemInListPosition >= 0 && itemInListPosition < mImportedDisabledNMods.size())
			{
				return createDisabledNModView(mImportedDisabledNMods.get(itemInListPosition));
			}
			return createAddNewView();
		}

    }

	private View createCutlineView(int textResId)
	{
		View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.moddedpe_ui_cutline, null);
		AppCompatTextView textTitle = (AppCompatTextView)convertView.findViewById(R.id.moddedpe_cutline_textview);
		textTitle.setText(textResId);
		return convertView;
	}

	private View createAddNewView()
	{
		View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.moddedpe_nmod_item_new, null);
		convertView.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					onAddNewNMod();
				}


			});
		return convertView;
	}

	private View createDisabledNModView(NMod nmod_)
	{
		final NMod nmod = nmod_;
		View convertView = null;
		if (nmod.isBugPack())
		{
			convertView = LayoutInflater.from(getActivity()).inflate(R.layout.moddedpe_nmod_item_bugged, null);
			AppCompatTextView textTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_bugged_item_card_view_text_name);
			textTitle.setText(nmod.getName());
			AppCompatTextView textPkgTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_bugged_item_card_view_text_package_name);
			textPkgTitle.setText(nmod.getPackageName());
			AppCompatImageView imageIcon = (AppCompatImageView)convertView.findViewById(R.id.nmod_bugged_item_card_view_image_view);
			Bitmap nmodIcon = nmod.getIcon();
			if (nmodIcon == null)
				nmodIcon = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_null_pack);
			imageIcon.setImageBitmap(nmodIcon);
			FloatingActionButton infoButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_bugged_info);
			View.OnClickListener onInfoClickedListener = new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					showBugDialog(nmod);
				}


			};
			FloatingActionButton deleteButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_bugged_delete);
			deleteButton.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						new AlertDialog.Builder(getActivity()).setTitle(R.string.nmod_delete_title).setMessage(R.string.nmod_delete_message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									getPESdk().getNModAPI().removeImportedNMod(nmod);
									refreshNModDatas();
									p1.dismiss();
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


				});
			infoButton.setOnClickListener(onInfoClickedListener);
			convertView.setOnClickListener(onInfoClickedListener);
			return convertView;
		}
		convertView = LayoutInflater.from(getActivity()).inflate(R.layout.moddedpe_nmod_item_disabled, null);
		AppCompatTextView textTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_disabled_item_card_view_text_name);
		textTitle.setText(nmod.getName());
		AppCompatTextView textPkgTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_disabled_item_card_view_text_package_name);
		textPkgTitle.setText(nmod.getPackageName());
		AppCompatImageView imageIcon = (AppCompatImageView)convertView.findViewById(R.id.nmod_disabled_item_card_view_image_view);
		Bitmap nmodIcon = nmod.getIcon();
		if (nmodIcon == null)
			nmodIcon = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_null_pack);
		imageIcon.setImageBitmap(nmodIcon);
		FloatingActionButton addButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_disabled_add);
		addButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					getPESdk().getNModAPI().setEnabled(nmod, true);
					refreshNModDatas();
				}


			});
		FloatingActionButton deleteButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_disabled_delete);
		deleteButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					new AlertDialog.Builder(getActivity()).setTitle(R.string.nmod_delete_title).setMessage(R.string.nmod_delete_message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								getPESdk().getNModAPI().removeImportedNMod(nmod);
								refreshNModDatas();
								p1.dismiss();
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


			});
		convertView.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					NModDescriptionActivity.startThisActivity(getActivity(), nmod);
				}


			});
		return convertView;
	}

	private View createEnabledNModView(NMod nmod_)
	{
		final NMod nmod = nmod_;
		View convertView = null;
		if (nmod.isBugPack())
		{
			convertView = LayoutInflater.from(getActivity()).inflate(R.layout.moddedpe_nmod_item_bugged, null);
			AppCompatTextView textTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_bugged_item_card_view_text_name);
			textTitle.setText(nmod.getName());
			AppCompatTextView textPkgTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_bugged_item_card_view_text_package_name);
			textPkgTitle.setText(nmod.getPackageName());
			AppCompatImageView imageIcon = (AppCompatImageView)convertView.findViewById(R.id.nmod_bugged_item_card_view_image_view);
			Bitmap nmodIcon = nmod.getIcon();
			if (nmodIcon == null)
				nmodIcon = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_null_pack);
			imageIcon.setImageBitmap(nmodIcon);
			FloatingActionButton infoButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_bugged_info);
			View.OnClickListener onInfoClickedListener = new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					showBugDialog(nmod);
				}


			};
			FloatingActionButton deleteButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_bugged_delete);
			deleteButton.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						new AlertDialog.Builder(getActivity()).setTitle(R.string.nmod_delete_title).setMessage(R.string.nmod_delete_message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									getPESdk().getNModAPI().removeImportedNMod(nmod);
									refreshNModDatas();
									p1.dismiss();
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


				});
			infoButton.setOnClickListener(onInfoClickedListener);
			convertView.setOnClickListener(onInfoClickedListener);
			return convertView;
		}
		convertView = LayoutInflater.from(getActivity()).inflate(R.layout.moddedpe_nmod_item_active, null);
		AppCompatTextView textTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_enabled_item_card_view_text_name);
		textTitle.setText(nmod.getName());
		AppCompatTextView textPkgTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_enabled_item_card_view_text_package_name);
		textPkgTitle.setText(nmod.getPackageName());
		AppCompatImageView imageIcon = (AppCompatImageView)convertView.findViewById(R.id.nmod_enabled_item_card_view_image_view);
		Bitmap nmodIcon = nmod.getIcon();
		if (nmodIcon == null)
			nmodIcon = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_null_pack);
		imageIcon.setImageBitmap(nmodIcon);
		FloatingActionButton minusButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_enabled_minus);
		minusButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					getPESdk().getNModAPI().setEnabled(nmod, false);
					refreshNModDatas();
				}


			});
		FloatingActionButton downButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_enabled_arrow_down);
		downButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					getPESdk().getNModAPI().downPosNMod(nmod);
					refreshNModDatas();
				}


			});
		FloatingActionButton upButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_enabled_arrow_up);
		upButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					getPESdk().getNModAPI().upPosNMod(nmod);
					refreshNModDatas();
				}


			});
		convertView.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					NModDescriptionActivity.startThisActivity(getActivity(), nmod);
				}


			});
		return convertView;
	}

	private void onAddNewNMod()
	{
		new AlertDialog.Builder(getActivity()).setTitle(R.string.nmod_add_new_title).setMessage(R.string.nmod_add_new_message).setNegativeButton(R.string.nmod_add_new_pick_installed, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					NModPackagePickerActivity.startThisActivity(getActivity());
					p1.dismiss();
				}


			}).setPositiveButton(R.string.nmod_add_new_pick_storage, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					NModFilePickerActivity.startThisActivity(getActivity());
					p1.dismiss();
				}


			}).show();
	}
}
