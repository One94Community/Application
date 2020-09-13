package com.ingteamsofindia.application.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ingteamsofindia.application.Adapter.PostAdapter;
import com.ingteamsofindia.application.Model.Post;
import com.ingteamsofindia.application.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView_Post;
    private PostAdapter mPostAdapter;
    private List<Post> mPostList;
    private List<String> mFollowingList;
    private RelativeLayout mHomeProgress;
    private String
            currentUser_CurrentDate,
            currentUser_FullName,
            currentUser_ID_Post,
            currentUser_ID_User,
            currentUser_PostDescription,
            currentUser_PostImage,
            currentUser_Username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView_Post = view.findViewById(R.id.recyclerViewUser);
        mRecyclerView_Post.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        mRecyclerView_Post.setLayoutManager(layoutManager);
        mPostList = new ArrayList<>();
        mPostAdapter = new PostAdapter(getActivity(), mPostList);
        mRecyclerView_Post.setAdapter(mPostAdapter);
        mFollowingList = new ArrayList<>();



        checkFollowingUser();
        return view;
    }

//    private void checkFollowingUser() {
//        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mFollowingList.clear();
//                for (DataSnapshot ds : snapshot.getChildren()){
//                    mFollowingList.add(ds.getKey());
//                }
//                readPost();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void readPost() {
//        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mPostList.clear();
//                for (DataSnapshot ds : snapshot.getChildren()){
//                    Post post = ds.getValue(Post.class);
//                    for (String id : mFollowingList){
//                        if (post.getCurrentUser_ID_User().equals(id)){
//                            mPostList.add(post);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void checkFollowingUser() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mFollowingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    mFollowingList.add(dataSnapshot.getKey());
                }
                readPost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readPost() {

        DatabaseReference dbPost = FirebaseDatabase.getInstance().getReference().child("Posts");
        dbPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        mHomeProgress.setVisibility(View.VISIBLE);
                        currentUser_CurrentDate = dataSnapshot.child("currentUser_CurrentDate").getValue(String.class);
                        currentUser_FullName = dataSnapshot.child("currentUser_FullName").getValue(String.class);
                        currentUser_ID_Post = dataSnapshot.child("currentUser_ID_Post").getValue(String.class);
                        currentUser_ID_User = dataSnapshot.child("currentUser_ID_User").getValue(String.class);
                        currentUser_PostDescription = dataSnapshot.child("currentUser_PostDescription").getValue(String.class);
                        currentUser_PostImage = dataSnapshot.child("currentUser_PostImage").getValue(String.class);
                        currentUser_Username = dataSnapshot.child("currentUser_Username").getValue(String.class);


                        Post user_posts = new Post(currentUser_CurrentDate, currentUser_FullName, currentUser_ID_Post, currentUser_ID_User, currentUser_PostDescription, currentUser_PostImage, currentUser_Username);
                        mPostList.add(user_posts);
                    }
                    mPostAdapter.notifyDataSetChanged();
                    mHomeProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHomeProgress = view.findViewById(R.id.mHomeProgressBar);
        mHomeProgress.setVisibility(View.VISIBLE);
        checkFollowingUser();

    }

}