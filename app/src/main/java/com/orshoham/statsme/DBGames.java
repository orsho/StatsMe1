package com.orshoham.statsme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBGames extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "gamesData";

    // speech table name
    private static final String TABLE_GAMES = "games";
    // speech Table Columns names
    public static final String KEY_ID_GAMES = "id";
    //public static final String KEY_GAME_NUMBER = "game_number";
    public static final String KEY_MYSET1SCORE_NUMBER = "my_set1_score";
    public static final String KEY_RIVALSET1SCORE_NUMBER = "rival_set1_score";
    public static final String KEY_MYSET2SCORE_NUMBER = "my_set2_score";
    public static final String KEY_RIVALSET2SCORE_NUMBER = "rival_set2_score";
    public static final String KEY_MYSET3SCORE_NUMBER = "my_set3_score";
    public static final String KEY_RIVALSET3SCORE_NUMBER = "rival_set3_score";
    public static final String KEY_MYWINNERS_NUMBER = "my_winners_score";
    public static final String KEY_MYFORCED_NUMBER = "my_forced_score";
    public static final String KEY_MYUNFORCED_NUMBER = "my_unforced_score";
    public static final String KEY_RIVALWINNERS_NUMBER = "rival_winners_score";
    public static final String KEY_RIVALFORCED_NUMBER = "rival_forced_score";
    public static final String KEY_RIVALUNFORCED_NUMBER = "rival_unforced_score";


    public DBGames(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase gamesData, int oldVersion, int newVersion) {
// Drop older table if existed
        gamesData.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
// Creating tables again
        onCreate(gamesData);
    }

    // Adding new record
    public void addMyWinners(GamesSQL game) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MYWINNERS_NUMBER, game.getMyWinners());
        Log.i("winner number in SQL is", Integer.toString(game.getMyWinners()));
        Log.i("ID number in SQL is", Integer.toString(game.getId()));
        // Inserting Row
        db.insert(TABLE_GAMES, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Speeches
    public List getAllGames() {
        List gameList = new ArrayList();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_GAMES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GamesSQL game = new GamesSQL();
                game.setId(cursor.getInt(0));
                game.setMyWinners(cursor.getInt(1));
                Log.i("game number SQl", Integer.toString(game.getId()));
                Log.i("winner number SQl", Integer.toString(game.getMyWinners()));
        // Adding speech to list
                gameList.add(game);
            } while (cursor.moveToNext());
        }
        Log.i("TABLE GAMES", "can show");
        // return sepeech list
        return gameList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create games table
        Log.i("createDB=", "Table games");
        String sqlGames = "CREATE TABLE " + TABLE_GAMES
                + " ("
                + KEY_ID_GAMES + " INTEGER PRIMARY KEY,"
               /* + KEY_MYSET1SCORE_NUMBER + " INTEGER "
                + KEY_RIVALSET1SCORE_NUMBER + " INTEGER "
                + KEY_MYSET2SCORE_NUMBER + " INTEGER "
                + KEY_RIVALSET2SCORE_NUMBER + " INTEGER "
                + KEY_MYSET3SCORE_NUMBER + " INTEGER "
                + KEY_RIVALSET3SCORE_NUMBER + " INTEGER "*/
                + KEY_MYWINNERS_NUMBER + " INTEGER "/*
                + KEY_MYFORCED_NUMBER + " INTEGER "
                + KEY_MYUNFORCED_NUMBER + " INTEGER "
                + KEY_RIVALWINNERS_NUMBER + " INTEGER "
                + KEY_RIVALFORCED_NUMBER + " INTEGER "
                + KEY_RIVALUNFORCED_NUMBER + " INTEGER "*/
                + ");";
        Log.i("createDB=", sqlGames);
        db.execSQL(sqlGames);

    }


}
