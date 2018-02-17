package com.orshoham.statsme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameStats extends AppCompatActivity {

    DBGames dbGames = new DBGames(this);

    ImageView myImageProfileView;
    ImageView rivalImageProfileView;

    Chronometer myTimeCount;

    TextView ScoreSet1;
    TextView ScoreSet2;
    TextView ScoreSet3;
    TextView myWinnerViewId;
    TextView myForcedViewId;
    TextView myUnforcedViewId;
    TextView rivalWinnerViewId;
    TextView rivalForcedViewId;
    TextView rivalUnforcedViewId;
    TextView myAcesViewId;
    TextView rivalAcesViewId;
    TextView myDoublesViewId;
    TextView rivalDoublesViewId;
    TextView myFirstInViewId;
    TextView rivalFirstInViewId;
    TextView myFirstWonViewId;
    TextView rivalFirstWonViewId;
    TextView mySecondWonViewId;
    TextView rivalSecondWonViewId;
    TextView myNetViewId;
    TextView rivalNetViewId;

    int getMyFirst;
    int getMyTotalFirst;
    int getMySecond;
    int getMyTotalSecond;
    int getRivalFirst;
    int getRivalTotalFirst;
    int getRivalSecond;
    int getRivalTotalSecond;

    int myFirstPercentageIn=0;
    int rivalFirstPercentageIn=0;
    int myFirstPercentageWon=0;
    int mySecondPercentageWon=0;
    int rivalFirstPercentageWon=0;
    int rivalSecondPercentageWon=0;

    int myNet=0;
    int myTotalNet=0;
    int rivalNet=0;
    int rivalTotalNet=0;


    public void showSetScore (List<GamesSQL> gameList){
        ScoreSet1.setText(Integer.toString(gameList.get(0).getMySet1())+":"
                +Integer.toString(gameList.get(0).getRivalSet1()));
        ScoreSet2.setText(Integer.toString(gameList.get(0).getMySet2())+":"
                +Integer.toString(gameList.get(0).getRivalSet2()));
        ScoreSet3.setText(Integer.toString(gameList.get(0).getMySet3())+":"
                +Integer.toString(gameList.get(0).getRivalSet3()));
    }

    public void showStats(List<GamesSQL> gameList){
        myWinnerViewId.setText(Integer.toString(gameList.get(0).getMyWinners()));
        myForcedViewId.setText(Integer.toString(gameList.get(0).getMyForced()));
        myUnforcedViewId.setText(Integer.toString(gameList.get(0).getMyUNForced()));
        rivalWinnerViewId.setText(Integer.toString(gameList.get(0).getRivalWinners()));
        rivalForcedViewId.setText(Integer.toString(gameList.get(0).getRivalForced()));
        rivalUnforcedViewId.setText(Integer.toString(gameList.get(0).getRivalUNForced()));
        myAcesViewId.setText(Integer.toString(gameList.get(0).getMyAces()));
        rivalAcesViewId.setText(Integer.toString(gameList.get(0).getRivalAces()));
        myDoublesViewId.setText(Integer.toString(gameList.get(0).getMyDoubles()));
        rivalDoublesViewId.setText(Integer.toString(gameList.get(0).getRivalDoubles()));
    }

    public void showServeStats(List<GamesSQL> gameList){
        getMyFirst = gameList.get(0).getMyFirst();
        getMyTotalFirst = gameList.get(0).getMyTotalFirst();
        getMySecond = gameList.get(0).getMySecond();
        getMyTotalSecond = gameList.get(0).getMyTotalSecond();
        getRivalFirst = gameList.get(0).getRivalFirst();
        getRivalTotalFirst = gameList.get(0).getRivalTotalFirst();
        getRivalSecond = gameList.get(0).getRivalSecond();
        getRivalTotalSecond = gameList.get(0).getRivalTotalSecond();

        if (getMyTotalFirst>0 || getMyTotalSecond>0){
            myFirstPercentageIn = Math.round((getMyTotalFirst*100)/(getMyTotalFirst+getMyTotalSecond));
        }
        if (getRivalTotalFirst>0 || getRivalTotalSecond>0){
            rivalFirstPercentageIn = Math.round((getRivalTotalFirst*100)/(getRivalTotalFirst+getRivalTotalSecond));
        }
        if (getMyTotalFirst>0){
            myFirstPercentageWon = Math.round((getMyFirst*100)/(getMyTotalFirst));
        }
        if (getRivalTotalFirst>0){
            rivalFirstPercentageWon = Math.round((getRivalFirst*100)/(getRivalTotalFirst));
        }
        if (getMyTotalSecond>0){
            mySecondPercentageWon = Math.round((getMySecond*100)/(getMyTotalSecond));
        }
        if (getRivalTotalSecond>0){
            rivalSecondPercentageWon = Math.round((getRivalSecond*100)/(getRivalTotalSecond));
        }

        myFirstInViewId.setText(Integer.toString(myFirstPercentageIn)+"%");
        rivalFirstInViewId.setText(Integer.toString(rivalFirstPercentageIn)+"%");
        myFirstWonViewId.setText(Integer.toString(myFirstPercentageWon)+"%");
        rivalFirstWonViewId.setText(Integer.toString(rivalFirstPercentageWon)+"%");
        mySecondWonViewId.setText(Integer.toString(mySecondPercentageWon)+"%");
        rivalSecondWonViewId.setText(Integer.toString(rivalSecondPercentageWon)+"%");
    }

    public void showNetStats(List<GamesSQL> gameList){
        myNet = gameList.get(0).getMyNet();
        myTotalNet = gameList.get(0).getMyTotalNet();
        rivalNet = gameList.get(0).getRivalNet();
        rivalTotalNet = gameList.get(0).getRivalTotalNet();

        Log.i("net", Integer.toString(myTotalNet));
        Log.i("net", Integer.toString(gameList.get(0).getMyTotalNet()));
        Log.i("net", Integer.toString(rivalTotalNet));

        myNetViewId.setText(Integer.toString(myNet)+"/"+Integer.toString(myTotalNet));
        rivalNetViewId.setText(Integer.toString(rivalNet)+"/"+Integer.toString(rivalTotalNet));
    }

    public void showTime (List<GamesSQL> gameList){

        long hours = TimeUnit.MILLISECONDS.toHours(gameList.get(0).getTime());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(gameList.get(0).getTime());

        if (minutes>9){
            myTimeCount.setText(Long.toString(hours)+":"+Long.toString(minutes));
        }
        if (minutes<=9){
            myTimeCount.setText(Long.toString(hours)+":0"+Long.toString(minutes));
        }
        Log.i("DBtime", Long.toString(minutes));
    }

    public void savePhoto() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userIdRef = rootRef.child("Users").child(uid).child("UserDetails");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageurl = dataSnapshot.child("imageurl").getValue(String.class);
                String userid = dataSnapshot.child("userid").getValue(String.class);
                Log.d("TAG", imageurl + " / " + userid);

                Tab1MyProfile.ImageDownloader task = new Tab1MyProfile.ImageDownloader();
                Bitmap myImage;

                try {
                    myImage = task.execute(imageurl).get();
                    myImageProfileView.setImageBitmap(myImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        userIdRef.addListenerForSingleValueEvent(eventListener);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_stats);

        //get the game number of item that being clicked in Tab1MyProfile
        Intent intent = getIntent();
        String gameNumberString = intent.getStringExtra("gameNumber");
        int gameNumber = Integer.parseInt(gameNumberString);
        Log.i("gameNumber in Stats", Integer.toString(gameNumber));

        List<GamesSQL> gameList = dbGames.getSpecificRowsByWhereEquals("game_number", gameNumber);

        //declare profile pictures
        myImageProfileView = (ImageView) findViewById(R.id.MyprofileImageDB);
        savePhoto();
        rivalImageProfileView = (ImageView) findViewById(R.id.RivalprofileImageDB);

        myTimeCount = (Chronometer) findViewById(R.id.timeCountDB);

        ScoreSet1 = (TextView) findViewById(R.id.totalScoreSet1IdDB);
        ScoreSet2 = (TextView) findViewById(R.id.totalScoreSet2IdDB);
        ScoreSet3 = (TextView) findViewById(R.id.totalScoreSet3IdDB);

        myWinnerViewId = (TextView)findViewById(R.id.myWinnerIdDB);
        myForcedViewId = (TextView)findViewById(R.id.myForcedIdDB);
        myUnforcedViewId = (TextView)findViewById(R.id.myUnforcedIdDB);
        rivalWinnerViewId = (TextView)findViewById(R.id.rivalWinnerIdDB);
        rivalForcedViewId = (TextView)findViewById(R.id.rivalForcedIdDB);
        rivalUnforcedViewId = (TextView)findViewById(R.id.rivalUnforcedIdDB);
        myAcesViewId = (TextView)findViewById(R.id.myAcesIdDB);
        rivalAcesViewId = (TextView)findViewById(R.id.rivalAcesIdDB);
        myDoublesViewId = (TextView)findViewById(R.id.myDoubleFaultsIdDB);
        rivalDoublesViewId = (TextView)findViewById(R.id.rivalDoubleFaultsIdDB);
        myFirstInViewId = (TextView)findViewById(R.id.myServeInIdDB);
        rivalFirstInViewId = (TextView)findViewById(R.id.rivalServeInIdDB);
        myFirstWonViewId = (TextView)findViewById(R.id.myFirstServeIdDB);
        rivalFirstWonViewId= (TextView)findViewById(R.id.rivalFirstServeIdDB);
        mySecondWonViewId = (TextView)findViewById(R.id.mySecondServeIdDB);
        rivalSecondWonViewId = (TextView)findViewById(R.id.rivalSecondServeIdDB);
        myNetViewId = (TextView)findViewById(R.id.myNetIdDB);
        rivalNetViewId = (TextView)findViewById(R.id.rivalNetIdDB);

        showTime(gameList);
        showSetScore(gameList);
        showStats(gameList);
        showServeStats(gameList);
        showNetStats(gameList);

    }
}
