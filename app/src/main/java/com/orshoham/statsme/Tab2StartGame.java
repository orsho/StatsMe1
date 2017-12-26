package com.orshoham.statsme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Tab2StartGame extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_start_game, container, false);

        Button game = (Button) rootView.findViewById(R.id.game_btn);
        Button self = (Button) rootView.findViewById(R.id.self_btn);
        Button coach = (Button) rootView.findViewById(R.id.coach_btn);
        game.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseGame.createNewGame();
                Intent intent = new Intent(getActivity(), GameLive.class);
                startActivity(intent);
            }
        });

        self.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Sorry, this feature is not avaliable yet")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        coach.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Sorry, this feature is not avaliable yet")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        //self.setOnClickListener(onClickListener);
        //coach.setOnClickListener(onClickListener);
        return rootView;
    }

}
