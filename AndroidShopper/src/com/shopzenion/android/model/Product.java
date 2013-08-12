package com.shopzenion.android.model;

public class Product {

	private String name;
	private long barcode;

	public Product(String name, long barcode) {
		super();
		this.name = name;
		this.barcode = barcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getBarcode() {
		return barcode;
	}

	public void setBarcode(long barcode) {
		this.barcode = barcode;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", barcode=" + barcode + "]";
	}

}
