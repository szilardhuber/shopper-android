package com.shopper.android;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressController {

	private static ProgressController instance;
	private ProgressDialog currentDialog;
	
	private ProgressController() {
		// singleton
	}
	
	public static ProgressController getInstance() {
		if (instance == null) {
			instance = new ProgressController();
		}
		return instance;
	}
	

	public void showProgress(int title, int message, Context ctx){
		currentDialog = new ProgressDialog(ctx);
		if (title > 0) {
			currentDialog.setTitle(ctx.getString(title));
		}
        currentDialog.setMessage(ctx.getString(message));
        currentDialog.setIndeterminate(true);
        currentDialog.setCancelable(true);
        currentDialog.show();
	}
	
	public void showProgress(int message, Context ctx){
		showProgress(-1, message, ctx);
	}
	
	public void hideProgress(){
		if (currentDialog != null) {
			currentDialog.dismiss();
		}
	}
}
