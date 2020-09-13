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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ingteamsofindia.application.R;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";
    /*~VARIABLE~*/
    private Button regButton;
    private EditText regEmail, regPassword, regConfirmPassword;
    private RelativeLayout regProgressView;
    private String email, password, confirmPassword;
    private TextView loginGo;
    private Context mContext;
    /*~FIREBASE~*/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Log.d(TAG, "onCreate: Started...");
        mContext = RegistrationActivity.this;
        mAuth = FirebaseAuth.getInstance();

        regEmail = findViewById(R.id.etRegisterEmailAddress);
        regPassword = findViewById(R.id.etRegisterPassword);
        regConfirmPassword = findViewById(R.id.etRegisterConfirmPassword);
        regButton = findViewById(R.id.mRegistrationButton);
        regProgressView = findViewById(R.id.mRegProgressBar);
        loginGo = findViewById(R.id.reflectedToSignIn);

        setupFirebaseAuth();
        /*Go to Login Activity*/
        mGoTOLoginActivity();
        /*Create a Account*/
        mCreateNewAccount();

    }
    private void mGoTOLoginActivity() {
        Log.d(TAG, "mGoTOLoginActivity: Started...");
        loginGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(mContext, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }
    private void mCreateNewAccount() {
        Log.d(TAG, "mCreateNewAccount: Started...");
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = regEmail.getText().toString();
                password = regPassword.getText().toString();
                confirmPassword = regConfirmPassword.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)) {
                    if (password.equals(confirmPassword)) {
                        regProgressView.setVisibility(View.VISIBLE);
                        /*Authentication SignUp*/
                        mAuthenticationSignUp();

                    } else {
                        regConfirmPassword.setError("Password & Confirm Password Does Not Match");
                        regConfirmPassword.requestFocus();
                    }
                } else {
                    if (TextUtils.isEmpty(email)) {
                        regEmail.setError("Please Enter Valid Email ID.");
                        regEmail.requestFocus();
                    }
                    if (TextUtils.isEmpty(password)) {
                        regPassword.setError("Please Enter Valid Password.");
                        regPassword.requestFocus();
                    }
                    if (TextUtils.isEmpty(confirmPassword)) {
                        regConfirmPassword.setError("Please Enter Valid Confirm Password.");
                        regConfirmPassword.requestFocus();
                    }
                }
            }
        });
    }
    private void mAuthenticationSignUp() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                /*~Send Verification Email~*/
                sendVerificationEmail();
                sendUserToLogin();
                mAuth.signOut();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                regProgressView.setVisibility(View.GONE);
            }
        });
    }
    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(mContext, "Registration Success, Please Verified your email", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    regProgressView.setVisibility(View.GONE);
                }
            });
        }
    }
    private void sendUserToLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /*@@@@@@@@@@@@@@@@@@@@@@@@@ <FIREBASE AUTH> @@@@@@@@@@@@@@@@@@@@@@@@@*/
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: Setting up firebase auth.");
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
                    regProgressView.setVisibility(View.GONE);
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