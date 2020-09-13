package com.ingteamsofindia.application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.ingteamsofindia.application.Activity.CommentActivity;
import com.ingteamsofindia.application.Model.Post;
import com.ingteamsofindia.application.Model.User;
import com.ingteamsofindia.application.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final String TAG = "PostAdapter";
    private Context mContext;
    private List<Post> mPostList;
    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPostList) {
        this.mContext = mContext;
        this.mPostList = mPostList;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Calling...");
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item,parent,false);
        return new PostViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Calling...");
        final Post user_posts = mPostList.get(position);

        Picasso.get().load(user_posts.getCurrentUser_PostImage()).error(R.drawable.ic_error).into(holder.mPost_Image);
        holder.mPost_UserFullName.setText(user_posts.getCurrentUser_FullName());
        holder.mPost_Description.setText(user_posts.getCurrentUser_PostDescription());
        holder.mPost_Date.setText(user_posts.getCurrentUser_CurrentDate());

        FirebaseDatabase.getInstance().getReference().child("Users").child(user_posts.getCurrentUser_ID_User()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                Picasso.get().load(user.getCurrentUser_ProfileImageUrl()).error(R.drawable.ic_error).into(holder.mProfile_Image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        isLiked(user_posts.getCurrentUser_ID_Post(),holder.mPost_Like);
        numOfLike(user_posts.getCurrentUser_ID_Post(),holder.mPost_numOfLike);
        getComment(user_posts.getCurrentUser_ID_Post(), holder.mPost_numOfComment);
        holder.mPost_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.mPost_Like.getTag().equals("Like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(user_posts.getCurrentUser_ID_Post())
                            .child(firebaseUser.getUid()).setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(user_posts.getCurrentUser_ID_Post())
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                }
            }
        });
        holder.mPost_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("currentUser_ID_Post",user_posts.getCurrentUser_ID_Post());
                intent.putExtra("currentUser_ID_User",user_posts.getCurrentUser_ID_User());
                mContext.startActivity(intent);
            }
        });
        holder.mPost_numOfComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("currentUser_ID_Post",user_posts.getCurrentUser_ID_Post());
                intent.putExtra("currentUser_ID_User",user_posts.getCurrentUser_ID_User());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: Calling...");
        return mPostList.size();
    }
    public static class PostViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "ViewHolder";

        public CircleImageView mProfile_Image;
        public ImageView mPost_Image,mPost_More,mPost_Like,mPost_Comment,mPost_SaveImage;
        public TextView mPost_UserFullName,mPost_numOfLike,mPost_numOfComment,mPost_Date;
        public SocialTextView mPost_Description;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: Calling...");
            mProfile_Image = itemView.findViewById(R.id.profileImg_Post);
            mPost_Image = itemView.findViewById(R.id.post_Image);
            mPost_More = itemView.findViewById(R.id.more);
            mPost_Like = itemView.findViewById(R.id.like);
            mPost_Comment = itemView.findViewById(R.id.comment);
            mPost_SaveImage = itemView.findViewById(R.id.save);
            mPost_UserFullName = itemView.findViewById(R.id.post_FullName);
            mPost_numOfLike = itemView.findViewById(R.id.numberOfLike);
            mPost_numOfComment = itemView.findViewById(R.id.numberOfComment);
            mPost_Description = itemView.findViewById(R.id.post_description);
            mPost_Date = itemView.findViewById(R.id.mPostDate);

        }
    }
    private void isLiked(String postID, final ImageView imageView){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("Liked");
                }else {
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void numOfLike(String postID, final TextView text){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                text.setText(snapshot.getChildrenCount()+" Like");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getComment(String postID, final TextView text){
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                text.setText("View all "+snapshot.getChildrenCount()+ " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
