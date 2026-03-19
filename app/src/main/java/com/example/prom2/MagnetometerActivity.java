package com.example.prom2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Source - https://stackoverflow.com/a/52384616
// Posted by Maxouille
// Retrieved 2026-03-19, License - CC BY-SA 4.0

public class MagnetometerActivity extends AppCompatActivity implements SensorEventListener {

    // Sensors & SensorManager
    private Sensor magnetometer;
    private SensorManager mSensorManager;

    // Storage for Sensor readings
    private float[] mGeomagnetic = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get a reference to the magnetometer
        magnetometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Exit unless sensor are available
        if (null == magnetometer)
            finish();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register for sensor updates

        mSensorManager.registerListener(this, magnetometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister all sensors
        mSensorManager.unregisterListener(this);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // Acquire magnetometer event data
        // If we have readings from both sensors then
        // use the readings to compute the device's orientation
        // and then update the display.

        if (mGeomagnetic != null) {
            Log.d("TAG", "mx : "+mGeomagnetic[0]+" my : "+mGeomagnetic[1]+" mz : "+mGeomagnetic[2]);
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            mGeomagnetic = new float[3];
            System.arraycopy(event.values, 0, mGeomagnetic, 0, 3);

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // N/A
    }
}
