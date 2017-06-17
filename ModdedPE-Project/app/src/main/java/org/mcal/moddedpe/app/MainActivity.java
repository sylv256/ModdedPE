package org.mcal.moddedpe.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import org.mcal.moddedpe.R;
import org.mcal.moddedpe.utils.UtilsSettings;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
{
	private ViewPager mMainViewPager;
	private MainManageNModFragment mManageNModFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_main_pager);

		ArrayList<Fragment> fragment_list=new ArrayList<Fragment>();
		ArrayList<CharSequence> titles_list=new ArrayList<CharSequence>();

		MainStartFragment startFragment = new MainStartFragment();
		fragment_list.add(startFragment);
		titles_list.add(getString(R.string.main_title));

		mManageNModFragment = new MainManageNModFragment();
		fragment_list.add(mManageNModFragment);
		titles_list.add(getString(R.string.manage_nmod_title));

		MainSettingsFragment settingsFragment = new MainSettingsFragment();
		fragment_list.add(settingsFragment);
		titles_list.add(getString(R.string.settings_title));

		MainFragmentPagerAdapter pagerAdapter = new MainFragmentPagerAdapter(fragment_list, titles_list);

		mMainViewPager = (ViewPager) findViewById(R.id.moddedpe_main_view_pager);
		mMainViewPager.setAdapter(pagerAdapter);
		mMainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
			{
				@Override
				public void onPageScrolled(int p1, float p2, int p3)
				{
					setTitle(mMainViewPager.getAdapter().getPageTitle(p1));
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
	}

	@Override
	protected void setDefaultActionBar()
	{
		super.setDefaultActionBar();

		final View burgerButton=getLayoutInflater().inflate(R.layout.moddedpe_ui_button_menu, null);
		burgerButton.findViewById(R.id.moddedpe_ui_button_item_image_button).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					PopupMenu popup =new PopupMenu(MainActivity.this, burgerButton);
                    popup.getMenuInflater().inflate(R.menu.moddedpe_main_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
						{
							public boolean onMenuItemClick(MenuItem item)
							{
								switchViewPager(item);
								return true;
							}
						});
					popup.show();
				}
			});
		setActionBarViewRight(burgerButton);

		View imageButtonCreeper=getLayoutInflater().inflate(R.layout.moddedpe_ui_button_creeper, null);
		imageButtonCreeper.findViewById(R.id.moddedpe_ui_button_item_image_button).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					startActivity(new Intent(MainActivity.this, AboutActivity.class));
				}

			});
		setActionBarViewLeft(imageButtonCreeper);
	}

	private void switchViewPager(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.item_main_page:
				mMainViewPager.setCurrentItem(0, true);
				break;
			case R.id.item_manage_nmods:
				mMainViewPager.setCurrentItem(1, true);
				break;
			case R.id.item_launcher_settings:
				mMainViewPager.setCurrentItem(2, true);
				break;
		}
	}

	public void onPlayClicked(View v)
	{
		if (!getPESdk().getMinecraftInfo().isMinecraftInstalled())
		{
			android.support.v7.app.AlertDialog.Builder mdialog = new AlertDialog.Builder(this);
			mdialog.setTitle(getString(R.string.no_mcpe_found_title));
			mdialog.setMessage(getString(R.string.no_mcpe_found));
			mdialog.setPositiveButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int id)
					{
						p1.dismiss();
					}
				});
			mdialog.show();
		}
		else if (!getPESdk().getMinecraftInfo().isSupportedMinecraftVersion(getResources().getStringArray(R.array.target_mcpe_versions)))
		{
			android.support.v7.app.AlertDialog.Builder mdialog = new AlertDialog.Builder(this);
			mdialog.setTitle(getString(R.string.no_available_mcpe_version_found_title));
			mdialog.setMessage(getString(R.string.no_available_mcpe_version_found, new Object[]{getPESdk().getMinecraftInfo().getMinecraftVersionName(),getString(R.string.target_mcpe_version_info)}));
			mdialog.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int id)
					{
						p1.dismiss();
					}
				});
			mdialog.setPositiveButton(getString(R.string.no_available_mcpe_version_continue), new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int id)
					{
						startMinecraft();
					}


				});
			mdialog.show();
		}
		else
			startMinecraft();
	}

	private void startMinecraft()
	{
		if (new UtilsSettings(MainActivity.this).isSafeMode())
		{
			new AlertDialog.Builder(this).setTitle(R.string.safe_mode_on_title).setMessage(R.string.safe_mode_on_message).
				setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						Intent intent = null;
						intent = new Intent(MainActivity.this, PreloadActivity.class);
						startActivity(intent);
						finish();
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
		else
		{
			Intent intent = null;
			intent = new Intent(MainActivity.this, PreloadActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		mManageNModFragment.onActivityResult(requestCode, resultCode, data);
	}

	private class MainFragmentPagerAdapter extends FragmentPagerAdapter
	{
		private List<Fragment> mFragments;
		private List<CharSequence> mTitles;
		public MainFragmentPagerAdapter(List<Fragment>fragments, List<CharSequence>titles)
		{
			super(getSupportFragmentManager());
			this.mFragments = fragments;
			this.mTitles = titles;
		}

		@Override
		public int getCount()
		{
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int p1)
		{
			return mFragments.get(p1);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return mTitles.get(position);
		}
	}
}
