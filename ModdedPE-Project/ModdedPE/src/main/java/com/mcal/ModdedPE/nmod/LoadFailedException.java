package com.mcal.ModdedPE.nmod;
import android.content.res.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nativeapi.*;

public class LoadFailedException extends Exception
{
	private String mFailMsg = null;
	private Throwable mCause = null;
	
	public LoadFailedException(String failMsg,Throwable throwable)
	{
		super();
		mFailMsg = failMsg;
		mCause = throwable;
	}
	
	public LoadFailedException(String failMsg)
	{
		super();
		mFailMsg = failMsg;
	}
	
	public String getNModBugMessage()
	{
		if(mFailMsg == null)
			return mCause.toString();
		return mFailMsg;
	}

	@Override
	public String toString()
	{
		return mCause.toString();
	}
}
