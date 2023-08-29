package com.zeydalcan.instagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.zeydalcan.instagram.databinding.ActivityLikedUserBinding;
import com.zeydalcan.instagram.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LikedUserActivity extends AppCompatActivity {
    private ActivityLikedUserBinding binding;
    private LikerAdapter adapter;

    private ArrayList<String> likerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLikedUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        likerList=new ArrayList<>();
        adapter = new LikerAdapter(likerList);
        binding.recyclerView3.setAdapter(adapter);
        String postId = getIntent().getStringExtra("postId");
        DocumentReference postRef = FirebaseFirestore.getInstance().collection("Posts").document(postId);
        postRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (snapshot != null && snapshot.exists()) {
                    likerList.clear();

                       String email= (String) snapshot.get("useremail");
                        likerList.add(email);


                        adapter.notifyDataSetChanged();

                }
            }
        });
    }
}