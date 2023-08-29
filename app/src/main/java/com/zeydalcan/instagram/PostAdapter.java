package com.zeydalcan.instagram;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;
import com.zeydalcan.instagram.databinding.RecyclerRowBinding;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    ArrayList<Post> postArrayList=new ArrayList<>();
    String TAG="PostAdapterError";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser user=firebaseAuth.getCurrentUser();
    String useremail= user.getEmail();


    public PostAdapter(ArrayList<Post> postArrayList) {
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new PostHolder(binding) ;
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.binding.recyclerviewRowUseremailText.setText(postArrayList.get(position).userEmail);
        holder.binding.recyclerviewRowCommentText.setText(postArrayList.get(position).comment);
        Picasso.get().load(postArrayList.get(position).downloadUrl).into(holder.binding.recyclerviewRowImageview);
        holder.binding.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intentToComment=new Intent(holder.itemView.getContext(), CommentActivity.class);
                    intentToComment.putExtra("postId",postArrayList.get(position).postId);
                    holder.itemView.getContext().startActivity(intentToComment);

                }catch (Exception e){
                    Log.e("Sorun1",e.getLocalizedMessage());
                }
            }
        });
        holder.binding.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked())
                {
                    increaseLikeCount(postArrayList.get(position).postId,postArrayList.get(position).userEmail);

                }
                else
                {

                    DocumentReference postRef = db.collection("Posts").document(postArrayList.get(position).postId);


                    postRef.update("likeCount", FieldValue.increment(-1),"likeState",false)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG,e.getLocalizedMessage());

                                }
                            });
                    postRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null && value.exists())

                        {   boolean liked=value.getBoolean("likeState");
                            Long likecount = value.getLong("likeCount");
                            if (!liked){
                                holder.binding.likeText.setText(likecount+"likes");

                            }

                        }
                    }
                });

                }
                DocumentReference documentReference=db.collection("Posts").document(postArrayList.get(position).postId);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null && value.exists())

                        {   boolean liked=value.getBoolean("likeState");
                            Long likecount = value.getLong("likeCount");
                            if (liked){
                                holder.binding.likeText.setText(likecount+"likes");

                            }

                        }
                    }
                });

            }

        });
        DocumentReference documentReference=db.collection("Posts").document(postArrayList.get(position).postId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists())

                {
                    Long likecount = value.getLong("likeCount");
                    holder.binding.likeText.setText(likecount+"likes");
                }
            }
        });


        holder.binding.likeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postId = postArrayList.get(position).postId;
                Intent intent = new Intent(holder.itemView.getContext(), LikedUserActivity.class);
                intent.putExtra("postId", postId);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    public void increaseLikeCount(String postId,String useremail) {


        DocumentReference postRef = db.collection("Posts").document(postId);


        postRef.update("likeCount", FieldValue.increment(1),"likeState",true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,e.getLocalizedMessage());

                    }
                });
    }


    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder
    {
        private RecyclerRowBinding binding;
        public PostHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
