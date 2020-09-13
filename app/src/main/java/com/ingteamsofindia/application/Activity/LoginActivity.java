package com.ingteamsofindia.application.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ingteamsofindia.application.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    /*~VARIABLE~*/
    private Button loginButton, registrationGo;
    private EditText loginEmail, loginPassword;
    private RelativeLayout loginProgressView;
    private String email, password;
    private Context mContext;

    /*~FIREBASE~*/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final Boolean CHECK_IF_VERIFIED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: Started...");
        mContext = LoginActivity.this;
        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.etLoginEmailAddress);
        loginPassword = findViewById(R.id.etLoginPassword);
        loginButton = findViewById(R.id.mLoginButton);
        registrationGo = findViewById(R.id.mRegistrationActivityGo);
        loginProgressView = findViewById(R.id.mLoginProgressBar);

        setupFirebaseAuth();
        /*Go to Registration Activity*/
        mGoToRegistrationActivity();
        /*Login Users*/
        mAuthenticationSignIn();

    }
    private void mGoToRegistrationActivity() {
        registrationGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(mContext, RegistrationActivity.class);
                startActivity(regIntent);
            }
        });
    }
    private void mAuthenticationSignIn() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = loginEmail.getText().toString();
                password = loginPassword.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    loginProgressView.setVisibility(View.VISIBLE);
                    FirebaseUser user = mAuth.getCurrentUser();
                    /*Authentication SignIn*/
                    mAuthenticationLogin();
                } else {
                    if (TextUtils.isEmpty(email)) {
                        loginEmail.setError("Please Enter Valid Email ID.");
                        loginEmail.requestFocus();
                    }
                    if (TextUtils.isEmpty(password)) {
                        loginPassword.setError("Please Enter Valid Password.");
                        loginPassword.requestFocus();
                    }
                }
            }
        });
        if (mAuth.getCurrentUser() != null) {
            sendUserToMain();
        }
    }
    private void mAuthenticationLogin() {
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                if (user.isEmailVerified()) {
                    Log.d(TAG, "onComplete: success. email is verified.");
                    sendUserToMain();
                } else {
                    Toast.makeText(mContext, "Email is not verified \n check your email inbox.", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    loginProgressView.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                loginProgressView.setVisibility(View.GONE);
                mAuth.signOut();
            }
        });
    }
    private void sendUserToMain() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

/*@@@@@@@@@@@@@@@@@@@@@@@@@ <FIREBASE AUTH> @@@@@@@@@@@@@@@@@@@@@@@@@*/

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged: Signed_In:");
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:Signed_Out");
                    loginProgressView.setVisibility(View.GONE);
                }
                // ...
            }
        };
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