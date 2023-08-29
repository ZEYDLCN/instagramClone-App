package com.zeydalcan.instagram;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeydalcan.instagram.databinding.ItemLikedUserBinding;

import java.util.ArrayList;

public class LikerAdapter extends RecyclerView.Adapter<LikerAdapter.LikerHolder>{
    ArrayList<String> likerList;

    public LikerAdapter(ArrayList<String> likerList) {
        this.likerList = likerList;
    }

    @NonNull
    @Override
    public LikerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLikedUserBinding itemLikedUserBinding=ItemLikedUserBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new LikerHolder(itemLikedUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LikerHolder holder, int position) {
       holder.binding.emailTextView.setText(likerList.get(position));
    }

    @Override
    public int getItemCount() {
        return likerList.size();
    }

    public class LikerHolder extends RecyclerView.ViewHolder{
        private ItemLikedUserBinding binding;
        public LikerHolder(ItemLikedUserBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
    }
}
