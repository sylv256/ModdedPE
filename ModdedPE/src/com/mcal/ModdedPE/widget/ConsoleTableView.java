package com.mcal.ModdedPE.widget;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.view.*;
import com.mcal.ModdedPE.*;
import android.support.v7.widget.*;
import com.mcal.ModdedPE.utils.*;

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
		AppCompatImageButton playButton = (AppCompatImageButton)rootView.findViewById(R.id.moddedpe_main_play_button);
		playButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{

				}


			});

		((AppCompatTextView)rootView.findViewById(R.id.moddedpe_main_text_view_app_version)).setText(getContext().getResources().getString(R.string.app_version));
		//((AppCompatTextView)main_view.findViewById(R.id.moddedpe_main_text_view_target_mc_version)).setTextColor(isSupportedMinecraftPEVersion() ?Color.GREEN: Color.RED);
		((AppCompatTextView)rootView.findViewById(R.id.moddedpe_main_text_view_is_safe_mode)).setVisibility(new UtilsSettings(getContext()).getSafeMode() ?View.VISIBLE: View.GONE);
		((AppCompatTextView)rootView.findViewById(R.id.moddedpe_main_text_view_target_mc_version)).setText(R.string.target_mcpe_version_info);
		addView(rootView);
	}
}
