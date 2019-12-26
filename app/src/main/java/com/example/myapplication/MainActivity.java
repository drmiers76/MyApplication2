package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView txtV;
    private SensorManager sensorMgr;
    private Sensor sense;
    private long startingEventTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtV = (TextView)findViewById(R.id.mainText);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorMgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sense     = sensorMgr.getDefaultSensor(Sensor.TYPE_PRESSURE);
//        sense     = sensorMgr.getDefaultSensor(Sensor.TYPE_HEART_BEAT); //Sensor.TYPE_LIGHT);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
//                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
//            txtV.setText("Permission not granted");
//        }

//        startingEventTime = 0;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtV.setText("Changed Text");
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
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
            txtV.setText("Clicked Menu Item");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public final void onAccuracyChanged(Sensor dSensor, int accuracy ){

    }

    @Override
    public final void onSensorChanged(SensorEvent event){

        if( event.sensor.getType() == Sensor.TYPE_PRESSURE ) {
            float milliBars = event.values[0];
            milliBars = milliBars / 33.863886666667f;   //33.863886666667
            //txtV.setText(Float.toString(milliBars));
            String str = String.format(Locale.getDefault(), "%7.3f", milliBars);
            //str += "  " + event.timestamp;
            if (startingEventTime == 0)
                startingEventTime = event.timestamp;
            long tStamp = (event.timestamp - startingEventTime) / 1000000;

            str += " : " + tStamp;
            txtV.setText(str);
        } else if( event.sensor.getType() == Sensor.TYPE_LIGHT ) {
            float lux = event.values[0];
            String str = String.format(Locale.getDefault(), "%10.4f", lux );

            if (startingEventTime == 0)
                startingEventTime = event.timestamp;
            long tStamp = (event.timestamp - startingEventTime) / 1000000;

            str += " : " + tStamp;

            txtV.setText( str );
        } else if( event.sensor.getType() == Sensor.TYPE_HEART_BEAT ){
            float hbRate = event.values[0];
            String str = String.format("%.2f", hbRate);
            txtV.setText( str );
        }
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorMgr.registerListener(this, sense, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorMgr.unregisterListener(this);
    }

}
