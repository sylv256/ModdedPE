package com.mcal.ModdedPE.app;
import android.os.*;
import android.view.*;
import com.mcal.ModdedPE.*;

public class MainStartFragment extends ModdedPEFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View main_view = inflater.inflate(R.layout.moddedpe_main, null);
		return main_view;
	}
}
