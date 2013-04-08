package com.shopper.android.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingList {

	private int id;
	private String name;

	public ShoppingList(JSONObject jsonList) throws JSONException {
		this.name = jsonList.getString("name");
		this.id = jsonList.getInt("id");
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
