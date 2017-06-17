package org.mcal.moddedpe.app;
import android.os.*;
import android.view.*;
import org.mcal.moddedpe.*;

public class MainStartFragment extends BaseFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View main_view = inflater.inflate(R.layout.moddedpe_main, null);
		return main_view;
	}
}
