package com.shopper.android.activity;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;

import com.shopper.android.model.ShoppingList;
import com.shopper.android.server.ServerResponse;
import com.shopper.android.view.ShoppingListView;

public class ShoppingListActivity extends RequireLoginActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getURL("/api/v1/Lists");
    }
	
	@Override
	public void gotResponse(ServerResponse response) {
		super.gotResponse(response);
		if (response != null) {
			ShoppingListView view = new ShoppingListView(this);
			try {
			JSONArray list = new JSONArray(response.getContent());
			for (int i = 0; i < list.length(); i++) {
				ShoppingList l;
					l = new ShoppingList(list.getJSONObject(i));
				view.addShoppingList(l);
			}
			} catch (JSONException e) {
				// TODO handle exception
				e.printStackTrace();
			}
			this.setContentView(view);	
		}
	}
}
