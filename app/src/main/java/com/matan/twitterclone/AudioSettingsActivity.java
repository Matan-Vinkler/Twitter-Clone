package com.matan.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;

public class AudioSettingsActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private SeekBar seekBar;
    private Switch btnSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_settings);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AudioSettingsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);

        seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                btnSwitch.setChecked(progress != 0);
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnSwitch = findViewById(R.id.switch_btn);
        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private int prog = manager.getStreamVolume(AudioManager.STREAM_MUSIC);

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!btnSwitch.isChecked()) {
                    prog = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    manager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
                    seekBar.setProgress(0);
                }
                else {
                    manager.setStreamVolume(AudioManager.STREAM_MUSIC, prog, AudioManager.FLAG_SHOW_UI);
                    seekBar.setProgress(prog);
                }
            }
        });
    }
}