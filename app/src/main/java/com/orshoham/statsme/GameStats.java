package com.orshoham.statsme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class GameStats extends AppCompatActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_stats);

        //get the game number of item that being clicked in Tab1MyProfile
        Intent intent = getIntent();
        String gameIdString = intent.getStringExtra("gameId");
        int gameId = Integer.parseInt(gameIdString);
        Log.i("gameId in Stats", Integer.toString(gameId));
    }
}
