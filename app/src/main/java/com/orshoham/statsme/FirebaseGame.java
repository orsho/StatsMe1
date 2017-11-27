package com.orshoham.statsme;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseGame {
    static FirebaseAuth mAuth;
    static DatabaseReference mref;

    //make connection with firebase database
    static void connectDatabase() {
        mref = FirebaseDatabase.getInstance().getReference();
    }

    static void createNewGame(){
        connectDatabase();
        final String userID = UserDetails.getUserId();

        //add game to user total games played
        UserDetails.addGameToUser();

        //create a new game in the user database
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("numgames", snapshot.getValue().toString());
                mref.child("Users/"+userID+"/Games/Game"+snapshot.getValue()+"/MyWinners").setValue("1");
                mref.child("Users/"+userID+"/Games/Game"+snapshot.getValue()+"/MyUF").setValue("3");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
/*
    public String getNumberOfGames() {
        final String userID = connectDatabase();
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("numgames", snapshot.getValue().toString());
                String numGame = snapshot.getValue().toString();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return numGame;
    }*/

    static void getSpeechCountByWord() {
        final String userID = UserDetails.getUserId();

        mref.child("Users/" + userID + "/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("numgamesWhichIsGoingOn", snapshot.getValue().toString());
                mref.child("Users/" + userID + "/Games/Game" + snapshot.getValue()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot gameSnapshot) {
                        for (DataSnapshot childSnapshot : gameSnapshot.getChildren()) {
                            Log.i("Value in game", childSnapshot.getValue(String.class));
                            Log.i("from", childSnapshot.toString());
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
