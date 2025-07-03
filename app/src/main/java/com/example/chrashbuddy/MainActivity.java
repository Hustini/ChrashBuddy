package com.example.chrashbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private SensorEventListener accelerometerEventListener;

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

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager == null) {
            Toast.makeText(this, "Sensor service not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor == null) {
            Toast.makeText(this, "No Gyroscope", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        accelerometerEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float z = event.values[2];

                if (x >= 10.0) {
                    var intent = new Intent(MainActivity.this, AlarmActivity.class);
                    startActivity(intent);
                }
                if (z >= 10.0) {
                    var intent = new Intent(MainActivity.this, AlarmActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

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

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && accelerometerSensor != null) {
            sensorManager.registerListener(accelerometerEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(accelerometerEventListener);
        }
    }
}