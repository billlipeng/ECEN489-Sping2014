package com.mfa157.assn3optout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_RSSI = "RSSI_data";
  public static final String COLUMN_ID = "ID";
  public static final String COLUMN_RSSI = "RSSI";

  private static final String DATABASE_NAME = "Assn3optout.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String TABLE_CREATE = "CREATE TABLE "
      + TABLE_RSSI + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_RSSI
      + " int not null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(TABLE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_RSSI);
    onCreate(db);
  }

} 