package com.mcal.ModdedPE.app;
import android.app.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmodpe.*;
import com.mcal.ModdedPE.resources.*;
import com.mcal.ModdedPE.utils.*;
import java.util.*;

public class ModdedPEManageNModPEActivity extends Activity
{
	private ListView listActive;
	private ListView listQuiet;
	private Vector<NModPE> activeList;
	private Vector<NModPE> disabledList;
	private NModPEManager nmodpeManager;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_manage_nmodpe);
		
		NModPEManager.reCalculate(this);
		nmodpeManager=NModPEManager.getNModPEManager(this);
		
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_header_bg);  
			((ImageView)findViewById(R.id.moddedpemanageNModPEHeaderBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mcd_bg);  
			((ImageView)findViewById(R.id.moddedpemanageNModPEBackground)).setImageBitmap(BitmapRepeater.createRepeaterW(getWindowManager().getDefaultDisplay().getWidth(), bitmap));
		}
		((TextView)findViewById(R.id.moddedpemanageNModPETitleTextView)).setText(getTitle());
		((TextView)findViewById(R.id.moddedpemanageNModPEIsSafetyMode)).setVisibility(new Settings(this).getSafeMode()?View.VISIBLE:View.GONE);
		
		listActive=(ListView)findViewById(R.id.moddedpemanageNModPEListActive);
		listQuiet=(ListView)findViewById(R.id.moddedpemanageNModPEListQuiet);
		
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
			final NModPE itemNModPE = activeList.get(itemPosition);
			
			LayoutInflater inflater=getLayoutInflater();
			convertView=inflater.inflate(R.layout.moddedpe_nmodpe_item_active,null);
			
			AppCompatTextView title=(AppCompatTextView)convertView.findViewById(R.id.moddedpenmodpeitemactiveTextViewTitle);
			title.setText(itemNModPE.getName());
			
			Button btn=(Button)convertView.findViewById(R.id.moddedpenmodpeitemactiveAdjustButton);
			btn.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						nmodpeManager.removeActive(itemNModPE);
						refreshDatas();
					}
					
				
			});
			
			Button buttonDown=(Button)convertView.findViewById(R.id.moddedpenmodpeitemactiveButtonDown);
			if(position==activeList.size()-1)
				buttonDown.setClickable(false);
			else 
				buttonDown.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							nmodpeManager.makeDown(itemNModPE);
							refreshDatas();
						}
						
					
				});
				
			Button buttonUp=(Button)convertView.findViewById(R.id.moddedpenmodpeitemactiveButtonUp);
			if(position==0)
				buttonUp.setClickable(false);
			else 
				buttonUp.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							nmodpeManager.makeUp(itemNModPE);
							refreshDatas();
						}


					});
			
			ImageView image=(ImageView)convertView.findViewById(R.id.moddedpenmodpeitemactiveImageView);
			if(itemNModPE.getIcon()!=null)
				image.setBackground(new BitmapDrawable(itemNModPE.getIcon()));
			else
				image.setBackgroundResource(R.drawable.mcd_null_pack);
			return convertView;
		}


    }
	
	private void refreshDatas()
	{
		activeList=nmodpeManager.getActiveNModPEs();
		disabledList=nmodpeManager.getDisabledNModPEs();
		
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
			final NModPE itemNModPE = disabledList.get(itemPosition);
			
			LayoutInflater inflater=getLayoutInflater();
			convertView=inflater.inflate(R.layout.moddedpe_nmodpe_item_disabled,null);
			
			ImageView image=(ImageView)convertView.findViewById(R.id.moddedpenmodpeitemdisabledImageView);
			if(itemNModPE.getIcon()!=null)
				image.setBackground(new BitmapDrawable(itemNModPE.getIcon()));
			else
				image.setBackgroundResource(R.drawable.mcd_null_pack);
			
			AppCompatTextView title=(AppCompatTextView)convertView.findViewById(R.id.moddedpenmodpeitemdisabledTextViewTitle);
			title.setText(itemNModPE.getName());

			Button btn=(Button)convertView.findViewById(R.id.moddedpenmodpeitemdisabledAdjustButton);
			btn.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View p1)
					{
						nmodpeManager.addActive(itemNModPE);
						refreshDatas();
					}
				});
			
			
			return convertView; 
		}
		
    }
	
	
	public void onCloseClicked(View view)
	{
		finish();
	}
}
