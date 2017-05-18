package com.mcal.ModdedPE.app;
import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import com.mcal.ModdedPE.widget.*;
import android.support.v7.widget.*;
import com.mcal.ModdedPE.nmod.*;
import com.mcal.ModdedPE.*;
import android.widget.*;
import com.mcal.ModdedPE.utils.*;

public class MainStartFragment extends Fragment
{
	private AppCompatTextView textViewIsSafeMode;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View main_view = inflater.inflate(R.layout.moddedpe_main, null);

		main_view.findViewById(R.id.moddedpemainMCDPlayButton).getLayoutParams().width = getActivity().getWindowManager().getDefaultDisplay().getWidth() / 3;
		((TextView)main_view.findViewById(R.id.moddedpemainTextViewAppVersion)).setText(getString(R.string.app_version));
		//((TextView)main_view.findViewById(R.id.moddedpemainTextViewTargetMCVersion)).setTextColor(isSupportedMinecraftPEVersion() ?Color.GREEN: Color.RED);
		(textViewIsSafeMode = (AppCompatTextView)main_view.findViewById(R.id.moddedpemainTextViewisSafetyMode)).setVisibility(new UtilsSettings(getContext()).getSafeMode() ?View.VISIBLE: View.GONE);
		((TextView)main_view.findViewById(R.id.moddedpemainTextViewTargetMCVersion)).setText(R.string.target_mcpe_version_info);

		return main_view;
	}
}
