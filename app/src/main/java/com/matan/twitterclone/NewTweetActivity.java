package com.matan.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewTweetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tweet);

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://twitter-clone-614bd-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference tweetRef = database.getReference("Tweets");

        String displayName = PreferenceManager.getDefaultSharedPreferences(this).getString("DisplayName", "");
        String username = PreferenceManager.getDefaultSharedPreferences(this).getString("Username", "");

        EditText edtContent = findViewById(R.id.edt_content);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTweetActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnTweet = findViewById(R.id.btn_tweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtContent.getText().toString().trim().equals(""))
                {
                    Toast.makeText(NewTweetActivity.this, "Content can't be empty!", Toast.LENGTH_LONG).show();
                    return;
                }

                btnTweet.setEnabled(false);

                String key = tweetRef.push().getKey();
                tweetRef.child(key).child("Message").setValue(edtContent.getText().toString());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm DD/MM/YYYY", Locale.getDefault());
                String currentDnt = simpleDateFormat.format(new Date());

                tweetRef.child(key).child("Time").setValue(currentDnt).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onBackPressed();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(NewTweetActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });

                tweetRef.child(key).child("DisplayName").setValue(displayName);
                tweetRef.child(key).child("Username").setValue(username);

                Intent intent = new Intent(NewTweetActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}