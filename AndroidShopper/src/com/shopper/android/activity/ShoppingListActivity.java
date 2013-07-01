package com.shopper.android.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;

import com.shopper.android.R;
import com.shopper.android.model.ShoppingList;
import com.shopper.android.server.ServerResponse;

public class ShoppingListActivity extends MyListActivity  {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setLayout(R.layout.sopping_list);
		setListView(android.R.id.list);
		setListRowId(R.layout.list_row);
		super.onCreate(savedInstanceState);
		getActivityHelper().getURL("/api/v1/Lists");
	}

	@Override
	public void gotResponse(ServerResponse response) {
		getActivityHelper().gotResponse(response);
		if (response != null) {
			try {
				JSONArray list = new JSONArray(response.getContent());
				ArrayList<String> data = new ArrayList<String>();
				for (int i = 0; i < list.length(); i++) {
					ShoppingList l = new ShoppingList(list.getJSONObject(i));
					data.add(l.getName());
				}
				setData(data);
			} catch (JSONException e) {
				// TODO handle exception
				e.printStackTrace();
			}
		}
	}

}
