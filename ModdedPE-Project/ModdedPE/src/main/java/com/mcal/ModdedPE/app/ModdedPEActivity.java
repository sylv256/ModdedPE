package com.mcal.ModdedPE.app;
import com.mcal.MCDesign.app.*;
import com.mcal.ModdedPE.nmod.*;
import com.mcal.ModdedPE.*;

public class ModdedPEActivity extends MCDActivity
{
	protected NModAPI getNModAPI()
	{
		return ModdedPEApplication.mNModAPI;
	}
}
