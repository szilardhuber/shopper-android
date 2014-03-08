package com.shopzenion.android.model;

public class ShoppingListItem {

	private long id;
	private Product product;
	private int quantity;
	private int rank;
	private int purchased;
	private long shoppingListId;
	private String note;

	public ShoppingListItem(long id, String description, int quantity,
			int rank, int purchased, long shoppingListId, long productBarcode,
			String note) {
		super();
		this.id = id;
		this.product = new Product(description, productBarcode);
		this.quantity = quantity;
		this.rank = rank;
		this.purchased = purchased;
		this.shoppingListId = shoppingListId;
		this.note = note;
	}

	public long getId() {
		return id;
	}

	public void setId(long ret) {
		this.id = ret;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getPurchased() {
		return purchased;
	}

	public void setPurchased(int purchased) {
		this.purchased = purchased;
	}

	public long getShoppingListId() {
		return shoppingListId;
	}

	public void setShoppingListId(long shoppingListId) {
		this.shoppingListId = shoppingListId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "ShoppingListItem [id=" + id + ", product=" + product
				+ ", quantity=" + quantity + ", rank=" + rank + ", purchased="
				+ purchased + ", shoppingListId=" + shoppingListId + ", note="
				+ note + "]";
	}

}
