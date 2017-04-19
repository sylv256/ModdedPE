package com.mcal.MCDesign.app;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.mcal.ModdedPE.*;
import android.support.v7.app.*;
import android.view.ViewGroup.*;
import android.graphics.*;
import com.mcal.ModdedPE.resources.*;
import android.graphics.drawable.*;

public class MCDAlertDialog extends android.support.v7.app.AlertDialog
{
	protected MCDAlertDialog(android.content.Context context)
	{
		super(context, android.R.style.Theme_Translucent);
	}

	public static class Builder extends android.support.v7.app.AlertDialog.Builder
	{
		public Builder(android.content.Context context) 
		{
			super(context);
		}

        public Builder(android.content.Context context, int themeResId)
		{
			super(context,themeResId);
		}
	}
}
