package com.ingteamsofindia.application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ingteamsofindia.application.Model.User;
import com.ingteamsofindia.application.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.SearchViewHolder> {
    private Context mContext;
    private List<User> mUserList;
    private boolean isFragment;
    FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<User> mUserList, boolean isFragment) {
        this.mContext = mContext;
        this.mUserList = mUserList;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_user_item,parent,false);
        return new UserAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = mUserList.get(position);
        holder.btnFollow.setVisibility(View.VISIBLE);
        holder.mUsername.setText(user.getCurrentUser_Username());
        holder.mFullName.setText(user.getCurrentUser_FullName());
        Picasso.get().load(user.getCurrentUser_ProfileImageUrl()).placeholder(R.drawable.ic_person).into(holder.mProfileImage);
        isFollow(user.getCurrentUser_ID(),holder.btnFollow);

        if (user.getCurrentUser_ID().equals(firebaseUser.getUid())){
            holder.btnFollow.setVisibility(View.GONE);
        }
        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.btnFollow.getText().toString().equals("Follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(firebaseUser.getUid())
                            .child("Following")
                            .child(user.getCurrentUser_ID())
                            .setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(user.getCurrentUser_ID())
                            .child("Followers")
                            .child(firebaseUser.getUid())
                            .setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(firebaseUser.getUid())
                            .child("Following")
                            .child(user.getCurrentUser_ID())
                            .removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(user.getCurrentUser_ID())
                            .child("Followers")
                            .child(firebaseUser.getUid())
                            .removeValue();
                }
            }
        });
    }

    private void isFollow(final String currentUser_id, final Button btnFollow) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(currentUser_id).exists()) btnFollow.setText("Following");
                else btnFollow.setText("Follow");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mProfileImage;
        public TextView mUsername, mFullName;
        public Button btnFollow;


        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImage = itemView.findViewById(R.id.profileImg_Post);
            mUsername = itemView.findViewById(R.id.post_Username);
            mFullName = itemView.findViewById(R.id.post_FullName);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}
