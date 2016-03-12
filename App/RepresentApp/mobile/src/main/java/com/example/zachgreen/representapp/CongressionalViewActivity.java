package com.example.zachgreen.representapp;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

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
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by zachgreen on 2/25/16.
 */
public class CongressionalViewActivity extends ListActivity {

        private boolean loc = false;
        private String zip_code = "94704";
        private double latitude = 37.8717;
        private double longitude = -122.2728;
        private String county = "Unknown County";
        private double obama_percentage = 50;
        private double romney_percentage = 50;
        private ArrayList<String> rep_ids = new ArrayList<String>();
        private ArrayList<String> rep_names = new ArrayList<String>();
        private ArrayList<String> rep_parties = new ArrayList<String>();
        private ArrayList<String> rep_emails = new ArrayList<String>();
        private ArrayList<String> rep_websites = new ArrayList<String>();
        private ArrayList<String> rep_twitter_ids = new ArrayList<String>();
        private ArrayList<String> rep_tweets = new ArrayList<String>();
        private CongressionalViewActivity that = this;
        private HashMap<String,String> id_to_tweet =  new HashMap<String,String>();
        private HashMap<String,String> id_to_img_url=  new HashMap<String,String>();
        private HashMap<String,Bitmap> rep_id_to_image = new HashMap<String,Bitmap>();
        private Bitmap[] imgs;

        private int count = 0;


        ListView list;

        private Integer[] imgid={
                R.drawable.bboxer,
                R.drawable.bboxer,
                R.drawable.bboxer
        };

        private String[] names ={
                "Sen. Barbara Boxer",
                "Sen. Barbara Boxer",
                "Sen. Barbara Boxer"
        };

        private String[] parties ={
                "Democrat",
                "Democrat",
                "Democrat"
        };

        private String[] emails ={
                "senator@boxer.senate.gov",
                "senator@boxer.senate.gov",
                "senator@boxer.senate.gov"
        };

        private String[] websites
                ={
                "www.boxer.senate.gov",
                "www.boxer.senate.gov",
                "www.boxer.senate.gov"
        };

        private String[] tweets ={
                "Boxer Calls for Immediate Action on @SenStabenow Bill to Protect American Communities from #Lead in #DrinkingWater",
                "Boxer Calls for Immediate Action on @SenStabenow Bill to Protect American Communities from #Lead in #DrinkingWater",
                "Boxer Calls for Immediate Action on @SenStabenow Bill to Protect American Communities from #Lead in #DrinkingWater"
        };


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.congressional_view);
                try {
                    Intent intent = getIntent();
                    Bundle bundle = intent.getExtras();
                    if (bundle.containsKey("loc")) {
                        loc = true;
                        latitude = bundle.getDouble("latitude");
                        longitude = bundle.getDouble("longitude");

                    } else {
                        loc = false;
                        zip_code = bundle.getString("ZIP_CODE");
                    }
                } catch (Exception e){
                }
                new CountyTask().execute(zip_code);

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

        private class CountyTask extends AsyncTask<String, Void, Long> {
                private void doSunlight(){
                        String result;
                        try {
                                URL url = null;
                                if (loc){
                                    url = new URL("http://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + latitude + "&longitude=" + longitude + "&apikey=bd670274fc7d4fedb637aed410b5a5a0");
                                } else {
                                    url = new URL("http://congress.api.sunlightfoundation.com/legislators/locate?zip=" + zip_code + "&apikey=bd670274fc7d4fedb637aed410b5a5a0");
                                }
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
                                        rep_emails.add(entry.getString("oc_email"));
                                        String party = entry.getString("party");
                                        switch(party){
                                            case "D":
                                                rep_parties.add("Democrat");
                                                break;
                                            case "R":
                                                rep_parties.add("Republican");
                                                break;
                                            case "I":
                                                rep_parties.add("Independent");
                                                break;
                                            default:
                                                rep_parties.add(party);
                                                break;
                                        }
                                        rep_websites.add(entry.getString("website"));
                                        rep_twitter_ids.add(entry.getString("twitter_id"));
                                }
                        } catch (Exception e) {
                                Log.d("T", "JSON: " + e.toString());
                        }
                }

                private void doCounty(){
                        String result;
                        try {
                                URL url = null;
                                if (loc){
                                    url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=AIzaSyAO5FiWsfu7Wl_-WjcQW8JCYj_2eXaw4Rc");
                                } else{
                                    url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + zip_code + "&key=AIzaSyAO5FiWsfu7Wl_-WjcQW8JCYj_2eXaw4Rc");
                                }
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
                                Log.d("T", "Exception doCounty: " + e.toString());
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

                private void doTwitter(){
                        TwitterAuthConfig authConfig = new TwitterAuthConfig("XuQFKO5qzzQwqVDSEUA35bRvT", "a9ybVzgZxtcQrA1ZUohqoG8p72FL8Wgl1RQguGAeXKx7NOMn7v");
                    Fabric.with(that, new Twitter(authConfig));
//                        Log.d("T", "twitter id 0 is: " + rep_twitter_ids.get(0));
                        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
                                @Override
                                public void success(Result<AppSession> appSessionResult) {
                                        AppSession session = appSessionResult.data;
                                        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                                        for (String twitter_id: rep_twitter_ids) {
                                                twitterApiClient.getStatusesService().userTimeline(null, twitter_id, 1, null, null, false, false, false, true, new Callback<List<Tweet>>() {
                                                        @Override
                                                        public void success(Result<List<Tweet>> listResult) {
                                                                for (Tweet tweet : listResult.data) {
                                                                        Log.d("fabricstuff", "result: " + tweet.text + "  " + tweet.createdAt);
                                                                        id_to_tweet.put(tweet.user.screenName, tweet.text);
                                                                        id_to_img_url.put(tweet.user.screenName, tweet.user.profileImageUrl.replace("_normal.", "."));
                                                                }
                                                                count++;

                                                        }

                                                        @Override
                                                        public void failure(TwitterException e) {
                                                                e.printStackTrace();
                                                                count++;
                                                        }
                                                });
                                        }
                                }
                                @Override
                                public void failure(TwitterException e) {
                                        e.printStackTrace();
                                }
                        });
                }

                private void doImage(){

                        for (String twitter_id: id_to_img_url.keySet()){
                                String url = id_to_img_url.get(twitter_id);
                                String urldisplay = url;
                                Log.d("T", "Profile url is: " + url);
                                Bitmap mIcon11 = null;
                                try {
                                        InputStream in = new java.net.URL(urldisplay).openStream();
                                        mIcon11 = BitmapFactory.decodeStream(in);
                                        rep_id_to_image.put(twitter_id, mIcon11);

                                } catch (Exception e) {
                                        Log.e("Error", e.getMessage());
                                        e.printStackTrace();
                                }
                        }
                }


                @Override
                protected Long doInBackground(String... url_strs) {
                    doSunlight();
                    doCounty();
                    doJSONCounty();
                    doTwitter();
                    while (count != rep_twitter_ids.size());
                    doImage();
                    Log.d("T", "Rep ids size: " + rep_ids.size());
                    Log.d("T", "Ids are: " + rep_ids.toArray(new String[rep_ids.size()]));
                    Log.d("T", "Names are: " + rep_names.toArray(new String[rep_names.size()]));
                    Log.d("T", "Parties are: " + rep_parties.toArray(new String[rep_parties.size()]));
                    Log.d("T", "Emails are: " + rep_emails.toArray(new String[rep_emails.size()]));
                    Log.d("T", "Websites are: " + rep_websites.toArray(new String[rep_websites.size()]));
                    JSONObject watchJson = new JSONObject();
                    try {
                        watchJson.put("county", county);
                        watchJson.put("obama_percentage", obama_percentage);
                        watchJson.put("romney_percentage", romney_percentage);
                        JSONArray reps_arr = new JSONArray();
                        for (int i = 0; i < rep_names.size(); i++){
                            JSONObject rep_entry = new JSONObject();
                            rep_entry.put("id", rep_ids.get(i));
                            rep_entry.put("name", rep_names.get(i));
                            rep_entry.put("party",rep_parties.get(i));
                            reps_arr.put(rep_entry);
                        }
                        watchJson.put("reps", reps_arr);
                    } catch (JSONException e){
                        Log.d("T", "JSON Creation Error: " + e.toString());
                    }
                    Intent serviceIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                    serviceIntent.putExtra("data", watchJson.toString());
                    startService(serviceIntent);
                        return null;
                }

                @Override
                protected void onPostExecute(Long result) {
                        names = rep_names.toArray(new String[rep_names.size()]);
                        parties = rep_parties.toArray(new String[rep_parties.size()]);
                        emails = rep_emails.toArray(new String[rep_emails.size()]);
                        websites = rep_websites.toArray(new String[rep_websites.size()]);
                        tweets = new String[rep_twitter_ids.size()];
                        imgs = new Bitmap[rep_twitter_ids.size()];
                        for (int i = 0; i < tweets.length; i++){
                                tweets[i] = id_to_tweet.get(rep_twitter_ids.get(i));
                                imgs[i] = rep_id_to_image.get(rep_twitter_ids.get(i));
                        }


                        CustomListAdapter adapter=new CustomListAdapter(that, imgs, names, parties, emails, websites, tweets);
                        list=(ListView)findViewById(android.R.id.list);
                        list.setAdapter(adapter);

                        list.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                        String rep_id = rep_ids.get(+position);
                                        Intent sendIntent = new Intent(getBaseContext(), DetailedViewActivity.class);
                                        sendIntent.putExtra("BIO_ID", rep_id);
                                        startActivity(sendIntent);
                                }
                        });
                }
        }
}

