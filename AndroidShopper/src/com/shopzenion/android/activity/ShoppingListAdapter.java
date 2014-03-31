package com.shopzenion.android.activity;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shopzenion.android.R;
import com.shopzenion.android.database.DBHandler;
import com.shopzenion.android.model.ShoppingListItem;
import com.shopzenion.android.util.GradientColor;
import com.shopzenion.android.util.Logger;

public final class ShoppingListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<ShoppingListItem> listItems;
	private DBHandler dbHandler;
	private int shoppingListItem;
	private GradientColor gradientColor;
	private Typeface font;

	public ShoppingListAdapter(Context context,
			List<ShoppingListItem> listItems, DBHandler dbHandler,
			int shoppingListItem) {
		inflater = LayoutInflater.from(context);
		this.listItems = listItems;
		this.dbHandler = dbHandler;
		this.shoppingListItem = shoppingListItem;
		this.gradientColor = new GradientColor(context.getResources().getColor(
				R.color.background_start_color), context.getResources()
				.getColor(R.color.background_end_color), listItems.size());
		font = Typeface.createFromAsset(context.getAssets(),
				"fonts/didactgothic.ttf");
		// font = Typeface
		// .createFromAsset(context.getAssets(), "DidactGothic.ttf");
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
			holder.text = (EditText) convertView.findViewById(R.id.itemname);
			convertView.setTag(holder);

			Button quantity = (Button) convertView.findViewById(R.id.quantity);
			Logger.debug("currentItem: " + currentItem);
			quantity.setText("" + currentItem.getQuantity());

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// colorize background
		LinearLayout item = (LinearLayout) convertView.findViewById(R.id.item);
		int itemColor = gradientColor.getColor(position);
		Logger.debug("Color " + position + ": "
				+ Integer.toHexString(itemColor));
		item.setBackgroundColor(itemColor);

		String name = currentItem.getProduct().getName();
		holder.text.setText(name);
		holder.text.setTypeface(font);

		makeItemEditable(holder.text, currentItem.getId());
		return convertView;
	}

	static class ViewHolder {
		EditText text;
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

	private void makeItemEditable(EditText et, final long itemId) {
		et.setFocusable(true);
		et.setEnabled(true);
		et.setClickable(true);
		et.setFocusableInTouchMode(true);
		et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				Logger.debug("action: " + actionId);
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| actionId == EditorInfo.IME_ACTION_DONE
						|| event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					Logger.debug("DOBE");
					dbHandler.setDescription(itemId, v.getText().toString());

					return true;
				}
				return false;
			}
		});
	}
}