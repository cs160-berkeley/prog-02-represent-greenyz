package com.example.zachgreen.representapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Button loc_button;
    private Button zip_button;
    private Random rm = new Random();
    private GoogleApiClient mGoogleApiClient;
    private double latitude = 37.8717;
    private double longitude = -122.2728;

    private String zip_code = "94704";
    private String county = "Unknown County";
    private double obama_percentage = 50;
    private double romney_percentage = 50;
    private ArrayList<String> rep_ids = new ArrayList<String>();
    private ArrayList<String> rep_names = new ArrayList<String>();
    private ArrayList<String> rep_parties = new ArrayList<String>();
    private ArrayList<String> rep_emails = new ArrayList<String>();
    private ArrayList<String> rep_websites = new ArrayList<String>();

    private class SunlightTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... url_strs) {
            String result;
            try {
                URL url = new URL(url_strs[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                JSONObject jObject = new JSONObject(result);
                urlConnection.disconnect();
                JSONArray results = jObject.getJSONArray("results");
                for (int i=0; i < results.length(); i++){
                    JSONObject entry = results.getJSONObject(i);
                    rep_ids.add(entry.getString("bioguide_id"));
                    rep_names.add(entry.getString("title") + " " + entry.getString("first_name") + " " + entry.getString("last_name"));
                    rep_parties.add(entry.getString("party"));
                    rep_websites.add(entry.getString("website"));
                }
            } catch (Exception e) {
                Log.d("T", "JSON: " + e.toString());
            }
            return null;
        }

        protected void onPostExecute(Long result) {
        }
    }

    private class CountyTask extends AsyncTask<String, Void, Void> {
        private void doSunlight(){
            String result;
            try {
                URL url = new URL("http://congress.api.sunlightfoundation.com/legislators/locate?zip=" + zip_code + "&apikey=bd670274fc7d4fedb637aed410b5a5a0");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                JSONObject jObject = new JSONObject(result);
                urlConnection.disconnect();
                JSONArray results = jObject.getJSONArray("results");
                for (int i=0; i < results.length(); i++){
                    JSONObject entry = results.getJSONObject(i);
                    rep_ids.add(entry.getString("bioguide_id"));
                    rep_names.add(entry.getString("title") + " " + entry.getString("first_name") + " " + entry.getString("last_name"));
                    rep_parties.add(entry.getString("party"));
                    rep_websites.add(entry.getString("website"));
                }
            } catch (Exception e) {
                Log.d("T", "JSON: " + e.toString());
            }
        }

        private void doCounty(){
            String result;
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + zip_code + "&key=AIzaSyAO5FiWsfu7Wl_-WjcQW8JCYj_2eXaw4Rc");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                JSONObject jObject = new JSONObject(result);
                Log.d("T", "JSON: " + jObject.toString());
                urlConnection.disconnect();
                JSONArray results = jObject.getJSONArray("results");
                JSONObject entry = results.getJSONObject(0);
                JSONArray address_components = entry.getJSONArray("address_components");
                for (int i = 0; i < address_components.length(); i++){
                    JSONObject component = address_components.getJSONObject(i);
                    JSONArray types = component.getJSONArray("types");
                    for (int j = 0; j < types.length(); j++){
                        if (types.getString(j).equals("administrative_area_level_2")){
                            county = component.getString("long_name");
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("T", "JSON: " + e.toString());
            }
        }

        private void doJSONCounty(){
            try {
                String shortened_county = county.replace(" County", "");
                JSONArray jArray = loadJSONFromAsset();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObj = jArray.getJSONObject(i);
                    String entry_county = jObj.getString("county-name");
                    if (entry_county.equals(shortened_county)){
                        obama_percentage = jObj.getInt("obama-percentage");
                        romney_percentage = jObj.getInt("romney-percentage");
                        break;
                    }
                }
            } catch (Exception e){
                Log.d("T", "Exception doJSON: " + e.toString());
            }
        }

        @Override
        protected Void doInBackground(String... url_strs) {
            doSunlight();
            doCounty();
            doJSONCounty();
            Log.d("T", "Ids are: " + rep_ids.toArray(new String[rep_ids.size()]));
            Log.d("T", "Names are: " + rep_names.toArray(new String[rep_names.size()]));
            Log.d("T", "Parties are: " + rep_parties.toArray(new String[rep_parties.size()]));
            Log.d("T", "Emails are: " + rep_emails.toArray(new String[rep_emails.size()]));
            Log.d("T", "Websites are: " + rep_websites.toArray(new String[rep_websites.size()]));

            Intent sendIntent = new Intent(getBaseContext(), CongressionalViewActivity.class);

            sendIntent.putExtra("rep_ids", rep_ids);
            sendIntent.putExtra("rep_names", rep_names);
            sendIntent.putExtra("rep_parties", rep_parties);
            sendIntent.putExtra("rep_emails", rep_emails);
            sendIntent.putExtra("rep_emails", rep_websites);
            startActivity(sendIntent);
            return null;
        }


        protected void onPostExecute(Long result) {
            new SunlightTask().execute("http://congress.api.sunlightfoundation.com/legislators/locate?zip=" + zip_code + "&apikey=bd670274fc7d4fedb637aed410b5a5a0");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
//                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        loc_button = (Button) findViewById(R.id.location_button);
        zip_button = (Button) findViewById(R.id.zip_button);
        zip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText zip_edit = (EditText) findViewById(R.id.zip_input);
                zip_code = zip_edit.getText().toString();
                Intent sendIntent = new Intent(getBaseContext(), CongressionalViewActivity.class);
                sendIntent.putExtra("ZIP_CODE", zip_code);
                startActivity(sendIntent);

            }
        });
        loc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent(getBaseContext(), CongressionalViewActivity.class);
                sendIntent.putExtra("loc", true);
                sendIntent.putExtra("longitude", longitude);
                sendIntent.putExtra("latitude", latitude);
                startActivity(sendIntent);
//                Log.d("T", "in MainActivity: " + zip);
            }
        });
    }

    private JSONArray loadJSONFromAsset() {
        String jsonStr = null;
        try {

            InputStream is = getAssets().open("election-county-2012.json");

            StringBuilder builder =new StringBuilder();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bReader.readLine()) != null) {
                builder.append(line);
            }

            JSONArray jsonObj = new JSONArray(builder.toString());

            return jsonObj;

        } catch (IOException ex) {
            Log.d("T", "IException: " + ex);
            ex.printStackTrace();
            return null;
        } catch (JSONException ex) {
            Log.d("T", "JException: " + ex);
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            Log.d("T", "Latitude is: " + latitude);
            Log.d("T", "Longitude is: " + longitude);
        } else {
            Log.d("T", "Null result");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {
        Log.d("T", "Connection Failed Statement");
    }
}
