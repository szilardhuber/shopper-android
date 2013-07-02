package com.shopper.android.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.shopper.android.ProgressController;
import com.shopper.android.R;
import com.shopper.android.model.ApplicationModel;
import com.shopper.android.server.ServerRequest;
import com.shopper.android.server.ServerResponse;
import com.shopper.android.server.ServerResponseCallback;

public class RequireLoginActivityHelper implements ServerResponseCallback {

	private Activity owner;
	private ServerResponseCallback callback;

	public RequireLoginActivityHelper(Activity owner,
			ServerResponseCallback callback) {
		this.owner = owner;
		this.callback = callback;
	}

	private static final String INTENT_LOGOUT = "INTENT_LOGOUT";

	private final BroadcastReceiver logoutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			owner.finish();
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(INTENT_LOGOUT);
		owner.registerReceiver(logoutReceiver, intentFilter);
		if (!ApplicationModel.getInstance(owner).isLogedIn()) {
			openLoginActivity();
		}
	}

	protected void onResume() {
		if (!ApplicationModel.getInstance(owner).isLogedIn()) {
			openLoginActivity();
		}
	}

	protected void onDestroy() {
		owner.unregisterReceiver(logoutReceiver);
	}

	private void openLoginActivity() {
		Intent intent = new Intent(owner, LoginActivity.class);
		owner.startActivity(intent);
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(INTENT_LOGOUT);
		owner.sendBroadcast(broadcastIntent);
	}

	private void logout() {
		ApplicationModel.getInstance(owner).setSessionId(null);
		ApplicationModel.getInstance(owner).setToken(null);
		openLoginActivity();
	}

	protected void getURL(String url) {
		ProgressController.getInstance().showProgress(R.string.progress, owner);
		new ServerRequest().sendGet(url, owner, callback);
	}

	@Override
	public void gotResponse(ServerResponse response) {
		ProgressController.getInstance().hideProgress();
		if (response.getStatus() == SecurityHandler.LOGIN_REQUIRED) {
			openLoginActivity();
		}
	}

	@Override
	public void cancelled() {
		ProgressController.getInstance().hideProgress();
	}

	@Override
	public void offline() {
		// TODO: implement this
		ProgressController.getInstance().hideProgress();
	}
}