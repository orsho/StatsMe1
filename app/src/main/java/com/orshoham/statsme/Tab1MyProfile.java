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

import static android.R.attr.name;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class Tab1MyProfile extends Fragment  {

    TextView userEmail;
    ImageView userImageProfileView;

    //var for the list of games
    ListView gamesListView;
    ArrayList<String> games = new ArrayList<>();

    private StorageReference mStorageRef;
    private DatabaseReference mUserDatabse;
    private FirebaseAuth mAuth;

    private SharedPreference sharedPreferenceFirstTime;

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


    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

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

        @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_my_profile, container, false);

        //define profile image
        userImageProfileView = (ImageView) rootView.findViewById(R.id.profile_pic);

        //this is just for me for knowing which user is logged
        userEmail = (TextView) rootView.findViewById(R.id.profile_stats6);

        mAuth = FirebaseAuth.getInstance();

            //check if user is not logged in
        if(mAuth.getCurrentUser() == null){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }

        //insert user var the Email of the current user (if user logged in)
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        userEmail.setText("AVG ACES PG(your user):" + user.getEmail());

        //FIREBASE DATABASE INSTANCE
        mUserDatabse = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //insert the user Email and num of games into firebase database
        mUserDatabse = FirebaseDatabase.getInstance().getReference();
        mUserDatabse.child("Users/"+userID+"/UserDetails/UserName").setValue(mAuth.getCurrentUser().getEmail());

        //check if the user is in the app for the first time
        sharedPreferenceFirstTime=new SharedPreference(getActivity());
        //sharedPreferenceFirstTime.setApp_runFirst("FIRST");
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

        //define the games list view
        gamesListView = (ListView) rootView.findViewById(R.id.gamesListView);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, games);
        gamesListView.setAdapter(arrayAdapter);

        //initial text before games have played
        TextView initialText = (TextView) rootView.findViewById(R.id.initialText);
        initialText.setText("You have not played any games yet");



        return rootView;
    }
}

