package com.mfa157.rssigatheringteam3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class DatabaseView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_viewer);
		
		final MySQLiteHelper dbhelper = new MySQLiteHelper(getApplicationContext());
		final SQLiteDatabase db=dbhelper.getWritableDatabase();
		
		final ListView listview = (ListView) findViewById(R.id.ListView);
		
		String[] values;
		final ArrayList<String> list = new ArrayList<String>();
		//int id
		Cursor c = db.rawQuery("SELECT * FROM RSSI_data", null);
		
	    if (c.moveToFirst()) {
	        do {
	        list.add("ID: "+c.getString(0)+"       RSSI:"+c.getString(1));
	    } while (c.moveToNext());
	    }
	    
	    final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
	    listview.setAdapter(adapter);
}
	
	private class StableArrayAdapter extends ArrayAdapter<String> {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }

	  }
}

