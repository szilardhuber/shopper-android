package com.shopzenion.database;

public class DBConstant {

	private DBConstant() {
	}

	// Database Name
	public static final String DATABASE_NAME = "shopzenion";

	// Database Version
	public static int DATABASE_VERSION = 1;

	public static final String TABLE_SHOPPING_LIST_ITEM = "shopping_list_item";

	public static final String CREATE_TABLE_SHOPPING_LIST = "CREATE TABLE shopping_list ( "
			+ "id INTEGER PRIMARY KEY, title TEXT NOT NULL,"
			+ "shopping_date INTEGER );";

	public static final int DEFAULT_SHOPPING_LIST_ID = 1;

	public static final String CREATE_TABLE_SHOPPING_LIST_ITEMS = "CREATE TABLE "
			+ TABLE_SHOPPING_LIST_ITEM
			+ " ("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "description TEXT NOT NULL,"
			+ "quantity INTEGER NOT NULL,"
			+ "rank INTEGER NOT NULL,"
			+ "purchased INTEGER NOT NULL DEFAULT 0,"
			+ "shopping_list INTEGER,"
			+ "product_barcode INTEGER,"
			+ "note TEXT,"
			+ "FOREIGN KEY (shopping_list) REFERENCES shopping_list(id));";

	public static final String SELECT_SHOPPING_LIST_ITEMS = "SELECT id, description, quantity, rank, "
			+ "purchased, product_barcode, note "
			+ "FROM "
			+ TABLE_SHOPPING_LIST_ITEM
			+ " WHERE shopping_list = ? "
			+ "ORDER BY rank;";

	public static final String SELECT_SHOPPING_LIST_ITEM = "SELECT description, quantity, rank, "
			+ "purchased, shopping_list, product_barcode, note "
			+ "FROM "
			+ TABLE_SHOPPING_LIST_ITEM + " WHERE id = ?;";

	public static final String UPDATE_RANK = "UPDATE "
			+ TABLE_SHOPPING_LIST_ITEM + " SET rank=? WHERE id = ?;";

	public static final String PUSH_RANK = "UPDATE " + TABLE_SHOPPING_LIST_ITEM
			+ " SET rank=rank+1 WHERE shopping_list = ? AND rank > ?;";

	public static final String PULL_RANK = "UPDATE " + TABLE_SHOPPING_LIST_ITEM
			+ " SET rank=rank-1 WHERE shopping_list = ? AND rank < ?;";
}