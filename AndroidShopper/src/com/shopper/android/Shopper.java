package com.shopper.android;

import android.os.Bundle;
import android.view.ViewGroup;

import com.shopper.android.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class Shopper extends RequireLoginActivity{
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ViewGroup vg = (ViewGroup) findViewById(R.id.main_content);
        ViewGroup.inflate(Shopper.this, R.layout.activity_main, vg);
    }
}
