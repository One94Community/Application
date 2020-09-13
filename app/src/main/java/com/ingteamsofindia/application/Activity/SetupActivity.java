package com.ingteamsofindia.application.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ingteamsofindia.application.Model.User;
import com.ingteamsofindia.application.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class SetupActivity extends AppCompatActivity {
    private static final String TAG = "SetupActivity";
    /*~FIREBASE~*/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseRef;
    private Context mContext = SetupActivity.this;

    /*VARIABLE*/
    private EditText mFullName, mUsername, mCountry;
    private Button mSaveButton;
    private String uID, currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Log.d(TAG, "onCreate: Started...");
        setupFirebaseAuth();
        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mFullName = findViewById(R.id.etUser_FullName);
        mUsername = findViewById(R.id.etUser_Username);
        mCountry = findViewById(R.id.etUser_Country);
        mSaveButton = findViewById(R.id.info_saveBtn);

        DateFormat Date = DateFormat.getDateInstance();
        Calendar calendar = Calendar.getInstance();
        currentDate = Date.format(calendar.getTime());

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

    }

    /*@@@@@@@@@@@@@@@@@@@@@@@@@ <FIREBASE AUTH> @@@@@@@@@@@@@@@@@@@@@@@@@*/
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: Setting up firebase auth.");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged: Signed_In");
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:Signed_Out");
                }
                // ...
            }
        };
    }

    private void saveUserInformation() {
        Log.d(TAG, "saveUserInformation: Initializing...");
        final String fullName, username, country, email;
        fullName = mFullName.getText().toString().trim();
        username = mUsername.getText().toString().trim();
        country = mCountry.getText().toString().trim();
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();


        if (!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(country)) {
            /*~Code Run~*/

            User user = new User("Not Applicable", country, currentDate, email, fullName, uID, "https://cutestangel.files.wordpress.com/2010/01/320px-smirc-thumbsup.png", username+"_"+System.currentTimeMillis());

            mDatabaseRef.child(uID).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(mContext, "Your Account is Complete, Enjoy!!!", Toast.LENGTH_LONG).show();
                    sendUserToMain();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            if (TextUtils.isEmpty(fullName)) {
                mFullName.setError("Enter Full Name");
                mFullName.requestFocus();
            }
            if (TextUtils.isEmpty(username)) {
                mUsername.setError("Enter Username");
                mUsername.requestFocus();
            }
            if (TextUtils.isEmpty(country)) {
                mCountry.setError("Enter Country");
                mCountry.requestFocus();
            }
        }

    }

    private void sendUserToMain() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}