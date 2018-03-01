package com.orshoham.statsme;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLive extends AppCompatActivity implements RecognitionListener {
    private String TAG = "TEST";

    private static SpeechRecognizer speech = null;
    private Intent intent;
    boolean flag = true;

    //create db in SQLite by Class DBHandler
    DBHandler db = new DBHandler(this);
    DBGames dbGames = new DBGames(this);

    //bluetooth
    private static final String TAG1 = "MainActivity";
    BluetoothAdapter mBlueToothAdapter;

    private Chronometer myTimeCount;
    private long lastPause;
    private int playPause = 1;
    private ImageButton mStartButton;
    private ImageButton mPauseButton;
    private Button mStopButton;
    Boolean playIsOn = false;

    private long time;

    ImageView userImageProfileView;
    ImageView rivalImageProfileView;

    TennisScoreCalculates calc;
    TextView myScore;
    TextView rivalScore;
    TextView totalScoreSet;
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

    boolean flagMyFirst = false;
    boolean flagMySecond = false;
    boolean flagRivalFirst = false;
    boolean flagRivalSecond = false;
    boolean flagMyWonPoint = false;
    boolean flagMyLostPoint = false;

    int countMyFirst = 0;
    int countRivalFirst = 0;
    int countMyTotalFirst = 0;
    int countRivalTotalFirst = 0;
    int countMySecond = 0;
    int countRivalSecond = 0;
    int countMyTotalSecond = 0;
    int countRivalTotalSecond = 0;


    int myFirstPercentageIn;
    int rivalFirstPercentageIn;
    int myFirstPercentageWon;
    int mySecondPercentageWon;
    int rivalFirstPercentageWon;
    int rivalSecondPercentageWon;

    MediaPlayer myPointSound;
    MediaPlayer rivalPointSound;
    MediaPlayer to;

    //create broadcast
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //when find
            if (action.equals(mBlueToothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBlueToothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG1, "on recieve: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG1, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG1, "mBroadcastReciever1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG1, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    public void enableDisableBT() {
        if(mBlueToothAdapter == null) {
            Log.d(TAG1, "enableDisableBT: Does not have bluetooth capabilties");
        }
        if (!mBlueToothAdapter.isEnabled()){
            Log.d (TAG1, "enableDisableBT: enable");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if (mBlueToothAdapter.isEnabled()) {
            Log.d (TAG1, "enableDisableBT: disable");
            mBlueToothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
    }


    public void startGame (View view) {
        //check if play or pause
        if (playIsOn == false){
            playIsOn = true;
            mStartButton.setEnabled(false);
            mStartButton.setVisibility(Button.GONE);
            mPauseButton.setEnabled(true);
            mPauseButton.setVisibility(Button.VISIBLE);
            this.flag = true;
            Log.i(TAG, "app started");
            //not the first time button play is pressed
            if (lastPause != 0){
                myTimeCount.setBase(myTimeCount.getBase() + SystemClock.elapsedRealtime() - lastPause);
            }
            //first time in the game button play is pressed
            else{
                myTimeCount.setBase(SystemClock.elapsedRealtime());
                calc.zeroAllStatsInFirebase();
            }
            // start measure match time
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
        builder.setMessage("Do you want to save the game?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendTimeGame();
                        dbGames.addMyGameStats(new GamesSQL(calc.updateGameSQL(), time));
                        db.deleteDb();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNeutralButton("Back to game", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void sendTimeGame (){
        time = SystemClock.elapsedRealtime() - myTimeCount.getBase();
        Log.i("my time", Long.toString(time));
        calc.setGameTime(time);
    }

    public void declareWin(String winLose){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You "+winLose)
                .setCancelable(false)
                .setPositiveButton("Back To Menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendTimeGame();
                        dbGames.addMyGameStats(new GamesSQL(calc.updateGameSQL(), time));
                        db.deleteDb();
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

        Log.i(TAG,"results: "+ matches.get(0));
        Toast.makeText(GameLive.this, s, Toast.LENGTH_LONG).show();
        Log.i("serves",s);
        //add this s result(speech) to db data base in SQLite
        db.addRecord(new Speech(s));
        checkIfServeStatus(s);
        gamePoints(s);
        netWon(s);
        //checkServeWon();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("wait 5 seconds", "before start again");
                speech.startListening(intent);
            }
        }, 5000);

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

    public MediaPlayer checkSoundByMyPointNumber (int myPoint, MediaPlayer sounds[]){
        if (myPoint==0){
            Log.i("enter sound my 0","enter");
            myPointSound=getSound(0,sounds);
            //myPointSound=sm[0];
        }
        if (myPoint==15){
            Log.i("enter sound my 15","enter");
            myPointSound=getSound(1,sounds);
        }
        if (myPoint==30){
            Log.i("enter sound my 30","enter");
            myPointSound=getSound(2,sounds);
        }
        if (myPoint==40){
            Log.i("enter sound my 40","enter");
            myPointSound=getSound(3,sounds);
        }
        if (myPoint==45){
            Log.i("enter sound my A","enter");
            myPointSound=getSound(4,sounds);
        }

        return myPointSound;

    }

    public MediaPlayer checkSoundByRivalPointNumber (int rivalPoint, MediaPlayer sounds[]){
        if (rivalPoint==0){
            Log.i("enter sound rival 0","enter");
            rivalPointSound=getSound(0,sounds);
        }
        if (rivalPoint==15){
            Log.i("enter sound rival 15","enter");
            rivalPointSound=getSound(1,sounds);
        }
        if (rivalPoint==30){
            Log.i("enter sound rival 30","enter");
            rivalPointSound=getSound(2,sounds);
        }
        if (rivalPoint==40){
            Log.i("enter sound rival 40","enter");
            rivalPointSound=getSound(3,sounds);
        }
        if (rivalPoint==45){
            Log.i("enter sound rival A","enter");
            rivalPointSound=getSound(4,sounds);
        }

        return rivalPointSound;

    }

    public MediaPlayer getSound(int index, MediaPlayer mp[]){
        MediaPlayer mySound;
        mySound = mp[index];
        return mySound;

    }

    public MediaPlayer [] createMyArraySoundsPoints(){
        MediaPlayer mp1 = MediaPlayer.create(getApplicationContext(),R.raw.love);
        MediaPlayer mp2 = MediaPlayer.create(getApplicationContext(),R.raw.fifteen);
        MediaPlayer mp3 = MediaPlayer.create(getApplicationContext(),R.raw.thirty);
        MediaPlayer mp4 = MediaPlayer.create(getApplicationContext(),R.raw.fourty);
        MediaPlayer mp5 = MediaPlayer.create(getApplicationContext(),R.raw.my_a);


        MediaPlayer []sounds = new MediaPlayer[]{mp1, mp2, mp3, mp4, mp5};

        return sounds;
    }

    public MediaPlayer [] createRivalArraySoundsPoints(){
        MediaPlayer mp1 = MediaPlayer.create(getApplicationContext(),R.raw.love);
        MediaPlayer mp2 = MediaPlayer.create(getApplicationContext(),R.raw.fifteen);
        MediaPlayer mp3 = MediaPlayer.create(getApplicationContext(),R.raw.thirty);
        MediaPlayer mp4 = MediaPlayer.create(getApplicationContext(),R.raw.fourty);
        MediaPlayer mp5 = MediaPlayer.create(getApplicationContext(),R.raw.rival_a);


        MediaPlayer []sounds = new MediaPlayer[]{mp1, mp2, mp3, mp4, mp5};

        return sounds;
    }

    public void soundScore(){
        Log.i("enter sound points","enter");
        final MediaPlayer []rivalSounds = createRivalArraySoundsPoints();
        final MediaPlayer []mySounds = createMyArraySoundsPoints();


        final int myPoint = calc.getMyGamePoint();
        final int rivalPoint = calc.getRivalGamePoint();


        if (flagMyFirst==true||flagMySecond==true) {
            if ((myPoint != 45 && rivalPoint != 45) && (myPoint != 0 || rivalPoint != 0)) {
                myPointSound = checkSoundByMyPointNumber(myPoint, mySounds);
                myPointSound.start();
                Log.i("enter sounding my point", "enter");
                myPointSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        myPointSound.release();
                        rivalPointSound = checkSoundByRivalPointNumber(rivalPoint, rivalSounds);
                        rivalPointSound.start();
                        Log.i("enter sounding rival", " point enter");
                        rivalPointSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                rivalPointSound.release();
                                Log.i("enter sounding rival", " release");
                            }
                        });
                    }
                });
            }

            if (myPoint == 45) {
                myPointSound = checkSoundByMyPointNumber(myPoint, mySounds);
                myPointSound.start();
                myPointSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        myPointSound.release();
                        Log.i("enter sounding rival", " release");
                    }
                });
                Log.i("enter sounding my point", "enter");
            }

            if (rivalPoint == 45) {
                rivalPointSound = checkSoundByRivalPointNumber(rivalPoint, rivalSounds);
                rivalPointSound.start();
                rivalPointSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        rivalPointSound.release();
                        Log.i("enter sounding rival", " release");
                    }
                });
                Log.i("enter sounding rival", " point enter");
            }

            if (myPoint == 0 && rivalPoint == 0) {
                final MediaPlayer EndGameSound = MediaPlayer.create(getApplicationContext(), R.raw.new_game);
                EndGameSound.start();
                EndGameSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        EndGameSound.release();
                    }
                });
                Log.i("enter sounding game end", " enter");
            }

            checkServeWon();
        }

        if (flagRivalFirst==true||flagRivalSecond==true) {
            if ((myPoint != 45 && rivalPoint != 45) && (myPoint != 0 || rivalPoint != 0)) {
                rivalPointSound = checkSoundByRivalPointNumber(rivalPoint, mySounds);
                rivalPointSound.start();
                Log.i("enter sounding my point", "enter");
                rivalPointSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        rivalPointSound.release();
                        myPointSound = checkSoundByMyPointNumber(myPoint, rivalSounds);
                        myPointSound.start();
                        Log.i("enter sounding rival", " point enter");
                        myPointSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                myPointSound.release();
                                Log.i("enter sounding rival", " release");
                            }
                        });
                    }
                });
            }

            if (myPoint == 45) {
                myPointSound = checkSoundByMyPointNumber(myPoint, mySounds);
                myPointSound.start();
                myPointSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        myPointSound.release();
                        Log.i("enter sounding rival", " release");
                    }
                });
                Log.i("enter sounding my point", "enter");
            }

            if (rivalPoint == 45) {
                rivalPointSound = checkSoundByRivalPointNumber(rivalPoint, rivalSounds);
                rivalPointSound.start();
                rivalPointSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        rivalPointSound.release();
                        Log.i("enter sounding rival", " release");
                    }
                });
                Log.i("enter sounding rival", " point enter");
            }

            if (myPoint == 0 && rivalPoint == 0) {
                final MediaPlayer EndGameSound = MediaPlayer.create(getApplicationContext(), R.raw.new_game);
                EndGameSound.start();
                EndGameSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        EndGameSound.release();
                    }
                });
                Log.i("enter sounding game end", " enter");
            }

            checkServeWon();
        }


    }


    //check if user won or lost the point, check if it winner/forced/ace..., count those results in the database
    // and show the results to the user
    public void gamePoints(String s) {
        if ((s.contains("my")||s.contains("ma"))&&
                (s.contains("ace")||s.contains("ice")||s.contains("ase"))){
            myAcesViewId.setText(Integer.toString(calc.addMyAces()));
            updateMyScore();
            final MediaPlayer myAceSound = MediaPlayer.create(getApplicationContext(), R.raw.your_ace);
            myAceSound.start();
            myAceSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    myAceSound.release();
                    soundScore();
                }
            });
            flagMyWonPoint = true;
            flagMyLostPoint = false;

            Log.i("enter my ace","enter");
        }
        else if ((s.contains("opa")||s.contains("opo")||s.contains("oppo")||s.contains("fun")||s.contains("appo"))&&
                (s.contains("ace")||s.contains("ice")||s.contains("ase"))){
            rivalAcesViewId.setText(Integer.toString(calc.addRivalAces()));
            updateRivalScore();
            final MediaPlayer oppoAceSound = MediaPlayer.create(getApplicationContext(), R.raw.oppo_ace);
            oppoAceSound.start();
            oppoAceSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    oppoAceSound.release();
                    soundScore();
                }
            });
            flagMyWonPoint = false;
            flagMyLostPoint = true;
            Log.i("enter opo ace","enter");
        }
        else if ((s.contains("my")||s.contains("ma"))&&
                (s.contains("do")||s.contains("du")||s.contains("dev"))){
            myDoublesViewId.setText(Integer.toString(calc.addMyDoubles()));
            updateRivalScore();
            final MediaPlayer myDoubleSound = MediaPlayer.create(getApplicationContext(), R.raw.your_double);
            myDoubleSound.start();
            myDoubleSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    myDoubleSound.release();
                    soundScore();
                }
            });
            flagMyWonPoint = false;
            flagMyLostPoint = true;
            Log.i("enter my double","enter");
        }
        else if ((s.contains("opa")||s.contains("opo")||s.contains("oppo")||s.contains("fun")||s.contains("appo"))&&
                (s.contains("do")||s.contains("du")||s.contains("dev"))){
            rivalDoublesViewId.setText(Integer.toString(calc.addRivalDoubles()));
            updateMyScore();
            final MediaPlayer oppoDoubleSound = MediaPlayer.create(getApplicationContext(), R.raw.oppo_double);
            oppoDoubleSound.start();
            oppoDoubleSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    oppoDoubleSound.release();
                    soundScore();
                }
            });
            flagMyWonPoint = true;
            flagMyLostPoint = false;
            Log.i("enter opo double","enter");
        }
        else if ((s.contains("my")||s.contains("ma"))&&
                (s.contains("win")||s.contains("wie"))){
            myWinnerViewId.setText(Integer.toString(calc.addMyWinners()));
            updateMyScore();
            final MediaPlayer myWinnerSound = MediaPlayer.create(getApplicationContext(), R.raw.your_winner);
            myWinnerSound.start();
            Log.i("flagMyfirst gamePoint", Boolean.toString(flagMyFirst));
            myWinnerSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    myWinnerSound.release();
                    soundScore();
                }
            });
            flagMyWonPoint = true;
            flagMyLostPoint = false;
            Log.i("enter my winner","enter");
        }
        else if ( (s.contains("my")||s.contains("ma")||s.contains("mi")) &&
             (s.contains("fau")||s.contains("for")) &&
             (!s.contains("on")&&!s.contains("un")&&!s.contains("own")&&!s.contains("ya")) ){
            myForcedViewId.setText(Integer.toString(calc.addMyForced()));
            updateRivalScore();
            final MediaPlayer myForcedSound = MediaPlayer.create(getApplicationContext(), R.raw.your_forced);
            myForcedSound.start();
            myForcedSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    myForcedSound.release();
                    soundScore();
                }
            });
            flagMyWonPoint = false;
            flagMyLostPoint = true;
            Log.i("enter my forced","enter");
        }
        else if ((s.contains("my")||s.contains("ma"))&&
                (s.contains("un")||s.contains("own")||s.contains("ton"))){
            myUnforcedViewId.setText(Integer.toString(calc.addMyUNForced()));
            updateRivalScore();
            final MediaPlayer myUnforcedSound = MediaPlayer.create(getApplicationContext(), R.raw.your_unforced);
            myUnforcedSound.start();
            myUnforcedSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    myUnforcedSound.release();
                    soundScore();
                }
            });
            flagMyWonPoint = false;
            flagMyLostPoint = true;
            Log.i("enter my unforced","enter");
        }
        else if ((s.contains("opa")||s.contains("opo")||s.contains("oppo")||s.contains("fun")||s.contains("appo"))&&
                (s.contains("win")||s.contains("wie")) ){
            rivalWinnerViewId.setText(Integer.toString(calc.addRivalWinners()));
            updateRivalScore();
            final MediaPlayer oppoWinnerSound = MediaPlayer.create(getApplicationContext(), R.raw.oppo_winner);
            oppoWinnerSound.start();
            oppoWinnerSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    oppoWinnerSound.release();
                    soundScore();
                }
            });
            flagMyWonPoint = false;
            flagMyLostPoint = true;
            Log.i("enter opo winner","enter");
        }
        //opo unforced(change places with opo forced beacause abundant word)
        else if ((s.contains("opa")||s.contains("opo")||s.contains("oppo")||s.contains("fun")||s.contains("appo"))&&
                (s.contains("unfor")||s.contains("enfor")||s.contains("t and for")||s.contains("on for"))){
            rivalUnforcedViewId.setText(Integer.toString(calc.addRivalUNForced()));
            updateMyScore();
            final MediaPlayer oppoUNForcedSound = MediaPlayer.create(getApplicationContext(), R.raw.oppo_unforced);
            oppoUNForcedSound.start();
            oppoUNForcedSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    oppoUNForcedSound.release();
                    soundScore();
                }
            });
            flagMyWonPoint = true;
            flagMyLostPoint = false;
            Log.i("enter opo unforced","enter");
        }
        //opo forced
        else if ((s.contains("opa")||s.contains("opo")||s.contains("oppo")||s.contains("fun")||s.contains("appo"))&&
                (s.contains("fa")||s.contains("for")||s.contains("fr"))  ){
            rivalForcedViewId.setText(Integer.toString(calc.addRivalForced()));
            updateMyScore();
            final MediaPlayer oppoForcedSound = MediaPlayer.create(getApplicationContext(), R.raw.oppo_forced);
            oppoForcedSound.start();
            oppoForcedSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    oppoForcedSound.release();
                    soundScore();
                }
            });
            flagMyWonPoint = true;
            flagMyLostPoint = false;
            Log.i("enter opo forced","enter");
        }

    }

    //check if the user or rival made net and the result depend if won or lost the point
    public void netWon (String s) {

        if ( (s.contains("my")||s.contains("ma")||s.contains("mi")) &&
                (s.contains("net"))) {
            calc.addMyTotalNet();
            if (flagMyWonPoint==true){
                calc.addMyNet();
            }
        }
        if ( (s.contains("opa")||s.contains("opo")||s.contains("oppo")||s.contains("fun")) &&
                (s.contains("net"))) {
            calc.addRivalTotalNet();
            if (flagMyLostPoint==true){
                calc.addRivalNet();
            }
        }
        Log.i("net total check", Integer.toString(calc.getMyTotalNet()));
        Log.i("net check", Integer.toString(calc.getMyNet()));
        Log.i("net flag check", Boolean.toString(flagMyWonPoint));
        myNetViewId.setText(Integer.toString(calc.getMyNet())+"/"+Integer.toString(calc.getMyTotalNet()));
        rivalNetViewId.setText(Integer.toString(calc.getRivalNet())+"/"+Integer.toString(calc.getRivalTotalNet()));
    }

    //check if the user or rival in his first serve or second serve
    public void checkIfServeStatus(String s) {
        if ( (s.contains("my")||s.contains("ma")||s.contains("mi")) &&
                (s.contains("1")||s.contains("one"))){
            flagMyFirst = true;
            flagMySecond = false;
            Log.i("flagMyfirst ServeStatus", Boolean.toString(flagMyFirst));
            final MediaPlayer myFirstServeSound = MediaPlayer.create(getApplicationContext(), R.raw.your_first_serve);
            myFirstServeSound.start();
            myFirstServeSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    myFirstServeSound.release();
                }
            });
        }
        if ( (s.contains("my")||s.contains("ma")||s.contains("mi")) &&
                (s.contains("sec")||s.contains("2"))) {
            flagMyFirst = false;
            flagMySecond = true;
            final MediaPlayer mySecondServeSound = MediaPlayer.create(getApplicationContext(), R.raw.your_second_serve);
            mySecondServeSound.start();
            mySecondServeSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mySecondServeSound.release();
                }
            });
        }
        if ( (s.contains("opa")||s.contains("opo")||s.contains("oppo")||s.contains("fun")||s.contains("appo")) &&
                (s.contains("1"))&&
                (!s.contains("1st")) ){
            flagRivalFirst = true;
            flagRivalSecond = false;
            Log.i("what", "inside first serve voice:"+s);
            final MediaPlayer oppoFirstServeSound = MediaPlayer.create(getApplicationContext(), R.raw.oppo_first_serve);
            oppoFirstServeSound.start();
            oppoFirstServeSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    oppoFirstServeSound.release();
                }
            });
        }
        if ( (s.contains("opa")||s.contains("opo")||s.contains("oppo")||s.contains("fun")||s.contains("appo")) &&
                (s.contains("sec")||s.contains("2"))) {
            flagRivalFirst = false;
            flagRivalSecond = true;
            final MediaPlayer oppoSecondServeSound = MediaPlayer.create(getApplicationContext(), R.raw.oppo_second_serve);
            oppoSecondServeSound.start();
            oppoSecondServeSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    oppoSecondServeSound.release();
                }
            });
        }
    }

    //calculate the serves numbers for user and rival and show it to the user
    public void editServesStatus(){
        if (countMyTotalFirst>0 || countMyTotalSecond>0){
            myFirstPercentageIn = Math.round(countMyTotalFirst*100)/(countMyTotalFirst+countMyTotalSecond);
            myFirstInViewId.setText(Integer.toString(myFirstPercentageIn)+"%");

        }
        if (countMyTotalFirst>0){
            myFirstPercentageWon = Math.round(countMyFirst*100/countMyTotalFirst);
            myFirstWonViewId.setText(Integer.toString(myFirstPercentageWon)+"%");
        }
        if (countMyTotalSecond>0){
            mySecondPercentageWon = Math.round(countMySecond*100/countMyTotalSecond);
            mySecondWonViewId.setText(Integer.toString(mySecondPercentageWon)+"%");
        }
        if (countRivalTotalFirst>0 || countRivalTotalSecond>0){
            rivalFirstPercentageIn = Math.round((countRivalTotalFirst*100)/(countRivalTotalFirst+countRivalTotalSecond));
            rivalFirstInViewId.setText(Integer.toString(rivalFirstPercentageIn)+"%");
        }
        if (countRivalTotalFirst>0){
            rivalFirstPercentageWon = Math.round(countRivalFirst*100/countRivalTotalFirst);
            rivalFirstWonViewId.setText(Integer.toString(rivalFirstPercentageWon)+"%");

        }
        if (countRivalTotalSecond>0){
            rivalSecondPercentageWon = Math.round(countRivalSecond*100/countRivalTotalSecond);
            rivalSecondWonViewId.setText(Integer.toString(rivalSecondPercentageWon)+"%");
        }

    }

    //turn all flags to zero (happens when starting a new point)
    public void zeroFlags (){
        flagMyWonPoint = false;
        flagMyFirst = false;
        flagRivalFirst = false;
        flagMyLostPoint = false;
        flagMySecond = false;
        flagRivalSecond = false;
    }

    //check if the user or rival won the first/second serve
    public void checkServeWon () {
        Log.i("my flag first serves", Boolean.toString(flagMyFirst));
        Log.i("my flag second serves", Boolean.toString(flagMySecond));
        Log.i("my flag won serves", Boolean.toString(flagMyWonPoint));
        Log.i("my flag lost serves", Boolean.toString(flagMyLostPoint));

        if (flagMyWonPoint == true && flagMyFirst == true) {
            Log.i("serves", "won point && myFirst");
            countMyFirst = calc.addMyFirst();
            countMyTotalFirst = calc.addMyTotalFirst();
            Log.i("flagMyfirst ServeWon", Boolean.toString(flagMyFirst));
            editServesStatus();
            zeroFlags();
            Log.i("flagMy ServeStatusZero", Boolean.toString(flagMyFirst));
        }

        if (flagMyLostPoint == true && flagMyFirst == true) {
            Log.i("serves", "lost point && myFirst");
            countMyTotalFirst = calc.addMyTotalFirst();
            editServesStatus();
            zeroFlags();
        }

        if (flagMyWonPoint == true && flagMySecond == true) {
            Log.i("serves", "won point && mySecond");
            countMySecond = calc.addMySecond();
            countMyTotalSecond = calc.addMyTotalSecond();
            editServesStatus();
            zeroFlags();
        }

        if (flagMyLostPoint == true && flagMySecond == true) {
            Log.i("serves", "lost point && mySecond");
            countMyTotalSecond = calc.addMyTotalSecond();
            editServesStatus();
            zeroFlags();
        }

        //Rival serves

        if (flagMyWonPoint == true && flagRivalFirst == true) {
            countRivalTotalFirst = calc.addRivalTotalFirst();
            editServesStatus();
            zeroFlags();
        }

        if (flagMyLostPoint == true && flagRivalFirst == true) {
            countRivalFirst = calc.addRivalFirst();
            countRivalTotalFirst = calc.addRivalTotalFirst();
            editServesStatus();
            zeroFlags();
        }

        if (flagMyWonPoint == true && flagRivalSecond == true) {
            countRivalTotalSecond = calc.addRivalTotalSecond();
            editServesStatus();
            zeroFlags();
        }

        if (flagMyLostPoint == true && flagRivalSecond == true) {
            countRivalSecond = calc.addRivalSecond();
            countRivalTotalSecond = calc.addRivalTotalSecond();
            editServesStatus();
            zeroFlags();
        }

    }

    //add point to user and show it. also check if user won the game
    public void updateMyScore(){
        calc.addMyPoint();
        if (calc.getMyGamePoint()==45){
            myScore.setText("you A");
        }
        else{
            myScore.setText("you "+Integer.toString(calc.getMyGamePoint()));
        }
        rivalScore.setText("rival "+Integer.toString(calc.getRivalGamePoint()));
        totalScoreSet.setText(Integer.toString(calc.getMyGameScore())+":"+Integer.toString(calc.getRivalGameScore()));
        if(calc.checkWin() == true){
            declareWin("won!! congratulations!");
        }
    }

    //add point to rival and show it. also check if rival won the game
    public void updateRivalScore(){
        calc.addRivalPoint();
        myScore.setText("you "+Integer.toString(calc.getMyGamePoint()));
        if (calc.getRivalGamePoint()==45){
            rivalScore.setText("rival A");
        }
        else{
            rivalScore.setText("rival "+Integer.toString(calc.getRivalGamePoint()));
        }
        totalScoreSet.setText(Integer.toString(calc.getMyGameScore())+":"+Integer.toString(calc.getRivalGameScore()));
        if(calc.checkWin() == true){
            declareWin("Lost.. maybe next time");
        }
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
               FirebaseGame game = new FirebaseGame();
               game.createNewGame();
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

        myWinnerViewId = (TextView)findViewById(R.id.myWinnerId);
        myForcedViewId = (TextView)findViewById(R.id.myForcedId);
        myUnforcedViewId = (TextView)findViewById(R.id.myUnforcedId);
        rivalWinnerViewId = (TextView)findViewById(R.id.rivalWinnerId);
        rivalForcedViewId = (TextView)findViewById(R.id.rivalForcedId);
        rivalUnforcedViewId = (TextView)findViewById(R.id.rivalUnforcedId);
        myAcesViewId = (TextView)findViewById(R.id.myAcesId);
        rivalAcesViewId = (TextView)findViewById(R.id.rivalAcesId);
        myDoublesViewId = (TextView)findViewById(R.id.myDoubleFaultsId);
        rivalDoublesViewId = (TextView)findViewById(R.id.rivalDoubleFaultsId);
        myFirstInViewId = (TextView)findViewById(R.id.myServeInId);
        rivalFirstInViewId = (TextView)findViewById(R.id.rivalServeInId);
        myFirstWonViewId = (TextView)findViewById(R.id.myFirstServeId);
        rivalFirstWonViewId= (TextView)findViewById(R.id.rivalFirstServeId);
        mySecondWonViewId = (TextView)findViewById(R.id.mySecondServeId);
        rivalSecondWonViewId = (TextView)findViewById(R.id.rivalSecondServeId);
        myNetViewId = (TextView)findViewById(R.id.myNetId);
        rivalNetViewId = (TextView)findViewById(R.id.rivalNetId);

        zeroFlags();

        //test for score method
        calc = new TennisScoreCalculates();
        userImageProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMyScore();
            }
        });

        rivalImageProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRivalScore();
            }
        });

        //bluetooth
        Button btnOnOff = (Button) findViewById(R.id.btnOnOff);
        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableDisableBT();
            }
        });
    }

}
