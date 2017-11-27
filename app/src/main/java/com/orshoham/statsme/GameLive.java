package com.orshoham.statsme;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GameLive extends AppCompatActivity implements RecognitionListener {
    private String TAG = "TEST";

    private static SpeechRecognizer speech = null;
    private Intent intent;
    boolean flag = true;


    public void startGame (View view) {
        Log.i(TAG, "app started");
        FirebaseGame.getSpeechCountByWord();
        speechRecognizer();
    }

    public void stop (View view) {
        this.flag = false;
        Log.i(TAG, "flag false");
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_live);



    }

}
