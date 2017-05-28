package com.mcal.ModdedPE.app;
import android.support.v4.app.*;
import com.mcal.ModdedPE.nmod.*;

public class ModdedPEFragment extends Fragment
{
	private NModAPI mNModAPI = null;

	public ModdedPEFragment()
	{
		mNModAPI = NModAPI.getInstance(this.getContext());
	}

	protected NModAPI getNModAPI()
	{
		return mNModAPI;
	}
}
