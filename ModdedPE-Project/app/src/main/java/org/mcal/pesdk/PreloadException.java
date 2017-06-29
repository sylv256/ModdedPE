package org.mcal.pesdk;

public class PreloadException extends Exception
{
	public static final int TYPE_LOAD_LIBS_FAILED = 1;
	public static final int TYPE_IO_EXCEPTION = 2;

	private int mType;

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
