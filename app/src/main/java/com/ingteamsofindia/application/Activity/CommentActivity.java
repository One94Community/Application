package com.ingteamsofindia.application.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ingteamsofindia.application.Model.User;
import com.ingteamsofindia.application.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {
    private static final String TAG = "CommentActivity";
    private EditText addComment;
    private CircleImageView profileImage;
    private TextView post;
    private String postID,authorId;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addComment = findViewById(R.id.addComment);
        profileImage = findViewById(R.id.imageProfile);
        post = findViewById(R.id.commentPost);
        Intent intent = getIntent();
        postID = intent.getStringExtra("currentUser_ID_Post");
        authorId = intent.getStringExtra("currentUser_ID_User");
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        getUserImage();
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(addComment.getText().toString())){
                    Toast.makeText(CommentActivity.this, "No comment added", Toast.LENGTH_SHORT).show();
                }else {
                    putComment();
                }
            }
        });
    }

    private void putComment() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("comment", addComment.getText().toString());
        map.put("publisher", fUser.getUid());
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postID).push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CommentActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CommentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserImage() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getCurrentUser_ProfileImageUrl()).placeholder(R.drawable.ic_person).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}