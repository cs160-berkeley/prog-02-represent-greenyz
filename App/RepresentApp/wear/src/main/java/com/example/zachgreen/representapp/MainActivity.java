package com.example.zachgreen.representapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.GridViewPager;
import android.util.Log;

import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends WearableActivity {

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private GridViewPager pager;
    private RepresentativeGridPagerAdapter adapter;
    private final String[] RANDOM_ZIPS = {"11101", "94704", "33131", "91301"};
    private Random rm = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (GridViewPager) findViewById(R.id.pager);
        Bundle extras = getIntent().getExtras();
        adapter = new RepresentativeGridPagerAdapter(this, getFragmentManager(), extras);
        pager.setAdapter(adapter);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Log.d("T", "shake recognized");
                double rand_val1 = new Random().nextDouble();
                int lat_low = 30, lat_high = 47;
                double rand_lat = lat_low + rand_val1 * (lat_high - lat_low);
                double rand_val2 = new Random().nextDouble();
                int long_low = -120, long_high = -75;
                double rand_long = long_low + rand_val2 * (long_high - long_low);

                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneServiceShake.class);
                JSONObject jObj = new JSONObject();
                try {
                    jObj.put("loc", true);
                    jObj.put("longitude", rand_long);
                    jObj.put("latitude", rand_lat);
                    sendIntent.putExtra("location", jObj.toString());
                } catch (Exception e){

                }

                startService(sendIntent);
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}