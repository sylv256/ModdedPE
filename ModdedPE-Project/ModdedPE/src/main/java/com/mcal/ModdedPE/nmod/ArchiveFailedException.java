package com.mcal.ModdedPE.nmod;

public class ArchiveFailedException extends Exception
{
	public static final int TYPE_JSON_SYNTAX_EXCEPTION = 1;
	public static final int TYPE_IO_EXCEPTION = 2;
	public static final int TYPE_NO_MANIFEST = 3;
	public static final int TYPE_PACKAGE_NOT_FOUND = 4;
	public static final int TYPE_FILE_NOT_FOUND = 5;
	public static final int TYPE_UNDEFINED_PACKAGE_NAME = 6;
	public static final int TYPE_INVAILD_PACKAGE_NAME = 7;
	public static final int TYPE_INEQUAL_PACKAGE_NAME = 8;
	
	private int mType;
	private Throwable mCause;
	
	public ArchiveFailedException(int type)
	{
		mType = type;
	}
	
	public ArchiveFailedException(int type,Throwable cause)
	{
		mType = type;
		mCause = cause;
	}
	
	public Throwable getImportFailedCause()
	{
		return mCause;
	}
}
