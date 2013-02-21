package com.shopper.android;


import com.shopper.android.server.ServerRequest;
import com.shopper.android.server.ServerResponse;
import com.shopper.android.server.ServerResponseCallback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RequireLoginActivity extends HeaderFooterActivity implements ServerResponseCallback{
	private static final String INTENT_LOGOUT = "INTENT_LOGOUT";
	
	private final BroadcastReceiver logoutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.button_logout).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						logout();
					}
				});
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(INTENT_LOGOUT);
		registerReceiver(logoutReceiver, intentFilter);
		if(!ApplicationModel.getInstance(this).isLogedIn()) {
			openLoginActivity();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!ApplicationModel.getInstance(this).isLogedIn()) {
			openLoginActivity();
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(logoutReceiver);
		super.onDestroy();
	}

	private void openLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(INTENT_LOGOUT);
		sendBroadcast(broadcastIntent);
	}

	private void logout() {
		ApplicationModel.getInstance(this).setSessionId(null);
		ApplicationModel.getInstance(this).setToken(null);
		openLoginActivity();
	}
	
	protected void getURL(String url){
		setProgressMessage(R.string.progress);
		showProgress(true);
		new ServerRequest().sendGet(url, this, this);
	}

	@Override
	public void gotResponse(ServerResponse response) {
		showProgress(false);
		if (response.getStatus() == SecurityHandler.LOGIN_REQUIRED) {
			openLoginActivity();
		}
	}

	@Override
	public void cancelled() {
		showProgress(false);		
	}

}