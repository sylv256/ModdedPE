package com.mycompany.myapp;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		((TextView)findViewById(R.id.m)).setText(getDatabasePath("lib").getPath());
    }
}
