package org.mcal.pesdk.nmod;

public class LoadFailedException extends Exception
{
	private int mType;
	
	public static final int TYPE_LOAD_LIB_FAILED = 1;
	public static final int TYPE_IO_FAILED = 2;
	public static final int TYPE_JSON_SYNTAX = 3;
	public static final int TYPE_FILE_NOT_FOUND = 4;
	public static final int TYPE_DECODE_FAILED = 5;
	public static final int TYPE_INVALID_SIZE = 6;
	
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
		switch(mType)
		{
			case TYPE_DECODE_FAILED:
				return "DECODE_FAILED";
			case TYPE_LOAD_LIB_FAILED:
				return "LOAD_LIB_FAILED";
			case TYPE_FILE_NOT_FOUND:
				return "FILE_NOT_FOUND";
			case TYPE_INVALID_SIZE:
				return "INVALID_SIZE";
			case TYPE_IO_FAILED:
				return "IO_FAILED";
			case TYPE_JSON_SYNTAX:
				return "JSON_SYNTAX";
		}
		return "TYPE";
	}
}
