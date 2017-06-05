package com.mcal.pesdk.nmod;

public class ArchiveFailedException extends Exception
{
	public static final int TYPE_JSON_SYNTAX_EXCEPTION = 1;
	public static final int TYPE_IO_EXCEPTION = 2;
	public static final int TYPE_NO_MANIFEST = 3;
	public static final int TYPE_PACKAGE_NOT_FOUND = 4;
	public static final int TYPE_UNDEFINED_PACKAGE_NAME = 5;
	public static final int TYPE_INVAILD_PACKAGE_NAME = 6;
	public static final int TYPE_INEQUAL_PACKAGE_NAME = 7;
	public static final int TYPE_DECODE_FAILED = 8;
	public static final int TYPE_UNEXPECTED = 9;
	public static final int TYPE_REDUNDANT_MANIFEST = 10;
	
	private int mType;
	
	public ArchiveFailedException(int type)
	{
		mType = type;
	}
	
	public ArchiveFailedException(int type,Throwable cause)
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
			case TYPE_JSON_SYNTAX_EXCEPTION:
				return "JSON_SYNTAX_EXCEPTION";
			case TYPE_INEQUAL_PACKAGE_NAME:
				return "INEQUAL_PACKAGE_NAME";
			case TYPE_DECODE_FAILED:
				return "DECODE_FAILED";
			case TYPE_IO_EXCEPTION:
				return "IO_EXCEPTION";
			case TYPE_INVAILD_PACKAGE_NAME:
				return "INVAILD_PACKAGE_NAME";
			case TYPE_PACKAGE_NOT_FOUND:
				return "PACKAGE_NOT_FOUND";
			case TYPE_NO_MANIFEST:
				return "NO_MANIFEST";
			case TYPE_UNDEFINED_PACKAGE_NAME:
				return "UNDEFINED_PACKAGE_NAME";
			case TYPE_UNEXPECTED:
				return "UNEXPECTED";
			case TYPE_REDUNDANT_MANIFEST:
				return "REDUNDANT_MANIFEST";
			default:
				return "null";
		}
	}
}
