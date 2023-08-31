package com.zeydalcan.instagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.zeydalcan.instagram.databinding.ActivityFeedBinding;
import com.zeydalcan.instagram.databinding.ActivityUploadActictyBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    ArrayList<Post> postArrayList;

    private ActivityFeedBinding binding;
    PostAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        postArrayList=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance(); // firebaseFirestore değişkenini başlatma
        getData();
        adapter=new PostAdapter(postArrayList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(FeedActivity.this));
        binding.recyclerView.setAdapter(adapter);
        binding.signout.setBackgroundColor(Color.BLUE);
        Button button=binding.signout;
        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_logout_24,0,0,0);
    }

    private void getData() {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Posts");
        collectionReference.orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(FeedActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }

                if (value != null) {
                    postArrayList.clear();
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Map<String, Object> getData = documentSnapshot.getData();
                        String userEmail = (String) getData.get("useremail");
                        String downloadUrl = (String) getData.get("downloadurl");
                          String comment = (String) getData.get("comment");
                        String postId = documentSnapshot.getId();
                        System.out.println(comment);
                        // Verileri kullanma veya işleme yapma
                        Post post=new Post(downloadUrl,userEmail,comment,postId);
                        postArrayList.add(post);



                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void upload(View view)
    {
        Intent intentToUpload=new Intent(FeedActivity.this,UploadActivity.class);
        startActivity(intentToUpload);

    }
    public void signout(View view)
    {
        auth.signOut();
        Intent intentToMain=new Intent(FeedActivity.this,MainActivity.class);
        startActivity(intentToMain);
        finish();
    }
}