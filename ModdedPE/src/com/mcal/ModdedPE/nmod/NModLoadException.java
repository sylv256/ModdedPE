package com.mcal.ModdedPE.nmod;
import android.content.res.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nativeapi.*;

public class NModLoadException extends Exception
{
	private String mFailMsg = null;
	
	public NModLoadException(String failMsg,Throwable throwable)
	{
		super(failMsg,throwable);
	}
	
	public NModLoadException(String failMsg)
	{
		super(failMsg);
	}
	
	public String getMessage()
	{
		if(mFailMsg == null)
			return toString();
		return mFailMsg;
	}
}
