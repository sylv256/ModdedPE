package com.mcal.ModdedPE.app;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;
import java.util.*;

import android.support.v7.app.AlertDialog;

public class MainManageNModFragment extends ModdedPEFragment
{
	private ListView managenmod_listView;
	private Vector<NMod> managenmod_activeList;
	private Vector<NMod> managenmod_disabledList;
	private View manage_nmod_view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		manage_nmod_view = inflater.inflate(R.layout.moddedpe_manage_nmod, null);

		managenmod_listView = (ListView)manage_nmod_view.findViewById(R.id.moddedpe_manage_nmod_list_view);

		refreshNModDatas();

		FloatingActionButton addBtn = (FloatingActionButton)manage_nmod_view.findViewById(R.id.moddedpe_manage_nmod_add_new);
		addBtn.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					onAddNewNMod();
				}


			});
		return manage_nmod_view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		try
		{
			if (resultCode == Activity.RESULT_OK)
			{
				if (requestCode == ModdedPENModPackagePickerActivity.REQUEST_PICK_PACKAGE)
				{
					PackagedNMod packagedNMod = getNModAPI().archivePackagedNMod(data.getExtras().getString(ModdedPENModPackagePickerActivity.TAG_PACKAGE_NAME));
					if (!getNModAPI().addNewNMod(packagedNMod, false))
					{
						new AlertDialog.Builder(getContext()).setTitle(R.string.nmod_existed_nmod_title).setMessage(R.string.nmod_existed_nmod_msg).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									p1.dismiss();
								}


							}).show();
					}
					refreshNModDatas();
				}
				else if (requestCode == ModdedPENModFilePickerActivity.REQUEST_PICK_FILE)
				{
					refreshNModDatas();
				}
			}
		}
		catch (Throwable t)
		{

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void refreshNModDatas()
	{
		managenmod_activeList = getNModAPI().getLoadedEnabledNMods();
		managenmod_disabledList = getNModAPI().getLoadedDisabledNMods();

		NModListAdapter adapterList = new NModListAdapter();
		managenmod_listView.setAdapter(adapterList);

		if (managenmod_activeList.isEmpty() && managenmod_disabledList.isEmpty())
		{
			manage_nmod_view.findViewById(R.id.moddedpe_manage_nmod_layout_nmods).setVisibility(View.GONE);
			manage_nmod_view.findViewById(R.id.moddedpe_manage_nmod_layout_no_found).setVisibility(View.VISIBLE);
		}
		else
		{
			manage_nmod_view.findViewById(R.id.moddedpe_manage_nmod_layout_nmods).setVisibility(View.VISIBLE);
			manage_nmod_view.findViewById(R.id.moddedpe_manage_nmod_layout_no_found).setVisibility(View.GONE);
		}
	}

	public class NModListAdapter extends BaseAdapter 
    {
		@Override 
		public int getCount()
		{
			int count = managenmod_activeList.size() + managenmod_disabledList.size() + 2;
			if (managenmod_activeList.size() > 0)
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
			boolean shouldShowEnabledList = managenmod_activeList.size() > 0 && (position  < managenmod_activeList.size() + 1);

			if (shouldShowEnabledList)
			{
				if (position == 0)
				{
					convertView = LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_ui_cutline, null);
					AppCompatTextView textTitle = (AppCompatTextView)convertView.findViewById(R.id.moddedpe_cutline_textview);
					textTitle.setText(R.string.nmod_enabled_title);
					return convertView;
				}
				else
				{
					int nmodIndex = position - 1;
					final NMod nmod = managenmod_activeList.get(nmodIndex);
					if (nmod.isBugPack())
					{
						convertView = LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_nmod_item_bugged, null);
						AppCompatTextView textTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_bugged_item_card_view_text_name);
						textTitle.setText(nmod.getName());
						AppCompatTextView textPkgTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_bugged_item_card_view_text_package_name);
						textPkgTitle.setText(nmod.getPackageName());
						AppCompatImageView imageIcon = (AppCompatImageView)convertView.findViewById(R.id.nmod_bugged_item_card_view_image_view);
						imageIcon.setImageBitmap(nmod.getIcon());
						FloatingActionButton infoButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_bugged_info);
						View.OnClickListener onInfoClickedListener = new View.OnClickListener()
						{

							@Override
							public void onClick(View p1)
							{
								ModdedPENModLoadFailActivity.startThisActivity(getContext(), nmod);
							}


						};
						infoButton.setOnClickListener(onInfoClickedListener);
						convertView.setOnClickListener(onInfoClickedListener);
						return convertView;
					}
					convertView = LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_nmod_item_active, null);
					AppCompatTextView textTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_enabled_item_card_view_text_name);
					textTitle.setText(nmod.getName());
					AppCompatTextView textPkgTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_enabled_item_card_view_text_package_name);
					textPkgTitle.setText(nmod.getPackageName());
					AppCompatImageView imageIcon = (AppCompatImageView)convertView.findViewById(R.id.nmod_enabled_item_card_view_image_view);
					imageIcon.setImageBitmap(nmod.getIcon());
					FloatingActionButton minusButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_enabled_minus);
					minusButton.setOnClickListener(new View.OnClickListener()
						{

							@Override
							public void onClick(View p1)
							{
								getNModAPI().setEnabled(nmod, false);
								refreshNModDatas();
							}


						});
					FloatingActionButton downButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_enabled_arrow_down);
					downButton.setOnClickListener(new View.OnClickListener()
						{

							@Override
							public void onClick(View p1)
							{
								getNModAPI().downPosNMod(nmod);
								refreshNModDatas();
							}


						});
					FloatingActionButton upButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_enabled_arrow_up);
					upButton.setOnClickListener(new View.OnClickListener()
						{

							@Override
							public void onClick(View p1)
							{
								getNModAPI().upPosNMod(nmod);
								refreshNModDatas();
							}


						});
					convertView.setOnClickListener(new View.OnClickListener()
						{

							@Override
							public void onClick(View p1)
							{
								ModdedPENModDescriptionActivity.startThisActivity(getContext(), nmod);
							}


						});
					return convertView;
				}
			}

			int disableStartPosition = managenmod_activeList.size() > 0 ? managenmod_activeList.size() + 1: 0;

			if (position == disableStartPosition)
			{
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_ui_cutline, null);
				AppCompatTextView textTitle = (AppCompatTextView)convertView.findViewById(R.id.moddedpe_cutline_textview);
				textTitle.setText(R.string.nmod_disabled_title);
				return convertView;
			}
			int itemInListPosition = position - 1 - disableStartPosition;

			if (itemInListPosition >= 0 && itemInListPosition < managenmod_disabledList.size())
			{
				final NMod nmod = managenmod_disabledList.get(itemInListPosition);
				if (nmod.isBugPack())
				{
					convertView = LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_nmod_item_bugged, null);
					AppCompatTextView textTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_bugged_item_card_view_text_name);
					textTitle.setText(nmod.getName());
					AppCompatTextView textPkgTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_bugged_item_card_view_text_package_name);
					textPkgTitle.setText(nmod.getPackageName());
					AppCompatImageView imageIcon = (AppCompatImageView)convertView.findViewById(R.id.nmod_bugged_item_card_view_image_view);
					imageIcon.setImageBitmap(nmod.getIcon());
					FloatingActionButton infoButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_bugged_info);
					View.OnClickListener onInfoClickedListener = new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							ModdedPENModLoadFailActivity.startThisActivity(getContext(), nmod);
						}


					};
					infoButton.setOnClickListener(onInfoClickedListener);
					convertView.setOnClickListener(onInfoClickedListener);
					return convertView;
				}
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_nmod_item_disabled, null);
				AppCompatTextView textTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_disabled_item_card_view_text_name);
				textTitle.setText(nmod.getName());
				AppCompatTextView textPkgTitle = (AppCompatTextView)convertView.findViewById(R.id.nmod_disabled_item_card_view_text_package_name);
				textPkgTitle.setText(nmod.getPackageName());
				AppCompatImageView imageIcon = (AppCompatImageView)convertView.findViewById(R.id.nmod_disabled_item_card_view_image_view);
				imageIcon.setImageBitmap(nmod.getIcon());
				FloatingActionButton addButton = (FloatingActionButton)convertView.findViewById(R.id.nmod_disabled_add);
				addButton.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							getNModAPI().setEnabled(nmod, true);
							refreshNModDatas();
						}


					});
				convertView.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							ModdedPENModDescriptionActivity.startThisActivity(getContext(), nmod);
						}


					});
				return convertView;
			}

			convertView = LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_nmod_item_new, null);
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

    }

	private void onAddNewNMod()
	{
		new AlertDialog.Builder(getActivity()).setTitle(R.string.nmod_add_new_title).setMessage(R.string.nmod_add_new_message).setNegativeButton(R.string.nmod_add_new_pick_installed, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					ModdedPENModPackagePickerActivity.startThisActivity(getActivity());
					p1.dismiss();
				}


			}).setPositiveButton(R.string.nmod_add_new_pick_storage, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					ModdedPENModFilePickerActivity.startThisActivity(getActivity());
					p1.dismiss();
				}


			}).show();
	}
}
