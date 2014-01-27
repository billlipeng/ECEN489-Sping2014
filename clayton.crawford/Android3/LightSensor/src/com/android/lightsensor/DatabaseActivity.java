package com.android.lightsensor;

import java.util.Locale;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.TextView;

public class DatabaseActivity extends Activity {
	
	TextView dbOutput;
	SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
		dbOutput = (TextView) findViewById(R.id.textView4);
		dbOutput.setMovementMethod(new ScrollingMovementMethod());
		generateTable();
	}
	
	private void generateTable() {
		db = openOrCreateDatabase("LuxReadings.db", SQLiteDatabase.OPEN_READONLY, null);
		db.setVersion(3);
		db.setLocale(Locale.getDefault());
		
		Cursor cursor = db.query("lux", null, null, null, null, null, null);
		cursor.moveToFirst();
		while(cursor.isAfterLast() == false) {
			dbOutput.append("\n" + cursor.getString(cursor.getColumnIndex("ID")) + "\t\t\t\t\t" + cursor.getString(cursor.getColumnIndex("Time")) + "\t\t\t\t\t" + cursor.getString(cursor.getColumnIndex("Lux")));
			cursor.moveToNext();
		}
		cursor.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database, menu);
		return true;
	}

}
