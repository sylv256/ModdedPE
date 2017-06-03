package com.mcal.ModdedPE.app;
import android.support.v4.app.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nmod.*;

public class ModdedPEFragment extends Fragment
{
	protected NModAPI getNModAPI()
	{
		return ModdedPEApplication.mNModAPI;
	}
}
