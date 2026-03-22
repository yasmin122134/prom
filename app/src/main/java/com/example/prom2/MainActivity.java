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

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Sensors & SensorManager
    private Sensor gyro;
    private SensorManager mSensorManager;

    // Storage for Sensor readings
    private double[] gyroReadings = null;
    private List<Double> gyroHistX = new ArrayList<Double>();
    private TextView textView;
    private Button button;
    private int sensType = Sensor.TYPE_GYROSCOPE;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get a reference to the magnetometer
        gyro = mSensorManager
                .getDefaultSensor(sensType);

        // Exit unless sensor are available
        if (null == gyro)
            finish();

        textView = (TextView)findViewById(R.id.textview);
        button = (Button)findViewById(R.id.button_first);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawLineChart();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register for sensor updates

        mSensorManager.registerListener(this, gyro,
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
        if (event.sensor.getType() == sensType) {
            gyroReadings = new double[3];
            System.arraycopy(event.values, 0, gyroReadings, 0, 3);
            gyroHistX.add(gyroReadings[0]);
        }
        if (gyroReadings != null) {
            String message = "mx : "+ gyroReadings[0]+"\nmy : "+ gyroReadings[1]+"\nmz : "+ gyroReadings[2];
            Log.d("TAG", message);
            textView.setText(message);
            gyroHistX.add(gyroReadings[0]);
        }

    }


    public void calcFFT(){
        double[] img = new double[gyroHistX.size()];
        double[] real = new double[gyroHistX.size()];
        for (int i = 0; i < gyroHistX.size(); i++) {
            real[i] = gyroHistX.get(i);
        }
        double[] res = FFTbase.fft(real, img, true);
//        Log.d()

    }


    private void drawLineChart() {
        LineChart lineChart = findViewById(R.id.lineChart);
        List<Entry> lineEntries = getDataSet();
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Work");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setCircleColor(Color.YELLOW);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setValueTextColor(Color.DKGRAY);
        lineDataSet.setMode(LineDataSet.Mode.STEPPED);

        LineData lineData = new LineData(lineDataSet);
        lineChart.getDescription().setTextSize(12);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawMarkers(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.animateY(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.setData(lineData);

        ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Rest");
        xAxisLabel.add("Work");
        xAxisLabel.add("2-up");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMaximum(3);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel) {
            @Override
            public String[] getValues() {
                return super.getValues();
            }
        });

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(24);

        lineChart.getAxisRight().setEnabled(false);

        lineChart.invalidate();

    }

    private List<Entry> getDataSet() {
        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < gyroHistX.size(); i++) {
            double d = gyroHistX.get(i);

            lineEntries.add(new Entry(i, (float) d));
        }
        return lineEntries;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // N/A
    }
}
