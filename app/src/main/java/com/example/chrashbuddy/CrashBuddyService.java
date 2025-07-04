package com.example.chrashbuddy;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class CrashBuddyService {
    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private SensorEventListener sensorEventListener;
    private final Context context;

    public CrashBuddyService(Context context) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = (sensorManager != null) ? sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) : null;
    }

    public boolean isSensorAvailable() {
        return accelerometer != null;
    }

    public void startDetection() {
        if (accelerometer == null) return;

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                if (Math.abs(x) > 20 || Math.abs(y) > 20 || Math.abs(z) > 20) {
                    Log.d("CrashDetection", "Potential accident detected!");
                    stopDetection();
                    Intent intent = new Intent(context, AlarmActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopDetection() {
        if (sensorEventListener != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }
}