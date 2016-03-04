package com.example.zachgreen.representapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.GridViewPager;
import android.util.Log;

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
//        String zipCode = "94704";
//        try {
//            Intent intent = getIntent();
//            Bundle extras = intent.getExtras();
//            zipCode = extras.getString("ZIP_CODE");
//        } catch (Exception e){
//        }
//        updateVoteFragment(zipCode);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Log.d("T", "shake recognized");
                String zip = RANDOM_ZIPS[rm.nextInt(RANDOM_ZIPS.length)];
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneServiceShake.class);
                sendIntent.putExtra("ZIP_CODE", zip);
                startService(sendIntent);
                Intent activityIntent = new Intent(getBaseContext(), MainActivity.class);
                activityIntent.putExtra("ZIP_CODE", zip);
                startActivity(activityIntent);
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