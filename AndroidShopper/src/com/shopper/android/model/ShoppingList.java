package com.shopper.android.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingList {

	private long id;
	private String name;

	public ShoppingList(JSONObject jsonList) throws JSONException {
		this.name = jsonList.getString("name");
		this.id = jsonList.getInt("id");
	}

	public ShoppingList(int id, String name) {
		this.name = name;
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
