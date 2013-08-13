package com.shopzenion.android.activity;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ericharlow.DragNDrop.DropListener;
import com.ericharlow.DragNDrop.RemoveListener;
import com.shopzenion.android.model.ShoppingListItem;

public final class ShoppingListAdapter extends BaseAdapter implements
		RemoveListener, DropListener {

	private LayoutInflater inflater;
	private List<ShoppingListItem> listItems;

	public ShoppingListAdapter(Context context, List<ShoppingListItem> listItems) {
		inflater = LayoutInflater.from(context);
		this.listItems = listItems;
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(
					android.R.layout.simple_list_item_1, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(android.R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ShoppingListItem shoppingListItem = listItems.get(position);
		
		String name = shoppingListItem.getProduct().getName();
		holder.text.setText(name);

		return convertView;
	}

	static class ViewHolder {
		TextView text;
	}

	public void onRemove(int position) {
		if (position < 0 || position > listItems.size()){			
			return;
		}
		listItems.remove(position);
	}

	public void onDrop(int from, int to) {
		ShoppingListItem temp = listItems.get(from);
		listItems.remove(from);
		listItems.add(to, temp);
	}
}