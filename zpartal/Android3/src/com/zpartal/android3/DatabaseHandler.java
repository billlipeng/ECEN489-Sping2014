package com.zpartal.android3;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	private static final String TABLE_NAME = "luxvals";
	private static final String DATABASE_NAME = "luxvalues.db";
	private static final int DATABASE_VERSION = 1;
	
	// Luxvals Column Names
	private static final String KEY_ID = "_id";
	private static final String KEY_TIME = "time";
	private static final String KEY_LUX = "lux";
	
	String CREATE_LUXVALS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TIME + " TEXT,"
            + KEY_LUX + " TEXT" + ")";
	
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_LUXVALS_TABLE);
		Log.w("android3", "DB created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);		
	}	
	
	void clearTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL(CREATE_LUXVALS_TABLE);
		db.close();
	}
	
	void addLuxVal(LuxValue val) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, val.getTime()); // time
        values.put(KEY_LUX, val.getLux()); // lux
 
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
	
	public List<LuxValue> getAllLuxVals() {
		List<LuxValue> luxList = new ArrayList<LuxValue>();
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
            do {
                LuxValue luxval = new LuxValue();
                luxval.setId(Integer.parseInt(cursor.getString(0)));
                luxval.setTime(cursor.getString(1));
                luxval.setLux(cursor.getString(2));
                // Adding contact to list
                luxList.add(luxval);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return luxList;		
	}
	
}
