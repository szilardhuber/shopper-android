package com.shopper.android.model;

public class Product {

	private String name;
	private double quantity;
	
	public Product(String name, double quantity) {
		super();
		this.name = name;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", quantity=" + quantity + "]";
	}
	
}
