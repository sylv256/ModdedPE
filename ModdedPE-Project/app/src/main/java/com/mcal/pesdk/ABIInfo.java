package com.mcal.pesdk;
import android.os.*;

public class ABIInfo
{
	public static String getTargetABIType()
	{
		if(Build.CPU_ABI.indexOf("arm")!=-1)
		{
			return "armeabi-v7a";
		}
		else if(Build.CPU_ABI.indexOf("x86")!=-1)
		{
			return "x86";
		}
		else
			return null;
	}
}
