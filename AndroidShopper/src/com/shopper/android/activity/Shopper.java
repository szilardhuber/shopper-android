package com.shopper.android.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shopper.android.R;
import com.shopper.android.server.ServerResponse;
import com.shopper.android.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class Shopper extends RequireLoginActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getURL("/api/v1/Lists");
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
