package com.shopper.android.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shopper.android.server.ServerResponse;
import com.shopper.android.server.ServerResponseCallback;

public class MyListActivity extends ListActivity implements
		ServerResponseCallback {
	
	private RequireLoginActivityHelper activityHelper;
	
	private ListView mainListView ;
	private int layout;
	private int listViewId;
	private int listRowId;
	
	private ArrayAdapter<String> listAdapter ;
	private ArrayList<String> listData = new ArrayList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		activityHelper = new RequireLoginActivityHelper(this, this);
		mainListView = (ListView) findViewById( listViewId );
		//getActivityHelper().inflateLayout(layout);
		getActivityHelper().onCreate(savedInstanceState);		
	}

	protected RequireLoginActivityHelper getActivityHelper() {
		return activityHelper;
	}

	@Override
	public void cancelled() {
		activityHelper.cancelled();

	}

	@Override
	public void offline() {
		activityHelper.offline();

	}

	@Override
	protected void onResume() {
		activityHelper.onResume();
	}

	@Override
	protected void onDestroy() {
		activityHelper.onDestroy();
	}

	@Override
	public void gotResponse(ServerResponse response) {
		activityHelper.gotResponse(response);		
		listAdapter = new ArrayAdapter<String>(this, listRowId , listData);
		mainListView.setAdapter( listAdapter ); 

	}

	protected void setLayout(int layout) {
		this.layout = layout;
	}
	
	protected void setListView(int listViewId){
		this.listViewId = listViewId;
	}
	
	protected void setListRowId(int listRowId) {
		this.listRowId = listRowId;
	}
	
	protected void setData(ArrayList<String> listData) {
		this.listData = listData; 
	}

}
