package com.orshoham.statsme.tab4;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orshoham.statsme.R;
import com.orshoham.statsme.Tab1MyProfile;

public class Tab4EditProfile extends AppCompatActivity {

    ImageView myImageProfileView;

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
        setContentView(R.layout.tab4_edit_profile);


        myImageProfileView = (ImageView) findViewById(R.id.imageId);
        savePhoto();
    }

}
