package com.mcal.ModdedPE.nmodpe;

public class NModPELoadException extends Exception
{
	public static final int UNKNOW=0;
	public static final int BAD_JNIONLOAD_RETURN_VALUE=1;
	public static final int CANNOT_LOCATE_SYMBOL=2;
	public static final int CANNOT_DLOPEN_LIB=3;
	public static final int NO_NATIVE_LIBS_FOUND=4;
	public static final int NO_LANGUAGE_FILE_FOUND=5;
	public static final int BAD_MANIFEST_GRAMMAR=6;
	
	private int type;
	private String defaultMsg;
	
	public NModPELoadException(int type,String msg)
	{
		this.type=type;
		this.defaultMsg=msg;
	}
	
	public NModPELoadException(int type,Throwable msg)
	{
		this.type=type;
		this.defaultMsg=msg.toString();
	}
	
	public int getType()
	{
		return type;
	}

	@Override
	public String toString()
	{
		return defaultMsg;
	}
}
