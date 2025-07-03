package com.example.chrashbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        var triggerAlarmButton = (TextView)findViewById(R.id.triggerAlarmButton);
        triggerAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var intent = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });

        var detectionStatusSymbol = (ImageView)findViewById(R.id.detectionStatusSymbol);
        final int[] currentColor = {Color.RED};
        var activationToggleButton = (TextView)findViewById(R.id.activationToggleButton);
        activationToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable drawable = (GradientDrawable) detectionStatusSymbol.getDrawable();
                if (currentColor[0] == Color.RED) {
                    drawable.setColor(Color.GREEN);
                    currentColor[0] = Color.GREEN;
                    activationToggleButton.setText("Deactivate detection");
                } else {
                    drawable.setColor(Color.RED);
                    currentColor[0] = Color.RED;
                    activationToggleButton.setText("Activate Detection");
                }
            }
        });
    }
}