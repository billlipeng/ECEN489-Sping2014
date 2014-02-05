package com.example.assignment3;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Entry
    private static final String DATABASE_Entry = "EntrysManager";

    // Entrys table Entry
    private static final String TABLE_Entrys = "Entrys";

    // Entrys Table Columns Entrys
    private static final String KEY_ID = "id";
    private static final String KEY_Entry = "Entry";
    

    public DatabaseHandler(Context context) {
        super(context, DATABASE_Entry, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EntryS_TABLE = "CREATE TABLE " + TABLE_Entrys + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_Entry + " TEXT"
                + ")";
        db.execSQL(CREATE_EntryS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Entrys);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new Entry
    void addEntry(Entry Entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_Entry, Entry.getEntry()); // Entry Entry
        

        // Inserting Row
        db.insert(TABLE_Entrys, null, values);
        db.close(); // Closing database connection
    }

    // Getting single Entry
    Entry getEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_Entrys, new String[] { KEY_ID,
                KEY_Entry}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Entry Entry = new Entry(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        // return Entry
        return Entry;
    }

    // Getting All Entrys
    public List<Entry> getAllEntrys() {
        List<Entry> EntryList = new ArrayList<Entry>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Entrys;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Entry Entry = new Entry();
                Entry.setID(Integer.parseInt(cursor.getString(0)));
                Entry.setEntry(cursor.getString(1));
               

                 String entry = cursor.getString(1);
                FullscreenActivity.ArrayofEntry.add(entry);
                // Adding Entry to list
                EntryList.add(Entry);
            } while (cursor.moveToNext());
        }

        // return Entry list
        return EntryList;
    }

    // Updating single Entry
    public int updateEntry(Entry Entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_Entry, Entry.getEntry());

        // updating row
        return db.update(TABLE_Entrys, values, KEY_ID + " = ?",
                new String[] { String.valueOf(Entry.getID()) });
    }

    // Deleting single Entry
    public void deleteEntry(Entry Entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Entrys, KEY_ID + " = ?",
                new String[] { String.valueOf(Entry.getID()) });
        db.close();
    }


    // Getting Entrys Count
    public int getEntrysCount() {
        String countQuery = "SELECT  * FROM " + TABLE_Entrys;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}