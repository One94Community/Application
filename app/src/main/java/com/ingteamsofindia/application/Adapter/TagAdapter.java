package com.ingteamsofindia.application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ingteamsofindia.application.R;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {
    private Context mContext;
    private List<String> mTagsList;
    private List<String> mTagsCount;

    public TagAdapter(Context mContext, List<String> mTagsList, List<String> mTagsCount) {
        this.mContext = mContext;
        this.mTagsList = mTagsList;
        this.mTagsCount = mTagsCount;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, parent, false);
        return new TagAdapter.TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.tag.setText(mTagsList.get(position));
        holder.numberOfPosts.setText(mTagsCount.get(position));

    }

    @Override
    public int getItemCount() {
        return mTagsList.size();
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {

        public TextView tag, numberOfPosts;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.hashTags);
            numberOfPosts = itemView.findViewById(R.id.numberOfComment);
        }
    }
    public void filter(List<String> filterTags, List<String> filterTagsCount){
        this.mTagsList = filterTags;
        this.mTagsCount = filterTagsCount;
        notifyDataSetChanged();
    }
}
