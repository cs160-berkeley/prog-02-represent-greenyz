package com.example.zachgreen.representapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class MainActivity extends Activity {
    private Button loc_button;
    private Button zip_button;
    private final String zip_code = "94704";
    private Random rm = new Random();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loc_button = (Button) findViewById(R.id.location_button);
        zip_button = (Button) findViewById(R.id.zip_button);
        zip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText zip_edit = (EditText) findViewById(R.id.zip_input);
                String zip_text = zip_edit.getText().toString();
                Intent serviceIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                serviceIntent.putExtra("ZIP_CODE", zip_text);
                startService(serviceIntent);
                Intent sendIntent = new Intent(getBaseContext(), CongressionalViewActivity.class);
                sendIntent.putExtra("ZIP_CODE", zip_text);
                startActivity(sendIntent);
            }
        });
        loc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("T", "Zip1 is " + "0");
                String zip = zip_code;
                Log.d("T", "Zip2 is " + zip);
                Intent serviceIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                serviceIntent.putExtra("ZIP_CODE", zip);
                startService(serviceIntent);
                Intent sendIntent = new Intent(getBaseContext(), CongressionalViewActivity.class);
                sendIntent.putExtra("ZIP_CODE", zip);
                startActivity(sendIntent);
//                Log.d("T", "in MainActivity: " + zip);
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
}
