package org.mcal.moddedpe_new.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import org.mcal.moddedpe_new.ModdedPEApplication;
import org.mcal.moddedpe_new.R;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class ConsoleTableView extends RelativeLayout
{
	public ConsoleTableView(Context c)
	{
		super(c);
		addTableView();
	}

	public ConsoleTableView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		addTableView();
	}

    public ConsoleTableView(Context context, AttributeSet attrs, int defStyleAttr) 
	{
		super(context, attrs, defStyleAttr);
		addTableView();
	}

    public ConsoleTableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) 
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		addTableView();
	}
	
	private void addTableView()
	{
		View rootView = LayoutInflater.from(getContext()).inflate(R.layout.moddedpe_main_console_table,null);
		
		((AppCompatTextView)rootView.findViewById(R.id.moddedpe_main_text_view_app_version)).setText(getContext().getResources().getString(R.string.app_version));
		((AppCompatTextView)rootView.findViewById(R.id.moddedpe_main_text_view_target_mc_version)).setTextColor(ModdedPEApplication.mPESdk.getMinecraftInfo().isSupportedMinecraftVersion(getContext().getResources().getStringArray(R.array.target_mcpe_versions)) ?Color.GREEN: Color.RED);
		((AppCompatTextView)rootView.findViewById(R.id.moddedpe_main_text_view_target_mc_version)).setText(R.string.target_mcpe_version_info);
		addView(rootView);
	}
}
