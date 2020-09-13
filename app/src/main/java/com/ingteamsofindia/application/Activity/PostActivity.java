package com.ingteamsofindia.application.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.widget.Hashtag;
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.ingteamsofindia.application.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "PostActivity";
    private Context mContext = PostActivity.this;
    private ImageView mImageAdded;
    private SocialAutoCompleteTextView mDescription;
    private Uri mImageUri;
    private String mImageUrl, uID, currentDate, mUser_ProfileImage, mUser_ProfileFullName,mUser_ProfileUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImageAdded = findViewById(R.id.post_image_added);
        mDescription = findViewById(R.id.post_description);
        DateFormat Date = DateFormat.getDateInstance();
        Calendar calendar = Calendar.getInstance();
        currentDate = Date.format(calendar.getTime());
        TextView mPost = findViewById(R.id.post);
        ImageView mClose = findViewById(R.id.close);
        uID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToTheMain();
            }
        });
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
        CropImage.activity().start(PostActivity.this);
    }

    private void upload() {
        Log.d(TAG, "upload: Code is running");
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");
        pd.show();

        /*Users Tags Data Retrieve*/
        userDataRetrieve();
        
        if (mImageUri != null) {
            Log.d(TAG, "upload: Image Uri is exist");
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Posts").child(currentDate + "_" + System.currentTimeMillis() + ".jpg");
            StorageTask uploadTask = filePath.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    assert downloadUri != null;
                    mImageUrl = downloadUri.toString();


                    DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Posts");
                    String postID = postRef.push().getKey();

                    HashMap<String, Object> postMap = new HashMap<>();
                    postMap.put("currentUser_ID_User", uID);
                    postMap.put("currentUser_ID_Post", postID);
                    postMap.put("currentUser_PostDescription", mDescription.getText().toString());
                    postMap.put("currentUser_PostImage", mImageUrl);
                    postMap.put("currentUser_CurrentDate", currentDate);
                    postMap.put("currentUser_FullName", mUser_ProfileFullName);
                    postMap.put("currentUser_Username", mUser_ProfileUsername);


                    postRef.child(postID).setValue(postMap);


                    DatabaseReference dbPostHashTag = FirebaseDatabase.getInstance().getReference("HashTags");
                    List<String> hashTags = mDescription.getHashtags();
                    if (!hashTags.isEmpty()) {
                        for (String tag : hashTags) {
                            postMap.clear();
                            postMap.put("currentUser_ID", uID);
                            postMap.put("currentUser_ID_Post", postID);
                            postMap.put("currentUser_Tags", tag.toLowerCase());

                            dbPostHashTag.child(tag.toLowerCase()).push().setValue(postMap);
                        }
                    }
                    pd.dismiss();
                    sendUserToTheMain();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, "No image was selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void userDataRetrieve() {
        Log.d(TAG, "userDataRetrieve: ");
        DatabaseReference dbPost = FirebaseDatabase.getInstance().getReference().child("Users");
        dbPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    mUser_ProfileImage = dataSnapshot.child("currentUser_ProfileImageUrl").getValue(String.class);
                    mUser_ProfileFullName = dataSnapshot.child("currentUser_FullName").getValue(String.class);
                    mUser_ProfileUsername = dataSnapshot.child("currentUser_Username").getValue(String.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            Picasso.get().load(mImageUri).error(R.drawable.ic_error).into(mImageAdded);
        } else {
            Toast.makeText(mContext, "Try again!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onActivityResult: Try Again!");
            sendUserToTheMain();
        }
    }

    private void sendUserToTheMain() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final ArrayAdapter<Hashtag> hashtagAdapter = new HashtagArrayAdapter<>(getApplicationContext());
        FirebaseDatabase.getInstance().getReference().child("HashTags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    hashtagAdapter.add(new Hashtag(ds.getKey(),(int) ds.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDescription.setHashtagAdapter(hashtagAdapter);
    }
}