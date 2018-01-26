package com.orshoham.statsme;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetails extends AppCompatActivity {
    public FirebaseAuth mAuth;
    public DatabaseReference mref;

    DBGames db = new DBGames(this);

    public String getUserId() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        return userID;
    }

    //make connection with firebase database
    public void connectDatabase() {
        mref = FirebaseDatabase.getInstance().getReference();
    }

    public void userFirstTimeApp(){
        connectDatabase();
        String userID = getUserId();
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").setValue("0");

    }




}
