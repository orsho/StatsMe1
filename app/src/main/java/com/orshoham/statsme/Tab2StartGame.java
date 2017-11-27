package com.orshoham.statsme;

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
                Intent intent = new Intent(getActivity(), StartGameRules.class);
                startActivity(intent);
            }
        });
        //self.setOnClickListener(onClickListener);
        //coach.setOnClickListener(onClickListener);
        return rootView;
    }

}
