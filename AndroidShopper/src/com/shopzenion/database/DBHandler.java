package com.shopzenion.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shopzenion.android.model.ShoppingList;
import com.shopzenion.android.model.ShoppingListItem;
import com.shopzenion.android.util.Logger;

public class DBHandler extends SQLiteOpenHelper {

	private SQLiteDatabase db;

	public DBHandler(Context ctx) {
		super(ctx, DBConstant.DATABASE_NAME, null, DBConstant.DATABASE_VERSION);
	}

	public void open() throws SQLException {
		Logger.debug("open db");
		if (db == null) {
			db = this.getWritableDatabase();
		}
	}

	public void close() throws SQLException {
		Logger.debug("close db");
		if (db != null) {
			db.close();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Logger.debug("create database");
		db.execSQL(DBConstant.CREATE_TABLE_SHOPPING_LIST);
		db.execSQL(DBConstant.CREATE_TABLE_SHOPPING_LIST_ITEMS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Logger.debug("DB onUpgrade: from " + oldVersion + " to " + newVersion);
	}

	public void addDefaultShoppingList() {
		addShoppingList(new ShoppingList(DBConstant.DEFAULT_SHOPPING_LIST_ID,
				"DFghsghsdgths"));
	}

	public void addTestData(long shoppingListId) {
		for (int i = 0; i < 10; i++) {
			int quantity = i * 12;
			ShoppingListItem testItem = new ShoppingListItem(0, "Item " + i,
					quantity, i, 0, shoppingListId, 0, null);
			addShoppingListItem(testItem);
		}
	}

	public long addShoppingList(ShoppingList shoppingList) {
		ContentValues values = new ContentValues();
		values.put("title", shoppingList.getName());
		Logger.debug("db: " + (db == null));
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

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				ShoppingList sl = new ShoppingList(cursor.getInt(0),
						cursor.getString(1));
				ret.add(sl);
			} while (cursor.moveToNext());
		}
		return ret;
	}

	public long addShoppingListItem(ShoppingListItem shoppingListItem) {
		ContentValues values = new ContentValues();
		values.put("description", shoppingListItem.getProduct().getName());
		values.put("quantity", shoppingListItem.getQuantity());
		values.put("rank", shoppingListItem.getRank());
		values.put("purchased", shoppingListItem.getPurchased());
		values.put("shopping_list", shoppingListItem.getShoppingListId());
		values.put("product_barcode", shoppingListItem.getProduct()
				.getBarcode());
		values.put("note", shoppingListItem.getNote());
		long ret = db.insert(DBConstant.TABLE_SHOPPING_LIST_ITEM, null, values);
		shoppingListItem.setId(ret);
		return ret;
	}

	public List<ShoppingListItem> getShoppingListItems(int shoppingListId) {
		List<ShoppingListItem> ret = new ArrayList<ShoppingListItem>();

		Cursor cursor = db.rawQuery(DBConstant.SELECT_SHOPPING_LIST_ITEMS,
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
		return ret;
	}

	public int getDifferentQuantityCount(int shoppingListId) {
		int ret = 1;

		Cursor cursor = db.rawQuery(DBConstant.DIFFERENT_QUANTITY_COUNT,
				new String[] { String.valueOf(shoppingListId) });

		if (cursor.moveToFirst()) {
			ret = cursor.getInt(2);
		}
		return ret;
	}

	public ShoppingListItem getShoppingListItem(int shoppingListItemId) {

		Cursor cursor = db.rawQuery(DBConstant.SELECT_SHOPPING_LIST_ITEM,
				new String[] { String.valueOf(shoppingListItemId) });
		ShoppingListItem item = null;
		if (cursor.moveToFirst()) {
			item = new ShoppingListItem(shoppingListItemId,
					cursor.getString(0), cursor.getInt(1), cursor.getInt(2),
					cursor.getInt(3), cursor.getInt(4), cursor.getLong(5),
					cursor.getString(6));
		}
		return item;
	}

	private void setRank(long shoppingListItemId, int rank) {
		Object[] bindArgs = { rank, shoppingListItemId };
		db.execSQL(DBConstant.UPDATE_RANK, bindArgs);
	}

	private void pushRank(long shoppingListId, int rank) {
		Object[] bindArgs = { shoppingListId, rank };
		db.execSQL(DBConstant.PUSH_RANK, bindArgs);
	}

	private void pullRank(long shoppingListId, int rank) {
		Object[] bindArgs = { shoppingListId, rank };
		db.execSQL(DBConstant.PULL_RANK, bindArgs);
	}

	public void moveShoppingListItem(long shoppingListId,
			long shoppingListItemId, int from, int to) {
		// TODO
		if (from < to) {
			pullRank(shoppingListId, to);
			setRank(shoppingListItemId, to);
		} else if (from > to) {
			pushRank(shoppingListId, to);
			setRank(shoppingListItemId, to);
		} else {
			// not a real move
		}
	}

	public void removeShoppingListItem(long shoppingListId) {
		db.delete(DBConstant.TABLE_SHOPPING_LIST_ITEM,
				"id = " + shoppingListId, null);
	}
}