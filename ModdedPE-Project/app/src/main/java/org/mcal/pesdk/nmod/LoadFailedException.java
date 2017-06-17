package org.mcal.pesdk.nmod;

public class LoadFailedException extends Exception
{
	private int mType;
	
	public static final int TYPE_LOAD_LIB_FAILED = 1;
	//public static final int TYPE_
	
	public LoadFailedException(String failMsg,Throwable throwable)
	{
		super(throwable);
	}
	
	public LoadFailedException(String failMsg)
	{
		super(new Exception(failMsg));
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
	
	public String toTypeString()
	{
		return "TYPE";
	}
}
