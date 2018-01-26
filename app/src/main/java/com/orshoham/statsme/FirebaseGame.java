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

public class FirebaseGame extends AppCompatActivity {
    static FirebaseAuth mAuth;
    static DatabaseReference mref;
    static String userID = getUserId();



    //make connection with firebase database
    public void connectDatabase() {
        mref = FirebaseDatabase.getInstance().getReference();
        Log.i("conected to", "Firebase Database");
    }

    static String getUserId() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        return userID;
    }

   public void createNewGame(){
        connectDatabase();

        //add game to user total games played
        addGameToUser();

        //create a new game in the user database
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    public void userFirstTimeApp(){
        connectDatabase();
        String userID = getUserId();
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").setValue("0");

    }

    public void addGameToUser(){
        final String userID = getUserId();
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String countToString = snapshot.getValue().toString();
                int countToInt = Integer.parseInt(countToString);
                countToInt++;
                String countToStringAgain = Integer.toString(countToInt);
                mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").setValue(countToStringAgain);
                Log.i("numGames in Firebase", countToStringAgain);
                //GamesSQL oneGame = new GamesSQL(countToInt);
                //Log.i("numGame in GameSQl", Integer.toString(oneGame.getGamesNumber()));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //add to firebase game point for the user (sort by set->game->game score->game point)
    static void addMyGamePointFirebase(final int countSets,final int countGames, final int countPoints, final int myGameScore, final int myGamePoint){
            mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    mref.child("Users/"+userID+"/Matches/Match"+snapshot.getValue()+
                            "/setNum"+Integer.toString(countSets)+
                            "/Game"+Integer.toString(countGames)+
                            "/"+Integer.toString(countPoints)+" totalGamePoint "+
                            Integer.toString(myGameScore)+" MyGameScore "+
                            Integer.toString(myGamePoint)+" myGamePoint")
                            .setValue(Integer.toString(myGamePoint));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
    }

    //add to firebase game point for the rival (sort by set->game->game score->game point)
    static void addRivalGamePointFirebase(final int countSets,final int countGames, final int countPoints, final int rivalGameScore, final int rivalGamePoint){
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mref.child("Users/"+userID+"/Matches/Match"+snapshot.getValue()+
                        "/setNum"+Integer.toString(countSets)+
                        "/Game"+Integer.toString(countGames)+
                        "/"+Integer.toString(countPoints)+" totalGamePoint "+
                        Integer.toString(rivalGameScore)+" rivalGameScore "+
                        Integer.toString(rivalGamePoint)+" myGamePoint")
                        .setValue(Integer.toString(rivalGamePoint));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //add number of times there is myWinner in a specific set
    static void addMyWinnersFirebase (final int countSets, final int countMyWinners){
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mref.child("Users/"+userID+"/Matches/Match"+snapshot.getValue()+
                        "/setNum"+Integer.toString(countSets)+
                        "/numMyWinners").setValue(Integer.toString(countMyWinners));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    static void addMyForcedFirebase (final int countSets, final int countMyForced){
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mref.child("Users/"+userID+"/Matches/Match"+snapshot.getValue()+
                        "/setNum"+Integer.toString(countSets)+
                        "/numMyForced").setValue(Integer.toString(countMyForced));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    static void addMyUNForcedFirebase (final int countSets, final int countMyUNForced){
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mref.child("Users/"+userID+"/Matches/Match"+snapshot.getValue()+
                        "/setNum"+Integer.toString(countSets)+
                        "/numMyUNForced").setValue(Integer.toString(countMyUNForced));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    static void addRivalWinnersFirebase (final int countSets, final int countRivalWinners){
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mref.child("Users/"+userID+"/Matches/Match"+snapshot.getValue()+
                        "/setNum"+Integer.toString(countSets)+
                        "/numRivalWinners").setValue(Integer.toString(countRivalWinners));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    static void addRivalForcedFirebase (final int countSets, final int countRivalForced){
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mref.child("Users/"+userID+"/Matches/Match"+snapshot.getValue()+
                        "/setNum"+Integer.toString(countSets)+
                        "/numRivalForced").setValue(Integer.toString(countRivalForced));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    static void addRivalUNForcedFirebase (final int countSets, final int countRivalUNForced){
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mref.child("Users/"+userID+"/Matches/Match"+snapshot.getValue()+
                        "/setNum"+Integer.toString(countSets)+
                        "/numRivalUNForced").setValue(Integer.toString(countRivalUNForced));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    static void updateFinalResults (final int myCountSets, final int rivalCountSets){
        mref.child("Users/"+userID+"/UserDetails/NumOfGamesPlayed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mref.child("Users/"+userID+"/Matches/Match"+snapshot.getValue()+
                        "/mainResults/mySetCount").setValue(Integer.toString(myCountSets));
                mref.child("Users/"+userID+"/Matches/Match"+snapshot.getValue()+
                        "/mainResults/rivalSetCount").setValue(Integer.toString(rivalCountSets));
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

}
