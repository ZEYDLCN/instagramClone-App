package com.zeydalcan.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.zeydalcan.instagram.databinding.ActivityCommentBinding;
import com.zeydalcan.instagram.databinding.ActivityUploadActictyBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    private ActivityCommentBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    ArrayList<Comment> commentArrayList;
    CommentAdapter commentAdapter;
    DocumentReference documentReference;

    private String TAG = "CommentActivity";

    String postId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        commentArrayList=new ArrayList<>();
        Intent intent=getIntent();
        postId =intent.getStringExtra("postId");
        Log.e(TAG,"postId: " + postId);

        getComment(postId);
        Log.e("Sorunnn",postId);
        commentAdapter=new CommentAdapter(commentArrayList);
        binding.recyclerView2.setAdapter(commentAdapter);
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(CommentActivity.this));



    }


    public void save(View view)
    {

        String userEmail = user.getEmail();
        String comment = binding.comment.getText().toString();

        HashMap<String, Object> commentData = new HashMap<>();
        commentData.put("useremail",userEmail);
        commentData.put("comment",comment);
        commentData.put("postId",postId);
        commentData.put("date", FieldValue.serverTimestamp());

        db.collection("Comments").add(commentData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Toast.makeText(CommentActivity.this,"Comment saved",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CommentActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }
    public void getComment(String postId)
    {
        CollectionReference collectionReference=FirebaseFirestore.getInstance().collection("Comments");
        collectionReference
                .orderBy("date", Query.Direction.ASCENDING)
                .whereEqualTo("postId", postId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null )
                        {
                            Toast.makeText(CommentActivity.this, error.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                        }
                        if (value != null)
                        {
                            commentArrayList.clear();
                            for (DocumentSnapshot documentSnapshot : value.getDocuments())
                            {
                                Map<String,Object> commentData = documentSnapshot.getData();
                                String comment= (String) commentData.get("comment");
                                String useremail= (String) commentData.get("useremail");
                                String postIdd= (String) commentData.get("postId");
                                Comment commentt=new Comment(useremail,comment,postIdd);
                                commentArrayList.add(commentt);
                                Log.e("Sorunvar",postIdd);
                            }



                        }
                        commentAdapter.notifyDataSetChanged();
                    }
                });


    }


}