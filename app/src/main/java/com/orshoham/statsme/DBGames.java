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
    public static final String KEY_GAME_NUMBER = "game_number";
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
    public static final String KEY_WINORLOSS_NUMBER = "win_or_loss";


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

    public void deleteDb() {
        SQLiteDatabase db = this.getWritableDatabase(); //get database
        db.execSQL("DELETE FROM " + TABLE_GAMES); //delete all rows in a table
        db.close();
    }

    public void deleteOneGame(int gameNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "DELETE FROM games WHERE game_number =" + (gameNumber);
        db.execSQL(selectQuery);
        Log.i("removed game number", Integer.toString(gameNumber));
        //String selectQuery2 = "UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE NAME = KEY_ID_GAMES";
        //db.execSQL(selectQuery2);
        insertGameNumbers();
    }

    public boolean checkTableGamesNotEmpty (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_GAMES, null);
        Boolean rowExists;

        if (mCursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            rowExists = true;
            Log.i("this DB", " is not empty");


        } else
        {
            // I AM EMPTY
            rowExists = false;
            Log.i("this DB", " is empty");
        }
        return rowExists;
    }

    // Adding new record
    public void addMyGameStats(GamesSQL game) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String selectQuery = "SELECT * FROM " + TABLE_GAMES;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        if (checkTableGamesNotEmpty()==true){
            values.put(KEY_GAME_NUMBER, cursor.getInt(1)+1);
            Log.i("entered game number", Integer.toString(cursor.getInt(1)+1));
        } else  {
            values.put(KEY_GAME_NUMBER, 1);
            Log.i("entered", Integer.toString(1));
        }
        values.put(KEY_MYSET1SCORE_NUMBER, game.getMySet1());
        values.put(KEY_RIVALSET1SCORE_NUMBER, game.getRivalSet1());
        values.put(KEY_MYSET2SCORE_NUMBER, game.getMySet2());
        values.put(KEY_RIVALSET2SCORE_NUMBER, game.getRivalSet2());
        values.put(KEY_MYSET3SCORE_NUMBER, game.getMySet3());
        values.put(KEY_RIVALSET3SCORE_NUMBER, game.getRivalSet3());
        values.put(KEY_MYWINNERS_NUMBER, game.getMyWinners());
        values.put(KEY_MYFORCED_NUMBER, game.getMyForced());
        values.put(KEY_MYUNFORCED_NUMBER, game.getMyUNForced());
        values.put(KEY_RIVALWINNERS_NUMBER, game.getRivalWinners());
        values.put(KEY_RIVALFORCED_NUMBER, game.getRivalForced());
        values.put(KEY_RIVALUNFORCED_NUMBER, game.getRivalUNForced());
        values.put(KEY_WINORLOSS_NUMBER, game.getWinOrLoss());
        // Inserting Row
        db.insert(TABLE_GAMES, null, values);
        //insertGameNumbers();
        db.close(); // Closing database connection
    }

    // Getting All Speeches
    public List getAllGames() {
        List<GamesSQL> gameList = new ArrayList();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_GAMES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GamesSQL game = new GamesSQL();
                game.setId(cursor.getInt(0));
                game.setGameNumber(cursor.getInt(1));
                game.setMySet1(cursor.getInt(2));
                game.setRivalSet1(cursor.getInt(3));
                game.setMySet2(cursor.getInt(4));
                game.setRivalSet2(cursor.getInt(5));
                game.setMySet3(cursor.getInt(6));
                game.setRivalSet3(cursor.getInt(7));
                game.setMyWinners(cursor.getInt(8));
                game.setMyForced(cursor.getInt(9));
                game.setMyUNForced(cursor.getInt(10));
                game.setRivalWinners(cursor.getInt(11));
                game.setRivalForced(cursor.getInt(12));
                game.setRivalUNForced(cursor.getInt(13));
                game.setWinOrLoss(cursor.getInt(14));
        // Adding speech to list
                gameList.add(game);
            } while (cursor.moveToNext());
        }
        //show numbers of rows in TABLE GAMES
        for(int i=0;i<gameList.size();i++){
            Log.i("id list(DBGAMES)", Integer.toString(gameList.get(i).getId()));
            Log.i("game list(DBGAMES)", Integer.toString(gameList.get(i).getGameNumber()));
        }
        // return sepeech list
        return gameList;
    }

    public List getSpecificRowsByWhereEquals(String colunmName, int number) {
        List<GamesSQL> gameList = new ArrayList();
        // Select All Query WHERE
        String selectQuery = "SELECT * FROM games WHERE " + colunmName + "=" + number;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GamesSQL game = new GamesSQL();
                game.setId(cursor.getInt(0));
                game.setGameNumber(cursor.getInt(1));
                game.setMySet1(cursor.getInt(2));
                game.setRivalSet1(cursor.getInt(3));
                game.setMySet2(cursor.getInt(4));
                game.setRivalSet2(cursor.getInt(5));
                game.setMySet3(cursor.getInt(6));
                game.setRivalSet3(cursor.getInt(7));
                game.setMyWinners(cursor.getInt(8));
                game.setMyForced(cursor.getInt(9));
                game.setMyUNForced(cursor.getInt(10));
                game.setRivalWinners(cursor.getInt(11));
                game.setRivalForced(cursor.getInt(12));
                game.setRivalUNForced(cursor.getInt(13));
                game.setWinOrLoss(cursor.getInt(14));
                // Adding to list
                gameList.add(game);
            } while (cursor.moveToNext());
        }
        //show numbers of rows in TABLE GAMES
        for(int i=0;i<gameList.size();i++){
            //Log.i("WhereEquals(DBGAMES)", Integer.toString(gameList.get(i).getId()));
        }
        // return list
        return gameList;
    }

    public void insertGameNumbers(){
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_GAMES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ContentValues values = new ContentValues();
        cursor.moveToFirst();
        int i=1;
        Log.i("TAGFunctionGameNumberI=", Integer.toString(i));
        // looping through all rows and adding to list
        if (checkTableGamesNotEmpty()==true) {
            if (cursor.moveToFirst()) {
                do {
                    Log.i("TAG gameNum before", Integer.toString(cursor.getInt(1)));
                    values.put(KEY_GAME_NUMBER, i);
                    db.update(TABLE_GAMES, values, "game_number=game_number", null);
                    Log.i("TAG gameNum after", Integer.toString(cursor.getInt(1)));
                    i++;
                } while (cursor.moveToNext());
            }
        }
        else  {
            values.put(KEY_GAME_NUMBER, 1);
            Log.i("TAGentered", Integer.toString(1));
            db.insert(TABLE_GAMES, null, values);
        }

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //create games table
        Log.i("createDB=", "Table games");
        String sqlGames = "CREATE TABLE " + TABLE_GAMES
                + " ("
                + KEY_ID_GAMES + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_GAME_NUMBER + " INTEGER, "
                + KEY_MYSET1SCORE_NUMBER + " INTEGER, "
                + KEY_RIVALSET1SCORE_NUMBER + " INTEGER, "
                + KEY_MYSET2SCORE_NUMBER + " INTEGER, "
                + KEY_RIVALSET2SCORE_NUMBER + " INTEGER, "
                + KEY_MYSET3SCORE_NUMBER + " INTEGER, "
                + KEY_RIVALSET3SCORE_NUMBER + " INTEGER, "
                + KEY_MYWINNERS_NUMBER + " INTEGER, "
                + KEY_MYFORCED_NUMBER + " INTEGER, "
                + KEY_MYUNFORCED_NUMBER + " INTEGER, "
                + KEY_RIVALWINNERS_NUMBER + " INTEGER, "
                + KEY_RIVALFORCED_NUMBER + " INTEGER, "
                + KEY_RIVALUNFORCED_NUMBER + " INTEGER, "
                + KEY_WINORLOSS_NUMBER + " INTEGER "
                + ");";
        Log.i("createDB=", sqlGames);
        db.execSQL(sqlGames);

    }


}
