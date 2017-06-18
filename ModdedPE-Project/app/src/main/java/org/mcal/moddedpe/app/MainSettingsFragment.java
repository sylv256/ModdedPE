package org.mcal.moddedpe.app;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import org.mcal.mcdesign.widget.*;
import org.mcal.moddedpe.*;

public class MainSettingsFragment extends BaseFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View options_view=inflater.inflate(R.layout.moddedpe_options, null);

		return options_view;
	}
}
