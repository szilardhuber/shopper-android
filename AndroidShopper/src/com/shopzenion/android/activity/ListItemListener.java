package com.shopzenion.android.activity;

import android.view.View;
import android.widget.ListView;

public interface ListItemListener {

	void onDragStart(View itemView);

	void onDrag(int x, int y, ListView listView);

	void onDragStop(View itemView);

	void onDrop(int from, int to);

	void onRemove(int which);
}
