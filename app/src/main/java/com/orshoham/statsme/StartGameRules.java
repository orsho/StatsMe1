package com.orshoham.statsme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class StartGameRules extends AppCompatActivity {

    private ListView lvSpinner;

    private void initData() {

        ArrayList<String> game = new ArrayList<>();
        game.add("choose court");
        game.add("number of sets");


        ArrayList<String> mSpinnerData = new ArrayList<>();
        mSpinnerData.add("1");
        mSpinnerData.add("2");
        mSpinnerData.add("3");
        mSpinnerData.add("4");

        BasicSpinnerAdapter adapter = new BasicSpinnerAdapter(game, mSpinnerData, this);
        lvSpinner.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game_rules);

        Button go = (Button) findViewById(R.id.startButtonId);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),GameLive.class);
                startActivity(i);
            }
        });

        //lvSpinner = (ListView) findViewById(R.id.listview_spinner);

        //initData();


    }
}
