package com.mcal.ModdedPE.app;
import android.app.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;
import com.mcal.ModdedPE.resources.*;
import com.mcal.ModdedPE.utils.*;
import java.util.*;

public class ModdedPEManageNModActivity extends Activity
{
	private ListView listActive;
	private ListView listQuiet;
	private Vector<NMod> activeList;
	private Vector<NMod> disabledList;
	private NModManager nmodManager;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_manage_nmod);
		
		nmodManager=NModManager.getNModManager(this);
		
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_header_bg);  
			((ImageView)findViewById(R.id.moddedpemanageNModHeaderBackgroundStatus)).setImageBitmap(BitmapRepeater.createRepeaterW(getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_bg);  
			((ImageView)findViewById(R.id.moddedpemanageNModBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		((TextView)findViewById(R.id.moddedpemanageNModTitleTextView)).setText(getTitle());
		((TextView)findViewById(R.id.moddedpemanageNModIsSafetyMode)).setVisibility(new Settings(this).getSafeMode()?View.VISIBLE:View.GONE);
		
		listActive=(ListView)findViewById(R.id.moddedpemanageNModListActive);
		listQuiet=(ListView)findViewById(R.id.moddedpemanageNModListQuiet);
		
		listActive.getLayoutParams().width=getWindowManager().getDefaultDisplay().getWidth()/2-1;
		listQuiet.getLayoutParams().width=getWindowManager().getDefaultDisplay().getWidth()/2-1;
		
		refreshDatas();
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
			return activeList.size();
		} 

		@Override 
		public View getView(int position, View convertView, ViewGroup parent)
		{ 
			if(position>=getCount())
				return convertView;
		
			final int itemPosition=position;
			final NMod itemNMod = activeList.get(itemPosition);
			
			LayoutInflater inflater=getLayoutInflater();
			convertView=inflater.inflate(R.layout.moddedpe_nmod_item_active,null);
			
			AppCompatTextView title=(AppCompatTextView)convertView.findViewById(R.id.moddedpenmoditemactiveTextViewTitle);
			title.setText(itemNMod.getName());
			
			Button btnDescription=(Button)convertView.findViewById(R.id.moddedpenmoditemactiveButtonDescription);
			btnDescription.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						new NModDescriptionDialog(ModdedPEManageNModActivity.this,itemNMod).show();
					}
					
				
			});
			
			Button btn=(Button)convertView.findViewById(R.id.moddedpenmoditemactiveAdjustButton);
			btn.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						nmodManager.removeActive(itemNMod);
						refreshDatas();
					}
					
				
			});
			
			Button buttonDown=(Button)convertView.findViewById(R.id.moddedpenmoditemactiveButtonDown);
			if(position==activeList.size()-1)
				buttonDown.setClickable(false);
			else 
				buttonDown.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							nmodManager.makeDown(itemNMod);
							refreshDatas();
						}
						
					
				});
				
			Button buttonUp=(Button)convertView.findViewById(R.id.moddedpenmoditemactiveButtonUp);
			if(position==0)
				buttonUp.setClickable(false);
			else 
				buttonUp.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							nmodManager.makeUp(itemNMod);
							refreshDatas();
						}


					});
			
			ImageView image=(ImageView)convertView.findViewById(R.id.moddedpenmoditemactiveImageView);
			image.setBackground(new BitmapDrawable(itemNMod.getIcon()));
			return convertView;
		}


    }
	
	private void refreshDatas()
	{
		activeList=nmodManager.getActiveNMods();
		disabledList=nmodManager.getDisabledNMods();
		
		AdapterActive adapterActive = new AdapterActive();
		listActive.setAdapter(adapterActive);

		AdapterQuiet adapterQuiet = new AdapterQuiet();
		listQuiet.setAdapter(adapterQuiet);
	}
	
	public class AdapterQuiet extends BaseAdapter 
    {
		@Override 
		public int getCount()
		{
			return disabledList.size();
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
			if(position>=getCount())
				return convertView;
		
			final int itemPosition=position;
			final NMod itemNMod = disabledList.get(itemPosition);
			
			LayoutInflater inflater=getLayoutInflater();
			if(!itemNMod.isBugPack())
			{
				convertView=inflater.inflate(R.layout.moddedpe_nmod_item_disabled,null);
			
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
							nmodManager.addActive(itemNMod);
							refreshDatas();
						}
					});
					
				Button btnDescription=(Button)convertView.findViewById(R.id.moddedpenmoditemdisabledButtonDescription);
				btnDescription.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							new NModDescriptionDialog(ModdedPEManageNModActivity.this,itemNMod).show();
						}


					});
			}
			else
			{
				convertView=inflater.inflate(R.layout.moddedpe_nmod_item_bugged,null);

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
							new LoadErrorDialog(ModdedPEManageNModActivity.this,itemNMod).show();
						}
					});
			}
			
			return convertView; 
		}
		
    }
	
	
	public void onCloseClicked(View view)
	{
		finish();
	}
}
