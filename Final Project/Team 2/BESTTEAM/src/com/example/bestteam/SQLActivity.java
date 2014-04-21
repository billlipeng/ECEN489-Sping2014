package com.example.bestteam;


import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SQLActivity extends Activity {
    MySQLiteHelper db = new MySQLiteHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sql);
	
	    List<Book> list = db.getAllBooks();			        
	    ArrayAdapter<Book> adapter=new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, list);
	    adapter.notifyDataSetChanged();
	    ListView lv = (ListView)findViewById(R.id.list);
        lv.setAdapter(adapter);	    
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.sql, menu);
		return true;
	}
	
	
	  public void delete (View view){
		  	db.deleteAll();

		  	List<Book> list = db.getAllBooks();			        
		    ArrayAdapter<Book> adapter=new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, list);
		    adapter.notifyDataSetChanged();
		    ListView lv = (ListView)findViewById(R.id.list);
	        lv.setAdapter(adapter);	  
	  }

}
