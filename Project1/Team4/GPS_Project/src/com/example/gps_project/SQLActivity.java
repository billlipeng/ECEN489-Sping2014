package com.example.gps_project;

// Activity to plot the SQLITE in a list.


import java.util.List;

import com.example.gps_project.Book;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SQLActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sql);
	    MySQLiteHelper db = new MySQLiteHelper(this);
	
	    List<Book> list = db.getAllBooks();			        
	    ArrayAdapter<Book> adapter=new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, list);
	    adapter.notifyDataSetChanged();
	    ListView lv = (ListView)findViewById(R.id.list);
        lv.setAdapter(adapter);	    
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sql, menu);
		return true;
	}

}
