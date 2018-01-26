package com.orshoham.statsme;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "speechData";
    // speech table name
    private static final String TABLE_SPEECH = "speech";
    // speech Table Columns names
    public static final String KEY_ID_SPEECH = "id";
    public static final String KEY_RECORD = "record";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("createDB=", "Table speech");
        String sql = "CREATE TABLE " + TABLE_SPEECH
                + " ("
                + KEY_ID_SPEECH + "  INTEGER PRIMARY KEY,"
                + KEY_RECORD + " TEXT "
                + ");";
        Log.i("createDB=", sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase speechData, int oldVersion, int newVersion) {
// Drop older table if existed
        speechData.execSQL("DROP TABLE IF EXISTS " + TABLE_SPEECH);
// Creating tables again
        onCreate(speechData);
    }

    // Adding new record
    public void addRecord(Speech record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RECORD, record.getRecord()); // record
        // Inserting Row
        Log.i("what the text", record.getRecord());
        db.insert(TABLE_SPEECH, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Speeches
    public List<Speech> getAllSpeech() {
        List<Speech> speechList = new ArrayList<Speech>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SPEECH;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Speech speech = new Speech();
                speech.setId(cursor.getInt(0));
                speech.setRecord(cursor.getString(1));
// Adding speech to list
                speechList.add(speech);
            } while (cursor.moveToNext());
        }
        Log.i("TABLE", "can show");
// return sepeech list
        return speechList;
    }


    // Getting speech Count where specific word has being said
    public int getSpeechCountByWord(String name) {
        //String countQuery = "SELECT * FROM " + DATABASE_SPEECH + " WHERE " + KEY_RECORD + " =one";
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM speech WHERE record = 'one'", null);
        Cursor cursor = db.rawQuery("SELECT * FROM speech WHERE record LIKE '%" + name + "%'", null);
        //Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Log.i("number in the logs", Integer.toString(cnt));
        cursor.close();
        return cnt;
    }

    public void deleteDb() {
        SQLiteDatabase db = this.getWritableDatabase(); //get database
        db.execSQL("DELETE FROM " + TABLE_SPEECH); //delete all rows in a table
        db.close();
    }


}


