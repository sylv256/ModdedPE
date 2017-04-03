package com.mcal.MCDesign.app;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.mcal.ModdedPE.*;

public class MCDDialog extends Dialog
{
	public MCDDialog(android.content.Context context)
	{
		super(context, android.R.style.Theme_Translucent);
		show();
	}
	
	private boolean mCancelAble=true;

	@Override
	public void setCancelable(boolean flag)
	{
		super.setCancelable(mCancelAble=flag);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mcd_dialog);
		
		final RelativeLayout view = (RelativeLayout)findViewById(R.id.contentDialog);
		RelativeLayout backView = (RelativeLayout)findViewById(R.id.dialog_rootView);
		backView.setOnTouchListener(new OnTouchListener() 
			{
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if ((event.getX() < view.getLeft() || event.getX() >view.getRight()|| event.getY() > view.getBottom() || event.getY() < view.getTop())&&MCDDialog.this.mCancelAble)
					{
						dismiss();
					}
					return false;
				}
			});
		
	}
	
	public void setTitleText(String title)
	{
		TextView tv=(TextView)findViewById(R.id.mcddialogAppCompatTextViewTitle);
		tv.setText(title);
	}
	
	public TextView getTitleTextView()
	{
		TextView tv=(TextView)findViewById(R.id.mcddialogAppCompatTextViewTitle);
		return tv;
	}

	public void setText(String text)
	{
		TextView tv=(TextView)findViewById(R.id.mcddialogMainText);
		tv.setText(text);
	}
	
	public TextView getTextTextView()
	{
		TextView tv=(TextView)findViewById(R.id.mcddialogMainText);
		return tv;
	}
	
	public void setNegativeButton(String name)
	{
		setNegativeButton(name,null);
	}
	
	public void setPositiveButton(String name)
	{
		setPositiveButton(name,null);
	}
	
	public void setNegativeButton(String name,View.OnClickListener l)
	{
		Button btn=(Button)findViewById(R.id.mcddialogbuttonLeft);
		btn.setVisibility(View.VISIBLE);
		btn.setText(name);
		btn.setOnClickListener(l);
	}

	public void setPositiveButton(String name,View.OnClickListener l)
	{
		Button btn=(Button)findViewById(R.id.mcddialogbuttonRight);
		btn.setVisibility(View.VISIBLE);
		btn.setText(name);
		btn.setOnClickListener(l);
	}
}
