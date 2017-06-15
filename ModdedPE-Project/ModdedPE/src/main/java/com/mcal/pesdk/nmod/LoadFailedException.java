package com.mcal.pesdk.nmod;

public class LoadFailedException extends Exception
{
	private String mFailMsg = null;
	private Throwable mCause = null;
	private int mType;
	
	public static final int TYPE_LOAD_LIB_FAILED = 1;
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
	
	public LoadFailedException(int type)
	{
		mType = type;
	}
	
	public LoadFailedException(int type,Throwable cause)
	{
		super(cause);
		mType = type;
	}
	
	public int getType()
	{
		return mType;
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
	
	public String toTypeString()
	{
		return "TYPE";
	}
}
