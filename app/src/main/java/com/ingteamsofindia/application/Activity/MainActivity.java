package com.ingteamsofindia.application.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ingteamsofindia.application.Fragments.HomeFragment;
import com.ingteamsofindia.application.Fragments.NotificationFragment;
import com.ingteamsofindia.application.Fragments.ProfileFragment;
import com.ingteamsofindia.application.Fragments.SearchFragment;
import com.ingteamsofindia.application.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;

    /*~FIREBASE~*/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext = MainActivity.this;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started...");

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        setupFirebaseAuth();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectorFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectorFragment = new SearchFragment();
                        break;
                    case R.id.nav_add:
                        selectorFragment = null;
                        startActivity(new Intent(mContext, PostActivity.class));
                        break;
                    case R.id.nav_notification:
                        selectorFragment = new NotificationFragment();
                        break;
                    case R.id.nav_profile:
                        selectorFragment = new ProfileFragment();
                        break;
                }
                if (selectorFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectorFragment).commit();
                }
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


    }

    /*@@@@@@@@@@@@@@@@@@@@@@@@@ <FIREBASE AUTH> @@@@@@@@@@@@@@@@@@@@@@@@@*/

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: Starting...");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                checkCurrentUser(user);
                if (user != null) {
                    Log.d(TAG, "setupFirebaseAuth: SignIn");

                } else {
                    Log.d(TAG, "setupFirebaseAuth: Signed_out");
                }
            }
        };
    }
    private void checkCurrentUser(FirebaseUser user) {
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");
        if (user == null) {
            sendUserToTheLogin();
        } else {
            checkUserExistence();
        }
    }
    private void checkUserExistence() {
        final String current_user_id = mAuth.getUid();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assert current_user_id != null;
                if (!snapshot.hasChild(current_user_id)){
                    sendUserToTheSetup();
                }else {
                    Log.d(TAG, "onDataChange: Welcome UID : "+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendUserToTheSetup() {
        Intent intent = new Intent(mContext, SetupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void sendUserToTheLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}