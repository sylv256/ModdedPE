package org.mcal.pesdk;

public class PreloadException extends Exception
{
	public static final int TYPE_LOAD_LIBS_FAILED = 1;
	public static final int TYPE_IO_FAILED = 2;
	public static final int TYPE_UNSUPPORTED_ABI = 3;
	
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
