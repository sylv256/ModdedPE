package com.mcal.ModdedPE.app;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.view.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mcal.MCDesign.app.*;
import com.mcal.MCDesign.widget.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;
import com.mcal.ModdedPE.utils.*;
import com.mcal.ModdedPE.widget.*;
import java.util.*;

public class ModdedPEMainActivity extends MCDActivity 
{
	private ViewPager mainViewPager;
	//main
	private NewsLayout newsLayout;
	private NMod main_showingNMod;
	private AppCompatTextView textViewIsSafeMode;
	//manage nmod
	private ListView managenmod_listViewActive;
	private ListView managenmod_listViewDisabled;
	private Vector<NMod> managenmod_activeList;
	private Vector<NMod> managenmod_disabledList;
	private NModManager managenmod_nmodManager;
	//options
	private MCDSwitch options_switchSafetyMode;
	private MCDSwitch options_switchRedstoneDot;
	private MCDSwitch options_switchHideDebugText;
	private MCDSwitch options_switchAutoSaveLevel;
	private MCDSwitch options_switchSelectAllInLeft;
	private MCDSwitch options_switchDisableTextureIsotropic;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final Settings settings=new Settings(this);
		mainViewPager = new ViewPager(this);
		setContentView(mainViewPager);
		mainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
			{

				@Override
				public void onPageScrolled(int p1, float p2, int p3)
				{
					setTitle(mainViewPager.getAdapter().getPageTitle(p1));
				}

				@Override
				public void onPageSelected(int p1)
				{

				}

				@Override
				public void onPageScrollStateChanged(int p1)
				{

				}
			});

		List<View> views_adapter=new Vector<View>();
		List<CharSequence> titles_adapter=new Vector<CharSequence>();
		//view main
		{
			View main_view=getLayoutInflater().inflate(R.layout.moddedpe_main, null);

			main_view.findViewById(R.id.moddedpemainMCDPlayButton).getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth() / 3;
			((TextView)main_view.findViewById(R.id.moddedpemainTextViewAppVersion)).setText(getString(R.string.app_version));
			((TextView)main_view.findViewById(R.id.moddedpemainTextViewTargetMCVersion)).setTextColor(isSupportedMinecraftPEVersion() ?Color.GREEN: Color.RED);
			(textViewIsSafeMode = (AppCompatTextView)main_view.findViewById(R.id.moddedpemainTextViewisSafetyMode)).setVisibility(settings.getSafeMode() ?View.VISIBLE: View.GONE);
			((TextView)main_view.findViewById(R.id.moddedpemainTextViewTargetMCVersion)).setText(R.string.targetMCPEVersionInfo);


			newsLayout = new NewsLayout(this);
			((RelativeLayout)main_view.findViewById(R.id.moddedpemainNewsLayout)).addView(newsLayout);
			updateNewsLayout();

			views_adapter.add(main_view);
			titles_adapter.add(getString(R.string.main_title));
		}

		//view manage nmods
		{
			View manage_nmod_view=getLayoutInflater().inflate(R.layout.moddedpe_manage_nmod, null);

			managenmod_nmodManager = managenmod_nmodManager.getNModManager(this);

			managenmod_listViewActive = (ListView)manage_nmod_view.findViewById(R.id.moddedpemanageNModListActive);
			managenmod_listViewDisabled = (ListView)manage_nmod_view.findViewById(R.id.moddedpemanageNModListDisabled);

			managenmod_listViewActive.getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth() / 2 - 1;
			managenmod_listViewDisabled.getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth() / 2 - 1;

			refreshNModDatas();

			if (managenmod_activeList.isEmpty() && managenmod_disabledList.isEmpty())
				manage_nmod_view.findViewById(R.id.moddedpemanagenmodLayoutNoFound).setVisibility(View.VISIBLE);
			else
				manage_nmod_view.findViewById(R.id.moddedpemanagenmodLayoutNormal).setVisibility(View.VISIBLE);

			views_adapter.add(manage_nmod_view);
			titles_adapter.add(getString(R.string.manage_nmod_title));
		}

		//options
		{
			View options_view=getLayoutInflater().inflate(R.layout.moddedpe_options, null);

			options_switchSafetyMode = (MCDSwitch)options_view.findViewById(R.id.moddedpeoptionsMCDSwitchSafetyMode);
			options_switchRedstoneDot = (MCDSwitch)options_view.findViewById(R.id.moddedpeoptionsMCDSwitchRedstoneDot);
			options_switchHideDebugText = (MCDSwitch)options_view.findViewById(R.id.moddedpeoptionsMCDSwitchToggleDebugText);
			options_switchAutoSaveLevel = (MCDSwitch)options_view.findViewById(R.id.moddedpeoptionsMCDSwitchAutoSaveLevel);
			options_switchSelectAllInLeft = (MCDSwitch)options_view.findViewById(R.id.moddedpeoptionsMCDSwitchSelectAllInLeft);
			options_switchDisableTextureIsotropic = (MCDSwitch)options_view.findViewById(R.id.moddedpeoptionsMCDSwitchDisableTextureIsotropic);

			loadOptions();

			CompoundButton.OnCheckedChangeListener options_switchUpdateListener=new CompoundButton.OnCheckedChangeListener()
			{

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					refreshOptionsViews();
					saveOptions();
					textViewIsSafeMode.setVisibility(new Settings(ModdedPEMainActivity.this).getSafeMode() ?View.VISIBLE: View.GONE);
				}

			};

			options_switchSafetyMode.setOnCheckedChangeListener(options_switchUpdateListener);
			options_switchRedstoneDot.setOnCheckedChangeListener(options_switchUpdateListener);
			options_switchHideDebugText.setOnCheckedChangeListener(options_switchUpdateListener);
			options_switchAutoSaveLevel.setOnCheckedChangeListener(options_switchUpdateListener);
			options_switchSelectAllInLeft.setOnCheckedChangeListener(options_switchUpdateListener);
			options_switchDisableTextureIsotropic.setOnCheckedChangeListener(options_switchUpdateListener);

			refreshOptionsViews();

			views_adapter.add(options_view);
			titles_adapter.add(getString(R.string.settings_title));
		}

		mainViewPager.setAdapter(new ViewPagerAdapter(views_adapter, titles_adapter));
	}

	private void setViewOnClickScrollTo(View view, final int to)
	{
		view.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					mainViewPager.setCurrentItem(to);
				}

			});
	}

	private void updateNewsLayout()
	{
		Vector<NMod> nmods=managenmod_nmodManager.getNModManager(this).getActiveNModsHasNews();

		if (nmods.size() > 0)
		{
			main_showingNMod = nmods.get(new Random(System.nanoTime()).nextInt(nmods.size()));
			newsLayout.setNewsImage(main_showingNMod.getVersionImage());
			newsLayout.setNewsTitle(main_showingNMod.getNewsTitle());
		}
		else
		{
			main_showingNMod = null;
			newsLayout.setNewsImage(BitmapFactory.decodeResource(getResources(), R.drawable.image_default_minecraft));
			newsLayout.setNewsTitle(getString(R.string.main_default_minecraft_title));
		}
	}

	@Override
	protected void setDefaultActionBar()
	{
		super.setDefaultActionBar();

		MCDBurgerButton burgerButton=new MCDBurgerButton(this);
		burgerButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					setMenuActionBar();
				}


			});
		setActionBarViewRight(burgerButton);

		AppCompatImageButton imageButton=new AppCompatImageButton(this);
		imageButton.setBackgroundResource(R.drawable.mcd_creeperface);
		imageButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					startActivity(new Intent(ModdedPEMainActivity.this, ModdedPEAboutActivity.class));
				}

			});
		setActionBarViewLeft(imageButton);

		setCustomActionBar(false);
	}

	private void setMenuActionBar()
	{
		ActionBar actionBar=getSupportActionBar();

		MCDActionBarView actionBarCustomView = (MCDActionBarView)LayoutInflater.from(this).inflate(R.layout.moddedpemain_menu_actionbar, null);
		ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
		layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL;
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(actionBarCustomView, layoutParams);
		android.support.v7.widget.Toolbar parent = (android.support.v7.widget.Toolbar) actionBarCustomView.getParent();
		parent.setContentInsetsAbsolute(0, 0);

		MCDBurgerButtonClose burgerButtonClose=new MCDBurgerButtonClose(this);
		burgerButtonClose.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					setDefaultActionBar();
				}


			});
		RelativeLayout layout=(RelativeLayout)actionBarCustomView.findViewById(R.id.moddedpe_menu_actionbar_ViewRight);
		layout.removeAllViews();
		layout.addView(burgerButtonClose);

		setViewOnClickScrollTo(actionBarCustomView.findViewById(R.id.moddedpe_menu_actionbar_main), 0);
		setViewOnClickScrollTo(actionBarCustomView.findViewById(R.id.moddedpe_menu_actionbar_manage_nmod), 1);
		setViewOnClickScrollTo(actionBarCustomView.findViewById(R.id.moddedpe_menu_actionbar_settings), 2);

		setCustomActionBar(true);
	}

	@Override
	protected void onDestroy()
	{
		saveOptions();
		super.onDestroy();
	}

	private void refreshOptionsViews()
	{
		if (options_switchSafetyMode.isChecked())
		{
			options_switchRedstoneDot.setChecked(false);
			options_switchRedstoneDot.setClickable(false);
			options_switchHideDebugText.setChecked(false);
			options_switchHideDebugText.setClickable(false);
			options_switchAutoSaveLevel.setChecked(false);
			options_switchAutoSaveLevel.setClickable(false);
			options_switchSelectAllInLeft.setChecked(false);
			options_switchSelectAllInLeft.setClickable(false);
			options_switchDisableTextureIsotropic.setChecked(false);
			options_switchDisableTextureIsotropic.setClickable(false);
		}
		else
		{
			options_switchRedstoneDot.setClickable(true);
			options_switchHideDebugText.setClickable(true);
			options_switchAutoSaveLevel.setClickable(true);
			options_switchSelectAllInLeft.setClickable(true);
			options_switchDisableTextureIsotropic.setClickable(true);
		}
	}

	private void saveOptions()
	{
		Settings settings=new Settings(this);
		settings.setRedstoneDot(options_switchRedstoneDot.isChecked());
		settings.setSafeMode(options_switchSafetyMode.isChecked());
		settings.setHideDebugText(options_switchHideDebugText.isChecked());
		settings.setAutoSaveLevel(options_switchAutoSaveLevel.isChecked());
		settings.setSelectAllInLeft(options_switchSelectAllInLeft.isChecked());
		settings.setDisableTextureIsotropic(options_switchDisableTextureIsotropic.isChecked());
	}

	private void loadOptions()
	{
		Settings settings=new Settings(this);
		options_switchSafetyMode.setChecked(settings.getSafeMode());
		options_switchRedstoneDot.setChecked(settings.getRedstoneDot());
		options_switchHideDebugText.setChecked(settings.getHideDebugText());
		options_switchAutoSaveLevel.setChecked(settings.getAutoSaveLevel());
		options_switchSelectAllInLeft.setChecked(settings.getSelectAllInLeft());
		options_switchDisableTextureIsotropic.setChecked(settings.getDisableTextureIsotropic());
	}

	private Context getMcContext()
	{
		try
		{
			return createPackageContext(ModdedPEApplication.MC_PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	private boolean isSupportedMinecraftPEVersion()
	{
		if (getMcContext() == null)
			return false;
		try
		{
			String mcpeVersionName=getMcContext().getPackageManager().getPackageInfo(getMcContext().getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
			for (String nameItem : getResources().getStringArray(R.array.targetMCPEVersions))
			{
				if (nameItem.equals(mcpeVersionName))
					return true;
			}
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		return false;
	}

	private String getMinecraftPEVersionName()
	{
		if (getMcContext() == null)
			return null;
		try
		{
			return getMcContext().getPackageManager().getPackageInfo(getMcContext().getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		return null;
	}

	public void onNewsClicked(View view)
	{
		if (main_showingNMod != null)
			ModdedPENModDescriptionActivity.startThisActivity(this, main_showingNMod);
	}

	public void onPlayClicked(View v)
	{
		if (getMcContext() == null)
		{
			android.support.v7.app.AlertDialog.Builder mdialog = new AlertDialog.Builder(this);
			mdialog.setTitle(getString(R.string.no_mcpe_found_title));
			mdialog.setMessage(getString(R.string.no_mcpe_found));
			mdialog.setNegativeButton(getString(R.string.no_mcpe_found_cancel), new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int id)
					{
						p1.dismiss();
					}
				});
			mdialog.show();
		}
		else if (!isSupportedMinecraftPEVersion())
		{
			android.support.v7.app.AlertDialog.Builder mdialog = new AlertDialog.Builder(this);
			mdialog.setTitle(getString(R.string.no_available_mcpe_version_found_title));
			mdialog.setMessage(getString(R.string.no_available_mcpe_version_found, new String[]{getMinecraftPEVersionName(),getString(R.string.targetMCPEVersionInfo)}));
			mdialog.setNeutralButton(getString(R.string.no_available_mcpe_version_cancel), new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int id)
					{
						p1.dismiss();
					}
				});
			mdialog.setNegativeButton(getString(R.string.no_available_mcpe_version_continue), new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int id)
					{
						startMinecraft();
					}


				});
			mdialog.show();
			return;
		}
		else
			startMinecraft();
	}

	private void startMinecraft()
	{
		Intent i=null;
		if (new Settings(this).getSafeMode())
			i = new Intent(this, com.mcal.ModdedPE.app.ModdedPESafetyModeMinecraftActivity.class);
		else
			i = new Intent(this, com.mcal.ModdedPE.app.ModdedPEMinecraftActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}


	private class ViewPagerAdapter extends PagerAdapter
	{
		private List<View> views;
		private List<CharSequence> titles;

		public ViewPagerAdapter(List<View> views, List<CharSequence> titles)
		{
			this.views = views;
			this.titles = titles;
		}

		@Override
		public int getCount()
		{
			return this.views.size();
		}
		@Override
		public boolean isViewFromObject(View p1, Object p2)
		{
			return p1 == p2;
		}
		@Override
		public View instantiateItem(ViewGroup container, int position)
		{
			container.addView(this.views.get(position));
			return this.views.get(position);
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView(this.views.get(position));
		}
		@Override
		public CharSequence getPageTitle(int position)
		{
			return titles.get(position);
		}
	}

	//Manage nmod

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

			LayoutInflater inflater=getLayoutInflater();
			convertView = inflater.inflate(R.layout.moddedpe_nmod_item_active, null);

			AppCompatTextView title=(AppCompatTextView)convertView.findViewById(R.id.moddedpenmoditemactiveTextViewTitle);
			title.setText(itemNMod.getName());

			Button btnDescription=(Button)convertView.findViewById(R.id.moddedpenmoditemactiveButtonDescription);
			btnDescription.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						ModdedPENModDescriptionActivity.startThisActivity(ModdedPEMainActivity.this, itemNMod);
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

		updateNewsLayout();
	}

	public class AdapterDisabled extends BaseAdapter 
    {
		@Override 
		public int getCount()
		{
			return managenmod_disabledList.size();
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
			if (position >= getCount())
				return convertView;

			final int itemPosition=position;
			final NMod itemNMod = managenmod_disabledList.get(itemPosition);

			LayoutInflater inflater=getLayoutInflater();
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
							ModdedPENModDescriptionActivity.startThisActivity(ModdedPEMainActivity.this, itemNMod);
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
							ModdedPENModLoadFailActivity.startThisActivity(ModdedPEMainActivity.this, itemNMod);
						}
					});
			}

			return convertView; 
		}

    }
}
