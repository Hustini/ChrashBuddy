package com.example.chrashbuddy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class AlarmActivity extends AppCompatActivity {
    int randomNum = 100 + (int)(Math.random() * 900);
    MediaPlayer mediaPlayer;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alarm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        var deactivationCodeLabel = (TextView)findViewById(R.id.deactivationCodeLabel);
        deactivationCodeLabel.setText("Enter " + String.valueOf(randomNum) + " to deactivate");

        var deactivateButton = (TextView)findViewById(R.id.deactivateButton);
        deactivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var deactivationCodeInputField = (EditText)findViewById(R.id.deactivationCodeInput);
                var deactivationCodeInputStr = deactivationCodeInputField.getText().toString();
                var deactivationCodeInputInt = Integer.parseInt(deactivationCodeInputStr);
                if (deactivationCodeInputInt == randomNum) {
                    var intent = new Intent(AlarmActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(AlarmActivity.this, "Deactivate alarm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void stopAlarmSound() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
            } catch (Exception e) {
                Log.e("AlarmActivity", "Error stopping media player", e);
            } finally {
                mediaPlayer = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopAlarmSound();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAlarmSound();
    }
}