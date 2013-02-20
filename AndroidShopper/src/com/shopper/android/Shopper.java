package com.shopper.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.shopper.android.server.ServerResponse;
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
        getURL("/api");
    }
	
	@Override
	public void gotResponse(ServerResponse response) {
		super.gotResponse(response);
		   ViewGroup vg = (ViewGroup) findViewById(R.id.main_content);        
	        ViewGroup.inflate(Shopper.this, R.layout.activity_main, vg);
	        if (response != null) {
	        	TextView t = (TextView) vg.findViewById(R.id.content);
	        	String content = response.getContent();
	        	t.setText(content);			
	        }
	}
}
