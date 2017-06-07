package com.mcal.pesdk.nmod;

public class LoadFailedException extends Exception
{
	private String mFailMsg = null;
	private Throwable mCause = null;
	
	public static final int TYPE_LOADING_LIB = 1;
	//public static final int TYPE_
	
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
