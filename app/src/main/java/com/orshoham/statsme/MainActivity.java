package com.orshoham.statsme;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (AutoCompleteTextView) findViewById(R.id.usernameID);
        password = (EditText) findViewById(R.id.passwordID);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() !=null){
            Intent i = new Intent(MainActivity.this, StartActivity.class);
            i.putExtra("Email",firebaseAuth.getCurrentUser().getEmail());
            Log.i("wawaa", firebaseAuth.getCurrentUser().getEmail());
            startActivity(i);
        }

        setTitle("Welcome to StatsMe");

       // Intent i = new Intent(MainActivity.this, StartActivity.class);
       // startActivity(i);

    }

    public void signUp (View view) {

        if (username.getText().toString().matches("") || password.getText().toString().matches("")) {
            handleEmptyInputs();
        } else {

            (firebaseAuth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {

                       if (task.isSuccessful()) {
                           Log.i("signup", "successful");
                           Intent i = new Intent(MainActivity.this, StartActivity.class);
                           startActivity(i);
                       } else {
                           Log.e("SignupERROR", task.getException().toString());
                           Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }
            });

        }
    }


    public void login (View view) {
        if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            handleEmptyInputs();
            return;
        }
        // regex from https://emailregex.com/
        if(!username.getText().toString().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")){
            handleIllegalEmail();
            return;
        }
        try {
            (firebaseAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.i("signin", "successful");
                                Intent i = new Intent(MainActivity.this, StartActivity.class);
                                i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                startActivity(i);
                            } else {
                                Log.e("LoginERROR", task.getException().toString());
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (NullPointerException e){
            Log.e("LoginERROR","Error occurred in the login process: " + e.getMessage());
        }
    }

    private void handleIllegalEmail() {
        Toast.makeText(this, "Invalid Mail Address", Toast.LENGTH_SHORT).show();
    }

    private void handleEmptyInputs() {
        Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();
    }

    public void handleGoogleAuth(View view) {

    }

    public void handleFacebookAuth(View view) {

    }
}

   /* private void addAdapterToViews() {

        Account[] accounts = AccountManager.get(this).getAccounts();
        Set<String> emailSet = new HashSet<String>();
        for (Account account : accounts) {
            if (username.matcher(account.name).matches()) {
                emailSet.add(account.name);
            }

        }
        auto_mail.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(emailSet)));

    }*/
