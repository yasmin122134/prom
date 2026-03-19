/*
package com.example.prom2;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.prom2.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
*/

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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


// Source - https://stackoverflow.com/a/52384616
// Posted by Maxouille
// Retrieved 2026-03-19, License - CC BY-SA 4.0

public class MainActivity extends AppCompatActivity implements SensorEventListener {

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
            String message = "mx : "+mGeomagnetic[0]+" my : "+mGeomagnetic[1]+" mz : "+mGeomagnetic[2];
            Log.d("TAG", message);

            // Inflate the custom Toast layout
            LayoutInflater inflater = LayoutInflater.from(context);
            View toastView = inflater.inflate(R.layout.custom_toast, null);
            TextView toastMessage = toastView.findViewById(R.id.toast_message);
            toastMessage.setText(message);

            // Create and configure the Toast
            Toast toast = new Toast(context);
            toast.setView(toastView);
            toast.setDuration(duration);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, toastOffsetY);

            // Show the Toast
            toast.show();
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            mGeomagnetic = new float[3];
            System.arraycopy(event.values, 0, mGeomagnetic, 0, 3);

        }


    }

    // Source - https://stackoverflow.com/a/4873320
// Posted by Frank C., modified by community. See post 'Timeline' for change history
// Retrieved 2026-03-19, License - CC BY-SA 3.0

    public void updateTextView(String toThis) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(toThis);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // N/A
    }
}
