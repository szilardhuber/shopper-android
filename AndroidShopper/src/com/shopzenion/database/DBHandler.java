package com.shopzenion.database;

import java.util.ArrayList;
import java.util.List;

import com.shopzenion.android.model.ShoppingList;
import com.shopzenion.android.model.ShoppingListItem;
import com.shopzenion.android.util.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

	private static DBHandler instance = null;

	public static DBHandler getInstance(Context ctx) {
		if (instance == null) {
			instance = new DBHandler(ctx.getApplicationContext());
		}
		return instance;
	}

	private DBHandler(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Database Name
	private static final String DATABASE_NAME = "shopzenion";

	// Database Version
	private static int DATABASE_VERSION = 1;

	private static final String SELECT_SHOPPING_LIST_ITEMS = "SELECT id, description, quantity, rank, "
			+ "purchased, product_barcode, note "
			+ "FROM shopping_list_item WHERE shopping_list = ? "
			+ "ORDER BY rank";

	private static final String SELECT_SHOPPING_LIST_ITEM = "SELECT description, quantity, rank, "
			+ "purchased, shopping_list, product_barcode, note "
			+ "FROM shopping_list_item WHERE id = ? ";

	@Override
	public void onCreate(SQLiteDatabase db) {
		String create_table_shopping_list = "CREATE TABLE shopping_list ( "
				+ "id INTEGER PRIMARY KEY, title TEXT NOT NULL,"
				+ "shopping_date INTEGER )";
		db.execSQL(create_table_shopping_list);
		String create_table_shopping_list_items = "CREATE TABLE shopping_list_item ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "description TEXT NOT NULL,"
				+ "quantity INTEGER NOT NULL,"
				+ "rank INTEGER NOT NULL,"
				+ "purchased INTEGER NOT NULL DEFAULT 0,"
				+ "shopping_list INTEGER,"
				+ "product_barcode INTEGER,"
				+ "note TEXT,"
				+ "FOREIGN KEY (shopping_list) REFERENCES shopping_list(id))";
		db.execSQL(create_table_shopping_list_items);
		long shoppingListId = addShoppingList(new ShoppingList(1,
				"Grocery list"), db);
		addTestData(shoppingListId, db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Logger.debug("DB onUpgrade: from " + oldVersion + " to " + newVersion);
	}

	private void addTestData(long shoppingListId, SQLiteDatabase db) {
		for (int i = 0; i < 10; i++) {
			ShoppingListItem testItem = new ShoppingListItem(0, "Item " + i, i,
					i, 0, shoppingListId, 0, null);
			addShoppingListItem(testItem, db);
		}

	}

	public long addShoppingList(ShoppingList shoppingList, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put("title", shoppingList.getName());
		long ret = db.insert("shopping_list", null, values);
		shoppingList.setId(ret);
		return ret;
	}

	public List<ShoppingList> getShoppingList(boolean actual) {
		List<ShoppingList> ret = new ArrayList<ShoppingList>();
		// Select All Query
		String selectQuery = "SELECT id, name FROM shopping_list ";
		if (actual) {
			selectQuery += " WHERE shopping_date IS NULL ";
		}
		selectQuery += " ORDER BY name";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				ShoppingList hs = new ShoppingList(cursor.getInt(0),
						cursor.getString(1));
				ret.add(hs);
			} while (cursor.moveToNext());
		}
		db.close();

		return ret;
	}

	public long addShoppingListItem(ShoppingListItem shoppingListItem) {
		SQLiteDatabase db = this.getWritableDatabase();
		long ret = addShoppingListItem(shoppingListItem, db);
		db.close();
		return ret;
	}

	public long addShoppingListItem(ShoppingListItem shoppingListItem,
			SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put("description", shoppingListItem.getProduct().getName());
		values.put("quantity", shoppingListItem.getQuantity());
		values.put("rank", shoppingListItem.getRank());
		values.put("purchased", shoppingListItem.getPurchased());
		values.put("shopping_list", shoppingListItem.getShoppingListId());
		values.put("product_barcode", shoppingListItem.getProduct()
				.getBarcode());
		values.put("note", shoppingListItem.getNote());
		long ret = db.insert("shopping_list_item", null, values);
		shoppingListItem.setId(ret);
		return ret;
	}

	public List<ShoppingListItem> getShoppingListItems(int shoppingListId) {
		List<ShoppingListItem> ret = new ArrayList<ShoppingListItem>();

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(SELECT_SHOPPING_LIST_ITEMS,
				new String[] { String.valueOf(shoppingListId) });

		if (cursor.moveToFirst()) {
			do {
				ShoppingListItem item = new ShoppingListItem(cursor.getInt(0),
						cursor.getString(1), cursor.getInt(2),
						cursor.getInt(3), cursor.getInt(4), shoppingListId,
						cursor.getLong(5), cursor.getString(6));
				ret.add(item);
			} while (cursor.moveToNext());
		}
		db.close();

		return ret;
	}

	public ShoppingListItem getShoppingListItem(int shoppingListItemId) {

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(SELECT_SHOPPING_LIST_ITEM,
				new String[] { String.valueOf(shoppingListItemId) });
		ShoppingListItem item = null;
		if (cursor.moveToFirst()) {
			item = new ShoppingListItem(shoppingListItemId,
					cursor.getString(0), cursor.getInt(1), cursor.getInt(2),
					cursor.getInt(3), cursor.getInt(4), cursor.getLong(5),
					cursor.getString(6));
		}
		db.close();

		return item;
	}

}