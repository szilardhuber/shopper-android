package com.shopper.android.model;

public class ShoppingListItem {

	private ShoppingList shoppingList;
	private Product product;
	private double quantity;
	
	public ShoppingListItem(ShoppingList shoppingList, Product product,
			double quantity) {
		super();
		this.shoppingList = shoppingList;
		this.product = product;
		this.quantity = quantity;
	}

	public ShoppingList getShoppingList() {
		return shoppingList;
	}

	public void setShoppingList(ShoppingList shoppingList) {
		this.shoppingList = shoppingList;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
}
