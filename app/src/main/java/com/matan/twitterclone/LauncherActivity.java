package com.matan.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;

                if(PreferenceManager.getDefaultSharedPreferences(LauncherActivity.this).getBoolean("is_user_logged", false)) {
                    intent = new Intent(LauncherActivity.this, HomeActivity.class);
                }
                else {
                    intent = new Intent(LauncherActivity.this, MainActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, 1200);
    }
}