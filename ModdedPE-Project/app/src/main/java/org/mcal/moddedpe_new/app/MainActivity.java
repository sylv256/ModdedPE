package org.mcal.moddedpe_new.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import org.mcal.moddedpe_new.R;
import org.mcal.moddedpe_new.utils.UtilsSettings;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
{
	private ViewPager mMainViewPager;
	private MainManageNModFragment mManageNModFragment;
	private MainSettingsFragment mMainSettingsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moddedpe_main_pager);

		ArrayList<Fragment> fragment_list=new ArrayList<>();
		ArrayList<CharSequence> titles_list=new ArrayList<>();

		MainStartFragment startFragment = new MainStartFragment();
		fragment_list.add(startFragment);
		titles_list.add(getString(R.string.main_title));

		mManageNModFragment = new MainManageNModFragment();
		fragment_list.add(mManageNModFragment);
		titles_list.add(getString(R.string.manage_nmod_title));

		mMainSettingsFragment = new MainSettingsFragment();
		fragment_list.add(mMainSettingsFragment);
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
	}

	private void switchViewPager(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.item_main_page:
				mMainViewPager.setCurrentItem(0, false);
				break;
			case R.id.item_manage_nmods:
				mMainViewPager.setCurrentItem(1, false);
				break;
			case R.id.item_launcher_settings:
				mMainViewPager.setCurrentItem(2, false);
				break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		mManageNModFragment.onActivityResult(requestCode, resultCode, data);
		mMainSettingsFragment.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		String errorString = new UtilsSettings(this).getOpenGameFailed();
		if (errorString != null)
		{
			new AlertDialog.Builder(this).setTitle(R.string.launch_failed_title).setMessage(getString(R.string.launch_failed_message, new Object[]{errorString})).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				}).show();
			new UtilsSettings(this).setOpenGameFailed(null);
		}
	}

	private class MainFragmentPagerAdapter extends FragmentPagerAdapter
	{
		private List<Fragment> mFragments;
		private List<CharSequence> mTitles;
		MainFragmentPagerAdapter(List<Fragment>fragments, List<CharSequence>titles)
		{
			super(getFragmentManager());
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
