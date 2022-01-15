package com.matan.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Tweet> tweetList = new ArrayList<Tweet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://twitter-clone-614bd-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference tweetRef = database.getReference("Tweets");

        String displayName = PreferenceManager.getDefaultSharedPreferences(this).getString("DisplayName", "");
        String username = PreferenceManager.getDefaultSharedPreferences(this).getString("Username", "");

        TweetAdapter tweetAdapter = new TweetAdapter(HomeActivity.this, tweetList);
        RecyclerView tweetListView = findViewById(R.id.tweet_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        tweetListView.setLayoutManager(layoutManager);
        tweetListView.setAdapter(tweetAdapter);

        //Navigation Menu
        NavigationView navigationView = findViewById(R.id.navigation_view);
        Menu menu = navigationView.getMenu();
        ImageButton btnMenu = findViewById(R.id.btn_menu);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        TextView txtHeaderName = headerLayout.findViewById(R.id.txt_displayname);
        TextView txtHeaderUsername = headerLayout.findViewById(R.id.txt_username);

        txtHeaderName.setText(displayName);
        txtHeaderUsername.setText("@" + username);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        tweetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    tweetList.clear();
                    for (DataSnapshot tweetDataSnapshot : snapshot.getChildren()) {
                        tweetList.add(new Tweet(tweetDataSnapshot.child("Username").getValue().toString(), tweetDataSnapshot.child("DisplayName").getValue().toString(), tweetDataSnapshot.child("Message").getValue().toString(), tweetDataSnapshot.child("Time").getValue().toString()));
                    }
                    tweetAdapter.notifyDataSetChanged();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewTweetActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        int id = item.getItemId();

        if(id == R.id.item_10) {
            FirebaseAuth.getInstance().signOut();

            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("is_user_logged", false).commit();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("DisplayName", "").clear().commit();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("Username", "").clear().commit();

            Intent intent = new Intent(HomeActivity.this, LauncherActivity.class);
            startActivity(intent);

            finish();
        }

        return true;
    }
}