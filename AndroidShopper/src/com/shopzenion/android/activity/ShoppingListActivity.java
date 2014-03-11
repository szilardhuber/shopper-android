package com.shopzenion.android.activity;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.shopzenion.android.R;
import com.shopzenion.android.database.DBConstant;
import com.shopzenion.android.database.DBHandler;
import com.shopzenion.android.model.ShoppingListItem;

public class ShoppingListActivity extends ListActivity {
	private DBHandler dbHandler;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.shopping_list);
		/*
		 * TextView shoppingListName = (TextView)
		 * findViewById(R.id.shoppingListName);
		 * shoppingListName.setText(R.string.default_shopping_list_name);
		 */
		dbHandler = new DBHandler(this);
		dbHandler.open();

		// TODO: remove this later
		dbHandler.addDefaultShoppingList();
		// dbHandler.addTestData(DBConstant.DEFAULT_SHOPPING_LIST_ID);

		List<ShoppingListItem> content = dbHandler
				.getShoppingListItems(DBConstant.DEFAULT_SHOPPING_LIST_ID);

		ListView listView = getListView();

		if (listView instanceof ShopingListListView) {
			((ShopingListListView) listView)
					.setListItemListener(listItemListener);
		}
		setListAdapter(new ShoppingListAdapter(this, content, dbHandler,
				R.layout.shopping_list_item));

	}

	private ListItemListener listItemListener = new ListItemListener() {
		int backgroundColor = 0xee6f510;
		int defaultBackgroundColor = 0xee6f510;

		public void onDrag(int x, int y, ListView listView) {
			// TODO Auto-generated method stub
		}

		public void onDragStart(View itemView) {
			itemView.setVisibility(View.INVISIBLE);
			defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
			itemView.setBackgroundColor(backgroundColor);
			/*
			 * ImageView iv = (ImageView)
			 * itemView.findViewById(R.id.ImageView01); if (iv != null)
			 * iv.setVisibility(View.INVISIBLE);
			 */
		}

		public void onDragStop(View itemView) {
			itemView.setVisibility(View.VISIBLE);
			itemView.setBackgroundColor(defaultBackgroundColor);
			/*
			 * ImageView iv = (ImageView)
			 * itemView.findViewById(R.id.ImageView01); if (iv != null)
			 * iv.setVisibility(View.VISIBLE);
			 */}

		public void onDrop(int from, int to) {
			ListAdapter adapter = getListAdapter();
			if (adapter instanceof ShoppingListAdapter) {
				((ShoppingListAdapter) adapter).move(from, to);
				getListView().invalidateViews();
			}
		}

		public void onRemove(int which) {
			ListAdapter adapter = getListAdapter();
			if (adapter instanceof ShoppingListAdapter) {
				((ShoppingListAdapter) adapter).remove(which);
				getListView().invalidateViews();
			}
		}

	};

	@Override
	protected void onResume() {
		dbHandler.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dbHandler.close();
		super.onPause();
	}
}