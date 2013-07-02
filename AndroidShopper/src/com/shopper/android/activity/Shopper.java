package com.shopper.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class Shopper extends Activity{
	
	private RequireLoginActivityHelper activityHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityHelper = new RequireLoginActivityHelper(this, null);
		activityHelper.onCreate(savedInstanceState);
		Intent shoppingListIntent = new Intent(Shopper.this,
				ShoppingListActivity.class);
		startActivity(shoppingListIntent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityHelper.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		activityHelper.onResume();
	}
}
