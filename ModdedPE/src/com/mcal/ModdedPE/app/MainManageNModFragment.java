package com.mcal.ModdedPE.app;
import android.app.*;
import android.content.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mcal.MCDesign.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;
import java.util.*;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

public class MainManageNModFragment extends Fragment
{
	private ListView managenmod_listViewActive;
	private ListView managenmod_listViewDisabled;
	private Vector<NMod> managenmod_activeList;
	private Vector<NMod> managenmod_disabledList;
	private NModManager managenmod_nmodManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View manage_nmod_view=inflater.inflate(R.layout.moddedpe_manage_nmod, null);

		managenmod_nmodManager = managenmod_nmodManager.getNModManager(container.getContext());

		managenmod_listViewActive = (ListView)manage_nmod_view.findViewById(R.id.moddedpemanageNModListActive);
		managenmod_listViewDisabled = (ListView)manage_nmod_view.findViewById(R.id.moddedpemanageNModListDisabled);

		managenmod_listViewActive.getLayoutParams().width = getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2 - 1;
		managenmod_listViewDisabled.getLayoutParams().width = getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2 - 1;

		refreshNModDatas();

		if (managenmod_activeList.isEmpty() && managenmod_disabledList.isEmpty())
			manage_nmod_view.findViewById(R.id.moddedpemanagenmodLayoutNoFound).setVisibility(View.VISIBLE);
		else
			manage_nmod_view.findViewById(R.id.moddedpemanagenmodLayoutNormal).setVisibility(View.VISIBLE);

		FloatingActionButton addBtn = (FloatingActionButton)manage_nmod_view.findViewById(R.id.moddedpe_manage_nmod_add_new);
		manage_nmod_view.findViewById(R.id.moddedpemanageNModAddNewCardView).getLayoutParams().width = getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2;
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
		if (resultCode == Activity.RESULT_OK)
		{
			if (requestCode == ModdedPENModPackagePickerActivity.REQUEST_PICK_PACKAGE)
			{
				PackagedNMod packagedNMod = PackagedNMod.archiveNMod(this.getContext(),data.getExtras().getString(ModdedPENModPackagePickerActivity.TAG_PACKAGE_NAME));
				//NModManager.getNModManager(this.getContext()).addNew(packagedNMod);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public class AdapterActive extends BaseAdapter 
    {
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
		public int getCount()
		{ 
			return managenmod_activeList.size();
		} 

		@Override 
		public View getView(int position, View convertView, ViewGroup parent)
		{ 
			if (position >= getCount())
				return convertView;

			final int itemPosition=position;
			final NMod itemNMod = managenmod_activeList.get(itemPosition);

			LayoutInflater inflater=getActivity().getLayoutInflater();
			convertView = inflater.inflate(R.layout.moddedpe_nmod_item_active, null);

			AppCompatTextView title=(AppCompatTextView)convertView.findViewById(R.id.moddedpenmoditemactiveTextViewTitle);
			title.setText(itemNMod.getName());

			Button btnDescription=(Button)convertView.findViewById(R.id.moddedpenmoditemactiveButtonDescription);
			btnDescription.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						ModdedPENModDescriptionActivity.startThisActivity(getActivity(), itemNMod);
					}


				});

			Button btn=(Button)convertView.findViewById(R.id.moddedpenmoditemactiveAdjustButton);
			btn.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						managenmod_nmodManager.removeActive(itemNMod);
						refreshNModDatas();
					}


				});

			Button buttonDown=(Button)convertView.findViewById(R.id.moddedpenmoditemactiveButtonDown);
			if (position == managenmod_activeList.size() - 1)
				buttonDown.setClickable(false);
			else 
				buttonDown.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							managenmod_nmodManager.makeDown(itemNMod);
							refreshNModDatas();
						}


					});

			Button buttonUp=(Button)convertView.findViewById(R.id.moddedpenmoditemactiveButtonUp);
			if (position == 0)
				buttonUp.setClickable(false);
			else 
				buttonUp.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							managenmod_nmodManager.makeUp(itemNMod);
							refreshNModDatas();
						}


					});

			ImageView image=(ImageView)convertView.findViewById(R.id.moddedpenmoditemactiveImageView);
			image.setBackground(new BitmapDrawable(itemNMod.getIcon()));
			return convertView;
		}


    }

	private void refreshNModDatas()
	{
		managenmod_activeList = managenmod_nmodManager.getActiveNMods();
		managenmod_disabledList = managenmod_nmodManager.getDisabledNMods();

		AdapterActive adapterActive = new AdapterActive();
		managenmod_listViewActive.setAdapter(adapterActive);

		AdapterDisabled adapterDisabled = new AdapterDisabled();
		managenmod_listViewDisabled.setAdapter(adapterDisabled);
	}

	public class AdapterDisabled extends BaseAdapter 
    {
		@Override 
		public int getCount()
		{
			return managenmod_disabledList.size() + 1;
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
			if (position == managenmod_disabledList.size())
			{
				convertView = getActivity().getLayoutInflater().inflate(R.layout.moddedpe_nmod_item_new, null);

				MCDAddButton addBtn = (MCDAddButton) convertView.findViewById(R.id.moddedpenmoditemaddNewButton);
				addBtn.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							onAddNewNMod();
						}


					});
				return convertView;
			}
			if (position >= getCount())
				return convertView;

			final int itemPosition=position;
			final NMod itemNMod = managenmod_disabledList.get(itemPosition);

			LayoutInflater inflater=getActivity().getLayoutInflater();
			if (!itemNMod.isBugPack())
			{
				convertView = inflater.inflate(R.layout.moddedpe_nmod_item_disabled, null);

				ImageView image=(ImageView)convertView.findViewById(R.id.moddedpenmoditemdisabledImageView);
				image.setBackground(new BitmapDrawable(itemNMod.getIcon()));

				AppCompatTextView title=(AppCompatTextView)convertView.findViewById(R.id.moddedpenmoditemdisabledTextViewTitle);
				title.setText(itemNMod.getName());

				Button btn=(Button)convertView.findViewById(R.id.moddedpenmoditemdisabledAdjustButton);
				btn.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View p1)
						{
							managenmod_nmodManager.addActive(itemNMod);
							refreshNModDatas();
						}
					});

				Button btnDescription=(Button)convertView.findViewById(R.id.moddedpenmoditemdisabledButtonDescription);
				btnDescription.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							ModdedPENModDescriptionActivity.startThisActivity(getActivity(), itemNMod);
						}


					});
			}
			else
			{
				convertView = inflater.inflate(R.layout.moddedpe_nmod_item_bugged, null);

				ImageView image=(ImageView)convertView.findViewById(R.id.moddedpenmoditembuggedImageView);
				image.setBackground(new BitmapDrawable(itemNMod.getIcon()));

				AppCompatTextView title=(AppCompatTextView)convertView.findViewById(R.id.moddedpenmoditembuggedTextViewTitle);
				title.setText(itemNMod.getName());

				Button btn=(Button)convertView.findViewById(R.id.moddedpenmoditembuggedAdjustButton);
				btn.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View p1)
						{
							ModdedPENModLoadFailActivity.startThisActivity(getActivity(), itemNMod);
						}
					});
			}

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
