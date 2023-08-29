package com.zeydalcan.instagram;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.zeydalcan.instagram.databinding.CommentRowBinding;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private ArrayList<Comment> comments;

    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentRowBinding binding=CommentRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CommentHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.binding.commentTextView.setText(comments.get(position).comment);
        holder.binding.emailTextView.setText(comments.get(position).username);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    public class CommentHolder extends RecyclerView.ViewHolder
    {
        private CommentRowBinding binding;
        public CommentHolder(CommentRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }


}

