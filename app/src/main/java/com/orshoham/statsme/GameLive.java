package com.orshoham.statsme;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameLive extends AppCompatActivity implements RecognitionListener {
    private String TAG = "TEST";

    private static SpeechRecognizer speech = null;
    private Intent intent;
    boolean flag = true;

    private Chronometer myTimeCount;
    private long lastPause;
    private int playPause = 1;
    private ImageButton mStartButton;
    private ImageButton mPauseButton;
    private Button mStopButton;
    Boolean playIsOn = false;

    ImageView userImageProfileView;
    ImageView rivalImageProfileView;

    TennisScoreCalculates calc;
    TextView myScore;
    TextView rivalScore;
    TextView totalScoreSet;


    public void startGame (View view) {
        if (playIsOn == false){
            playIsOn = true;
            mStartButton.setEnabled(false);
            mStartButton.setVisibility(Button.GONE);
            mPauseButton.setEnabled(true);
            mPauseButton.setVisibility(Button.VISIBLE);
            this.flag = true;
            Log.i(TAG, "app started");
            // start measure match time
            if (lastPause != 0){
                myTimeCount.setBase(myTimeCount.getBase() + SystemClock.elapsedRealtime() - lastPause);
            }
            else{
                myTimeCount.setBase(SystemClock.elapsedRealtime());
            }
            myTimeCount.start();

            speechRecognizer();
        }
    }

    public void pauseGame (View view) {
        if (playIsOn == true) {
            playIsOn = false;
            mStartButton.setEnabled(true);
            mStartButton.setVisibility(Button.VISIBLE);
            mPauseButton.setEnabled(false);
            mPauseButton.setVisibility(Button.GONE);
            onTotalDestroy();
            Log.i(TAG, "app paused");
            lastPause = SystemClock.elapsedRealtime();
            myTimeCount.stop();
        }

    }

    public void stopGame (View view) {
        this.flag = false;
        Log.i(TAG, "flag false");
        // stop measure match time
        lastPause = SystemClock.elapsedRealtime();
        myTimeCount.stop();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to quit the match?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void declareWin(String winLose){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You "+winLose)
                .setCancelable(false)
                .setPositiveButton("Back To Menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void speechRecognizer() {

        if (this.flag == true){
            speech = SpeechRecognizer.createSpeechRecognizer(this);
            speech.setRecognitionListener(this);
            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speech.startListening(intent);
        }
        else {
            onTotalDestroy();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO Auto-generated method stub

        if(speech != null)
        {
            speech.destroy();
            Log.i(TAG,"destroy");
        }
    }

    public void onBeginningOfSpeech() {
        Log.i(TAG, "on beginning of speech");
    }

    public void onBufferReceived(byte[] arg0) {
        Log.i(TAG, "on bufferr eceived");
    }

    public void onEndOfSpeech() {
        Log.i(TAG, "on end of speech");
    }

    public void onError(int arg0) {
        Log.i(TAG, "error code: " + arg0);
        ononDestroy();
        if (arg0 == 8){
            ononDestroy();
        }
    }

    public void onEvent(int arg0, Bundle arg1) {
        Log.i(TAG, "on event");
    }

    public void onPartialResults(Bundle arg0) {
        Log.i(TAG, "on partial results");
    }

    public void onReadyForSpeech(Bundle arg0) {
        Log.i(TAG, "on ready for speech");
    }

    public void onResults(Bundle arg0) {
        Log.i(TAG, "on results");
        ArrayList<String> matches = arg0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String s = "";
        for (String result:matches)
            s += result + "\n";

        Log.i(TAG,"resuls: "+ matches.get(0));
        Toast.makeText(GameLive.this, s, Toast.LENGTH_SHORT).show();
        speech.startListening(intent);
    }

    public void ononDestroy() {
//      super.onDestroy();
        speech.destroy();
        speechRecognizer();
    }

    public void onTotalDestroy() {
        speech.destroy();
        this.flag = true;
        Log.i(TAG, "on total destroy and flag true");
    }
    public void onRmsChanged(float arg0) {
        // Log.i(TAG, "on rms changed");
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
                    userImageProfileView.setImageBitmap(myImage);
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
        setContentView(R.layout.game_live);

        myTimeCount = (Chronometer) findViewById(R.id.timeCount);
        mStartButton = (ImageButton) findViewById(R.id.btnPlayGame);
        mPauseButton = (ImageButton) findViewById(R.id.btnPauseGame);
        mStopButton = (Button) findViewById(R.id.btnEndGame);

        //user cannot see pause button (only see it after play button is pressed)
        mPauseButton.setEnabled(false);
        mPauseButton.setVisibility(Button.GONE);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startGame(view);
            }
        });

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseGame(view);
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopGame(view);
            }
        });

        //declare profile pictures
        userImageProfileView = (ImageView) findViewById(R.id.MyprofileImage);
        savePhoto();
        rivalImageProfileView = (ImageView) findViewById(R.id.RivalprofileImage);

        myScore = (TextView) findViewById(R.id.myScoreId);
        rivalScore = (TextView) findViewById(R.id.rivalScoreId);
        totalScoreSet = (TextView) findViewById(R.id.totalScoreSetId);

        //test for score method
        calc = new TennisScoreCalculates();
        userImageProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calc.addMyPoint();
                myScore.setText("you "+Integer.toString(calc.getMyGamePoint()));
                rivalScore.setText("rival "+Integer.toString(calc.getRivalGamePoint()));
                totalScoreSet.setText(Integer.toString(calc.getMyGameScore())+":"+Integer.toString(calc.getRivalGameScore()));
                if(calc.checkWin() == true){
                    declareWin("won!! congratulations!");
                }

            }
        });

        rivalImageProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calc.addRivalPoint();
                rivalScore.setText("rival "+Integer.toString(calc.getRivalGamePoint()));
                myScore.setText("you "+Integer.toString(calc.getMyGamePoint()));
                totalScoreSet.setText(Integer.toString(calc.getMyGameScore())+":"+Integer.toString(calc.getRivalGameScore()));
                if(calc.checkWin() == true){
                    declareWin("Lost.. maybe next time");
                }
            }
        });



    }

}
