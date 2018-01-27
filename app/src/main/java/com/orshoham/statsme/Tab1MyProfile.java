package com.orshoham.statsme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.Manifest;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.name;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.orshoham.statsme.FirebaseGame.userID;


public class Tab1MyProfile extends Fragment  {

    TextView userEmail;
    ImageView userImageProfileView;

    //var for the list of games
    static ListView gamesListView;
    ArrayList<String> games = new ArrayList<>();

    private StorageReference mStorageRef;
    private DatabaseReference mUserDatabse;
    private FirebaseAuth mAuth;

    private SharedPreference sharedPreferenceFirstTime;

    private TextView gamesPlayedView;
    private TextView numberOfWinsView;
    private TextView avgWinnersView;
    private TextView avgUNForcedView;

    DBGames dbGames;

    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    //function that change image from library to bitmap
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            final Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                //get the signed in user
                FirebaseUser user = mAuth.getCurrentUser();
                final String userID = user.getUid();

                StorageReference storageReference = mStorageRef.child("images/users/" + userID + "/" + name + ".jpg");
                storageReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        mUserDatabse.child("Users/"+userID+"/UserDetails/userid").setValue(mAuth.getCurrentUser().getUid());
                        mUserDatabse.child("Users/"+userID+"/UserDetails/imageurl").setValue(downloadUri.toString());
                        savePhoto();
                        //Picasso.with(getActivity()).load(downloadUri).fit().centerCrop().into(userImageProfileView);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return null;

        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
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

                ImageDownloader task = new ImageDownloader();
                Bitmap myImage;

                try {
                    myImage = task.execute(imageurl).get();
                    userImageProfileView.setImageBitmap(myImage);

                    //resize options. do I need it?
                    //userImageProfileView.setImageBitmap(getResizedBitmap(myImage, 80, 100));
                    //userImageProfileView.setImageBitmap(Bitmap.createScaledBitmap(myImage, 80, 100, false));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        userIdRef.addListenerForSingleValueEvent(eventListener);

    }

    public void showGameList(ArrayList<String> gamesPlayedList){
        //create "gameList" which include all Games SQL Database
        List<GamesSQL> gameList = dbGames.getAllGames();
        //show the database ID by showing "gameList Id" and add to view of this activity
        for(int i=0;i<gameList.size();i++){
            Log.i("myset3(TAB1)", Integer.toString(gameList.get(i).getMySet3()));
            gamesPlayedList.add("Game Number " + Integer.toString(gameList.get(i).getId()) + " final Score: "
                    +Integer.toString(gameList.get(i).getMySet1())+":"
                    +Integer.toString(gameList.get(i).getRivalSet1())+", "
                    +Integer.toString(gameList.get(i).getMySet2())+":"
                    +Integer.toString(gameList.get(i).getRivalSet2())+", "
                    +Integer.toString(gameList.get(i).getMySet3())+":"
                    +Integer.toString(gameList.get(i).getRivalSet3())
            );
        }
    }

    /*
    public void updateListGames(){
        ArrayList<String> gamesPlayedList = new ArrayList<>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, gamesPlayedList);
        gamesListView.setAdapter(arrayAdapter);
        dbGames = new DBGames(getActivity());
        showGameList(gamesPlayedList);
    }*/

        @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_my_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();



            //check if user is not logged in
        if(mAuth.getCurrentUser() == null){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }

        //FIREBASE DATABASE INSTANCE
        mUserDatabse = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //insert the user Email into firebase database
        mUserDatabse = FirebaseDatabase.getInstance().getReference();
        mUserDatabse.child("Users/"+userID+"/UserDetails/UserName").setValue(mAuth.getCurrentUser().getEmail());

        //check if the user is in the app for the first time
        sharedPreferenceFirstTime=new SharedPreference(getActivity());
        //sharedPreferenceFirstTime.setApp_runFirst("FIRST"); erase // if its the first time
        if(sharedPreferenceFirstTime.getApp_runFirst().equals("FIRST"))
        {
            // That's mean First Time Launch
            // After your Work , SET Status NO
            sharedPreferenceFirstTime.setApp_runFirst("NO");
            UserDetails firstUserDetails = new UserDetails();
            firstUserDetails.userFirstTimeApp();
            Log.i("app run first time?", "yes!!");
        }

            //get permission to gallery
        rootView.findViewById(R.id.profile_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                else {
                    getPhoto();
                }
            }
        });
        //show the photo that the user uploaded. show it every oncreate
        savePhoto();

        //define profile image
        userImageProfileView = (ImageView) rootView.findViewById(R.id.profile_pic);

        //define the games list view
        gamesListView = (ListView) rootView.findViewById(R.id.gamesListView);
        ArrayList<String> gamesPlayedList = new ArrayList<>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, gamesPlayedList);
        gamesListView.setAdapter(arrayAdapter);
        //declare the database in thid activity
        dbGames = new DBGames(getActivity());
        showGameList(gamesPlayedList);

        /*
        gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), GameStats.class);
                intent.putExtra("gameId", gameId.get(i));
                startActivity(intent);
            }
        });*/

        //define main stats and show them by functions
        StatsTab1 mainStats = new StatsTab1();
        gamesPlayedView = (TextView) rootView.findViewById(R.id.gamesPlayedId);
        gamesPlayedView.setText("NUM OF GAMES PLAYED: "+ Integer.toString(mainStats.numOfGamesPlayed(dbGames)));
        numberOfWinsView = (TextView) rootView.findViewById(R.id.profile_stats2);
        numberOfWinsView.setText("NUM OF WINS: "+ Integer.toString(mainStats.checkSumWins(dbGames)));
        numberOfWinsView = (TextView) rootView.findViewById(R.id.profile_stats3);
        numberOfWinsView.setText("NUM OF LOSSES: "+ Integer.toString(mainStats.checkSumLosses(dbGames)));
        avgWinnersView = (TextView) rootView.findViewById(R.id.profile_stats4);
        avgWinnersView.setText("AVG WINNERS PG: "+ Double.toString(mainStats.avgWinnersPG(dbGames)));
        Log.i("avg winners", Double.toString(mainStats.avgWinnersPG(dbGames)));
        avgUNForcedView = (TextView) rootView.findViewById(R.id.profile_stats5);
        avgUNForcedView.setText("AVG UF PG: "+ Double.toString(mainStats.avgUNForcedPG(dbGames)));





        //initial text before games have played
        TextView initialText = (TextView) rootView.findViewById(R.id.initialText);
        initialText.setText("You have not played any games yet");
        //check if the DB games isnt null. if it isn't then make the last row disappear
        if(dbGames.checkTableGamesNotEmpty()){
            initialText.setVisibility(View.GONE);
        }

        return rootView;
    }
}

