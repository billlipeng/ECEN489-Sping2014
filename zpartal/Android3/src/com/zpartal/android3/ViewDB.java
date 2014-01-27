package com.zpartal.android3;

import java.util.List;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class ViewDB extends ListActivity {	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_db);
		DatabaseHandler db = new DatabaseHandler(this);
		List<LuxValue> luxvals = db.getAllLuxVals();    
		ArrayAdapter<LuxValue> adapter = new ArrayAdapter<LuxValue>(this,
				android.R.layout.simple_list_item_1, luxvals);
		setListAdapter(adapter);		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_db, menu);
		return true;
	}

	
}
