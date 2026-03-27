package com.example.prom2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor gyro;
    private TextView textView;

    // Buffer variables for high-frequency sampling
    private static final int BATCH_SIZE = 100;
    private int sampleCount = 0;
    private StringBuilder dataBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (mSensorManager != null) {
            gyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }

        if (gyro == null) {
            finish();
        }

        dataBuffer = new StringBuilder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gyro != null) {
            mSensorManager.registerListener(this, gyro, 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // hardware timestamp of the event (in nanoseconds)
            long timestamp = event.timestamp;

            // format: timestamp,x,y,z
            dataBuffer.append(timestamp).append(",")
                    .append(x).append(",")
                    .append(y).append(",")
                    .append(z).append("\n");

            sampleCount++;

            if (sampleCount >= BATCH_SIZE) {

                Log.d("GyroDataLog", "\n" + dataBuffer.toString());
                // Log.d("GyroDataLog", "\n10000 values!");

                dataBuffer.setLength(0);
                sampleCount = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}