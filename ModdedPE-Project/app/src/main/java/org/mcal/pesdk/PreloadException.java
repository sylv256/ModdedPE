package org.mcal.pesdk;

public class PreloadException extends Exception
{
	public static final int TYPE_LOAD_LIBS_FAILED = 1;

	private int mType;
	
	public PreloadException(int type)
	{
		mType = type;
	}
	
	public PreloadException(int type,Throwable cause)
	{
		super(cause);
		mType = type;
	}
	
	public int getType()
	{
		return mType;
	}
}
