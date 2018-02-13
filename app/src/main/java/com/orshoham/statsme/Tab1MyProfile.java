package com.orshoham.statsme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
        //create element of TAB1Stats for checking who is the winner
        StatsTab1 checkWinner = new StatsTab1();
        //show the game numbers and score form DB by showing "gameList" and add to view of this activity
        for(int i=0;i<gameList.size();i++){
            //Log.i("myset3(TAB1)", Integer.toString(gameList.get(i).getMySet3()));
            String winStatus = checkWinner.checkWinnerStringForGamesList(dbGames, gameList.get(i).getGameNumber());
            gamesPlayedList.add("Game#" + Integer.toString(gameList.get(i).getGameNumber()) + "      "
                    +Integer.toString(gameList.get(i).getMySet1())+":"
                    +Integer.toString(gameList.get(i).getRivalSet1())+", "
                    +Integer.toString(gameList.get(i).getMySet2())+":"
                    +Integer.toString(gameList.get(i).getRivalSet2())+", "
                    +Integer.toString(gameList.get(i).getMySet3())+":"
                    +Integer.toString(gameList.get(i).getRivalSet3())+"   "
                    + winStatus
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

    public void update(){
        Fragment currentFragment = getFragmentManager().findFragmentByTag("container");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
    }

        @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_my_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();

        final StatsTab1 mainStats = new StatsTab1();


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
        final ArrayList<String> gamesPlayedList = new ArrayList<>();

        final ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, gamesPlayedList){
        @Override
        //color the text if win or loss
        public View getView(int position, View convertView, ViewGroup parent){
            // Get the current item from ListView
            View view = super.getView(position,convertView,parent);
            //insert to var winStatus String that contained you win/you lost
            String winStatus = mainStats.checkWinnerStringForGamesList(dbGames, position+1);
            //itemView = the item of the specific row on the list
            TextView itemView = (TextView) view.findViewById(android.R.id.text1);
            //selectedFromList= the string of the specific row
            String selectedFromList = (String) (gamesListView.getItemAtPosition(position));
            Spannable spannable = new SpannableString(selectedFromList);
            if(winStatus.contains("win"))
            {
                // Set blue color between specific indexes in the string of the specific row
                spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#0f65af")), 25, 36, 0);
            }
            else if(winStatus.contains("lost"))
            {
                //set red color
                spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#db084b")), 25, 36, 0);

            }
            else
            {
                // Set black
                itemView.setTextColor(Color.parseColor("#000000"));
            }
            //itemView.setText(spannable, TextView.BufferType.SPANNABLE);
            itemView.setText(spannable);
            return view;
        }
        };
        gamesListView.setAdapter(arrayAdapter);
        //declare the database in thiש activity
        dbGames = new DBGames(getActivity());
        showGameList(gamesPlayedList);
        //sending the game number of item in the list that being clicked and open that specific game
        gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), GameStats.class);
                String listContent = gamesPlayedList.get(i);
                int listIndex = gamesPlayedList.indexOf(listContent)+1;
                Log.i("listIndex", Integer.toString(listIndex));
                intent.putExtra("gameNumber", Integer.toString(listIndex));
                startActivity(intent);
            }
        });
        //delete game by long click
        gamesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Intent intent = new Intent(getActivity(), GameStats.class);
                //var listContent get the index (the row) that being clicked
                String listContent = gamesPlayedList.get(i);
                int listIndex = gamesPlayedList.indexOf(listContent);
                Log.i("removing index", Integer.toString(listIndex));
                //delete the row by operate function in DB class. send the number of the game that we want to delete
                dbGames.deleteOneGame(listIndex+1);
                //delete the row in this list
                gamesPlayedList.remove(listIndex);
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });



        //define main stats and show them by functions
        gamesPlayedView = (TextView) rootView.findViewById(R.id.gamesPlayedId);
        gamesPlayedView.setText("#GAMES PLAYED: "+ Integer.toString(mainStats.numOfGamesPlayed(dbGames)));
        numberOfWinsView = (TextView) rootView.findViewById(R.id.profile_stats2);
        numberOfWinsView.setText("#WINS: "+ Integer.toString(mainStats.checkSumWins(dbGames)));
        numberOfWinsView = (TextView) rootView.findViewById(R.id.profile_stats3);
        numberOfWinsView.setText("#LOSSES: "+ Integer.toString(mainStats.checkSumLosses(dbGames)));
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

