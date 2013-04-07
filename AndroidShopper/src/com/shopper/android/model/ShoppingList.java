package com.shopper.android.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingList {

	private int id = 1; // TODO: make it dynamic
	private String name;

	public ShoppingList(JSONObject jsonList) throws JSONException {
		this.name = jsonList.getString("name");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
