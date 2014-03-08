package com.shopzenion.android.activity;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.shopzenion.android.R;
import com.shopzenion.android.model.ShoppingListItem;
import com.shopzenion.android.util.Logger;
import com.shopzenion.database.DBHandler;

public final class ShoppingListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<ShoppingListItem> listItems;
	private DBHandler dbHandler;
	private int shoppingListItem;

	public ShoppingListAdapter(Context context,
			List<ShoppingListItem> listItems, DBHandler dbHandler,
			int shoppingListItem) {
		inflater = LayoutInflater.from(context);
		this.listItems = listItems;
		this.dbHandler = dbHandler;
		this.shoppingListItem = shoppingListItem;
	}

	public int getCount() {
		return listItems.size();
	}

	public ShoppingListItem getItem(int position) {
		return listItems.get(position);
	}

	public long getItemId(int position) {
		return listItems.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ShoppingListItem currentItem = listItems.get(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(shoppingListItem, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.itemname);
			convertView.setTag(holder);

			Button quantity = (Button) convertView.findViewById(R.id.quantity);
			Logger.debug("currentItem: " + currentItem);
			quantity.setText("" + currentItem.getQuantity());

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String name = currentItem.getProduct().getName();
		holder.text.setText(name);
		convertView.setBackgroundColor(0x00ff00);
		return convertView;
	}

	static class ViewHolder {
		TextView text;
	}

	public void remove(int position) {
		if (position < 0 || position > listItems.size()) {
			return;
		}
		ShoppingListItem temp = listItems.get(position);
		dbHandler.removeShoppingListItem(temp.getId());
		listItems.remove(position);
	}

	public void move(int from, int to) {
		ShoppingListItem temp = listItems.get(from);
		listItems.remove(from);
		listItems.add(to, temp);
		dbHandler.moveShoppingListItem(temp.getShoppingListId(), temp.getId(),
				from, to);
	}
}