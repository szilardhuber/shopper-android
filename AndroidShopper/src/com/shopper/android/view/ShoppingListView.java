package com.shopper.android.view;

import com.shopper.android.model.ShoppingList;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ShoppingListView extends ScrollView {

	private TableLayout tl;
	private Context context;

	private int HEADER_FONT_SIZE;
	private int PADDING_HEADER;
	private int COLUMN_WIDTH;
	private int PADDING_LEFT;
	private int ROW_HEIGHT;
	private int LINE_MARGIN = 2;


	/**
	 * Constructor.
	 * 
	 * @param context context
	 * 
	 */
	public ShoppingListView(Context context) {
		super(context);
		this.context = context;

		setDimensions();
		createLayout(context);
	}

	private void setDimensions() {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int complexUnitDip = TypedValue.COMPLEX_UNIT_DIP;

		HEADER_FONT_SIZE = Math.round(TypedValue.applyDimension(complexUnitDip,
				20, displayMetrics));
		PADDING_HEADER = Math.round(TypedValue.applyDimension(
				complexUnitDip, 15, displayMetrics));
		COLUMN_WIDTH = Math.round(TypedValue.applyDimension(
				complexUnitDip, 170, displayMetrics));
		PADDING_LEFT = Math.round(TypedValue.applyDimension(
				complexUnitDip, 10, displayMetrics));
		ROW_HEIGHT = Math.round(TypedValue.applyDimension(
				complexUnitDip, 40, displayMetrics));
		LINE_MARGIN = Math.round(TypedValue.applyDimension(
				complexUnitDip, 1, displayMetrics));

	}

	private void createLayout(Context context) {
		// create table layout
		tl = new TableLayout(context);
		tl.setOrientation(TableLayout.VERTICAL);
		tl.setBackgroundColor(Color.GRAY);
		this.addView(tl);

		// create row for header
		TableRow header = new TableRow(context);
		header.setBackgroundColor(Color.LTGRAY);
		tl.addView(header);

		TextView listName = new TextView(context);
		listName.setTextSize(HEADER_FONT_SIZE);
		listName.setTextColor(Color.BLACK);
		listName.setText("Bevásárló lista"); //TODO: strings.xml
		listName.setWidth(COLUMN_WIDTH);
		listName.setPadding(PADDING_LEFT, PADDING_HEADER, 0, PADDING_HEADER);
		header.addView(listName);
	}


	/**
	 * @param shoppingList shoppingList to add to layout
	 */
	public void addShoppingList(ShoppingList shoppingList) {
		// row list item
		TableRow tr = new TableRow(context);
		tl.addView(tr);
		tr.setBackgroundColor(Color.WHITE);		
		TableRow.LayoutParams trLineParams = new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT, 1);
		trLineParams.setMargins(LINE_MARGIN, LINE_MARGIN, LINE_MARGIN, LINE_MARGIN);
		tr.setLayoutParams(trLineParams);

		TextView list = new TextView(context);
		list.setTextColor(Color.DKGRAY);
		list.setText(shoppingList.getName());
		list.setWidth(COLUMN_WIDTH);
		list.setPadding(PADDING_LEFT, 0, 0, 0);
		list.setHeight(ROW_HEIGHT);
		tr.addView(list);
		
		Button detailsButton = new Button(getContext());
		detailsButton.setText(">"); // TODO: Replace with icon
		tr.addView(detailsButton);
		
	}
}
