package com.example.onlineshop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorActivity extends AppCompatActivity {
    private static final String TAG = "SensorsActivity";
    private static final int LOCATION_REQUEST_CODE = 1;
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            Log.e(TAG, "Failed to retrieve location manager.");
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    SensorActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE
            );
            return;
        }
        if  (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return;
        }

        LocationListener locationListenerGPS=new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                ((TextView)findViewById(R.id.latitude)).setText(String.valueOf(location.getLatitude()));
                ((TextView)findViewById(R.id.longitude)).setText(String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListenerGPS);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        ((TextView)findViewById(R.id.latitude)).setText(String.valueOf(location.getLatitude()));
        ((TextView)findViewById(R.id.longitude)).setText(String.valueOf(location.getLongitude()));

        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        if (sensorManager == null) {
            return;
        }

        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ListView listview = (ListView) findViewById(R.id.sensor_list);
        List<Map<String, String>> list = new ArrayList<>();

        Log.i(TAG, "There are " + sensors.size() + " senzors.");
        for (Sensor sensor : sensors) {
            Log.i(TAG, "Sensor " + sensor.getName());
            Map<String, String> sensor_info = new HashMap<>();

            sensor_info.put("name", sensor.getName());
            sensor_info.put("version", String.valueOf(sensor.getVersion()));
            sensor_info.put("power", String.valueOf(sensor.getPower()));
            sensor_info.put("values", "");

            list.add(sensor_info);
        }
        final ListAdapter adapter = new SimpleAdapter(
                this,
                list,
                R.layout.sensors_info,
                new String[]{"name", "version", "power", "values"},
                new int[]{R.id.name, R.id.version, R.id.power, R.id.values}
        );

        listview.setAdapter(adapter);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE)
        {
            if (grantResults[0] != 0)
            {
                Log.i(TAG, "Granted permission");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }
}
