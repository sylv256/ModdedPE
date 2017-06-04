package com.mcal.ModdedPE.app;
import com.mcal.MCDesign.app.*;
import com.mcal.ModdedPE.*;
import com.mcal.pesdk.nmod.*;

public class ModdedPEActivity extends MCDActivity
{
	protected NModAPI getNModAPI()
	{
		return ModdedPEApplication.mNModAPI;
	}
}
