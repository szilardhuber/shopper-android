package com.shopper.android.activity;

import android.content.Intent;
import android.os.Bundle;

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
		Intent shoppingListIntent = new Intent(Shopper.this,
				ShoppingListActivity.class);
		startActivity(shoppingListIntent);
	}
}
