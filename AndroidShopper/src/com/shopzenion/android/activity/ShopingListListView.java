package com.shopzenion.android.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

public class ShopingListListView extends ListView {

	boolean isDragging;

	int dragStartPos;
	int dragEndPos;
	int offset;

	ImageView view;
	GestureDetector gestureDetector;
	ListItemListener listItemListener;

	public ShopingListListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final int x = (int) ev.getX();
		final int y = (int) ev.getY();

		if (action == MotionEvent.ACTION_DOWN && x < this.getWidth() / 4) {
			isDragging = true;
		}

		if (!isDragging)
			return super.onTouchEvent(ev);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			dragStartPos = pointToPosition(x, y);
			if (dragStartPos != INVALID_POSITION) {
				int pos = dragStartPos - getFirstVisiblePosition();
				offset = y - getChildAt(pos).getTop();
				offset = offset - ((int) ev.getRawY()) - y;
				startDrag(pos, y);
				drag(0, y);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			drag(0, y);
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
		default:
			isDragging = false;
			dragEndPos = pointToPosition(x, y);
			stopDrag(dragStartPos - getFirstVisiblePosition());
			if (listItemListener != null && dragStartPos != INVALID_POSITION
					&& dragEndPos != INVALID_POSITION)
				listItemListener.onDrop(dragStartPos, dragEndPos);
			break;
		}
		return true;
	}

	private void drag(int x, int y) {
		if (view != null) {
			WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) view
					.getLayoutParams();
			layoutParams.x = x;
			layoutParams.y = y - offset;
			WindowManager mWindowManager = (WindowManager) getContext()
					.getSystemService(Context.WINDOW_SERVICE);
			mWindowManager.updateViewLayout(view, layoutParams);

			if (listItemListener != null)
				listItemListener.onDrag(x, y, null);// change null to "this"
													// when
													// ready to use
		}
	}

	private void startDrag(int itemIndex, int y) {
		stopDrag(itemIndex);

		View item = getChildAt(itemIndex);
		if (item == null)
			return;
		item.setDrawingCacheEnabled(true);
		if (listItemListener != null)
			listItemListener.onDragStart(item);

		// Create a copy of the drawing cache so that it does not get recycled
		// by the framework when the list tries to clean up memory
		Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());

		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.gravity = Gravity.TOP;
		layoutParams.x = 0;
		layoutParams.y = y - offset;

		layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		layoutParams.format = PixelFormat.TRANSLUCENT;
		layoutParams.windowAnimations = 0;

		Context context = getContext();
		ImageView v = new ImageView(context);
		v.setImageBitmap(bitmap);

		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(v, layoutParams);
		view = v;
	}

	private void stopDrag(int itemIndex) {
		if (view != null) {
			if (listItemListener != null)
				listItemListener.onDragStop(getChildAt(itemIndex));
			view.setVisibility(GONE);
			WindowManager wm = (WindowManager) getContext().getSystemService(
					Context.WINDOW_SERVICE);
			wm.removeView(view);
			view.setImageDrawable(null);
			view = null;
		}
	}

	// private GestureDetector createFlingDetector() {
	// return new GestureDetector(getContext(), new SimpleOnGestureListener() {
	// @Override
	// public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	// float velocityY) {
	// if (mDragView != null) {
	// int deltaX = (int)Math.abs(e1.getX()-e2.getX());
	// int deltaY = (int)Math.abs(e1.getY() - e2.getY());
	//
	// if (deltaX > mDragView.getWidth()/2 && deltaY < mDragView.getHeight()) {
	// mRemoveListener.onRemove(mStartPosition);
	// }
	//
	// stopDrag(mStartPosition - getFirstVisiblePosition());
	//
	// return true;
	// }
	// return false;
	// }
	// });
	// }

	public void setListItemListener(ListItemListener itemListener) {
		listItemListener = itemListener;
	}

}
