package com.shopper.database;

import java.util.ArrayList;
import java.util.List;

import com.shopper.android.model.ShoppingList;
import com.shopper.android.model.ShoppingListItem;
import com.shopper.android.util.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

	// Database Name
	private static final String DATABASE_NAME = "shopzenion";

	// Database Version
	private static int DATABASE_VERSION = 1;

	private static final String SELECT_SHOPPING_LIST_ITEMS = "SELECT id, description, quantity, rank, "
			+ "purchased, product_barcode, note "
			+ "FROM shopping_list_item WHERE shopping_list = ? "
			+ "ORDER BY rank";

	public DBHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

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
				+ "FOREIGN KEY shopping_list REFERENCES shopping_list(id))";
		db.execSQL(create_table_shopping_list_items);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Logger.debug("DB onUpgrade: from " + oldVersion + " to " + newVersion);
	}

	public void addShoppingList(ShoppingList shoppingList) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("title", shoppingList.getName());
		db.insert("shopping_list", null, values);
		db.close();
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

	public List<ShoppingListItem> getShoppingListItems(int shoppingListId) {
		List<ShoppingListItem> ret = new ArrayList<ShoppingListItem>();

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(SELECT_SHOPPING_LIST_ITEMS,
				new String[] { String.valueOf(shoppingListId) });

		if (cursor.moveToFirst()) {
			do {
				// TODO
			} while (cursor.moveToNext());
		}
		db.close();

		return ret;
	}

}