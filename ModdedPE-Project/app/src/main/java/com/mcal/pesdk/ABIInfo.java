package com.mcal.pesdk;
import android.os.*;

public class ABIInfo
{
	public static String getTargetABIType()
	{
		String property = System.getProperty("os.arch");
        if(property.equals("i686") || property.startsWith("x86"))
			return "x86";
		return "armeabi-v7a";
	}
}
