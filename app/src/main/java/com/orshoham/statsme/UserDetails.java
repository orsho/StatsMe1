package com.orshoham.statsme;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetails {
    static FirebaseAuth mAuth;
    static DatabaseReference mref;


    static String getUserId() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        return userID;
    }

    //make connection with firebase database
    static void connectDatabase() {
        mref = FirebaseDatabase.getInstance().getReference();
    }

    public void userFirstTimeApp(){
        connectDatabase();
        String userID = getUserId();
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").setValue("0");

    }

    public static void addGameToUser(){
        connectDatabase();
        final String userID = getUserId();
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String countToString = snapshot.getValue().toString();
                int countToInt = Integer.parseInt(countToString);
                countToInt++;
                String countToStringAgain = Integer.toString(countToInt);
                mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").setValue(countToStringAgain);
                Log.i("numgames", snapshot.getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}
