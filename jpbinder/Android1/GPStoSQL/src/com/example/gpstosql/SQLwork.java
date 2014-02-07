package com.example.gpstosql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLwork extends SQLiteOpenHelper {
	
	private static final int dbver = 1;
	
	String maketbl = "create table gpsTbl (ids integer primary key, latitude val, longitude val)";
	
	
	public SQLwork(Context context) {
		super(context, "gpsTBl.db", null, dbver);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(maketbl);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table " + "gpsTbl");
		onCreate(db);		
	}	
	
	void newTbl() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("drop table " + "gpsTbl");
		db.execSQL(maketbl);
		db.close();
	}
	
	
	
	void coordsToDB(int lat, int lng) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        
        values.put("latitude", lat);
        values.put("longitude", lng);
 
        db.insert("gpsTbl", null, values);
        db.close();
    }
	
		
}
	

