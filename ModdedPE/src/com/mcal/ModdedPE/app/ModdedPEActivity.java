package com.mcal.ModdedPE.app;
import com.mcal.MCDesign.app.*;
import com.mcal.ModdedPE.nmod.*;

public class ModdedPEActivity extends MCDActivity
{
	private NModAPI mNModAPI = null;
	
	public ModdedPEActivity()
	{
		mNModAPI = NModAPI.getInstance(this);
	}
	
	protected NModAPI getNModAPI()
	{
		return mNModAPI;
	}
}
