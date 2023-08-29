package com.zeydalcan.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zeydalcan.instagram.databinding.ActivityMainBinding;
import com.zeydalcan.instagram.databinding.ActivityUploadActictyBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        if (user != null){
            Intent intentToFeed=new Intent(MainActivity.this,FeedActivity.class);
            startActivity(intentToFeed);
            finish();
        }
    }
    public void signUp(View view){
        String username=binding.usernameText.getText().toString();
        String password=binding.passwordText.getText().toString();
        if (username.equals("")||password.equals("")){
            Toast.makeText(MainActivity.this,"You must enter password and username",Toast.LENGTH_LONG).show();
        }else {
            auth.createUserWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intentToFeed=new Intent(MainActivity.this,FeedActivity.class);
                    startActivity(intentToFeed);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                }
            });
        }
    }
    public void signIn(View view){
        String username=binding.usernameText.getText().toString();
        String password=binding.passwordText.getText().toString();

        if (username.equals("")|| password.equals("")){
                Toast.makeText(MainActivity.this,"Enter username and password.",Toast.LENGTH_LONG);
        }else{

        auth.signInWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intentToFeed=new Intent(MainActivity.this,FeedActivity.class);
                startActivity(intentToFeed);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        }

    }

}