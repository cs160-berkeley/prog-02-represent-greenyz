package com.example.zachgreen.representapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

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
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by zachgreen on 2/27/16.
 */
public class DetailedViewActivity extends Activity {
    private String rep_id;
    private String name;
    private String party;
    private String end_date;
    private String twitter_id;
    private Bitmap img;
    private ArrayList<String> bills = new ArrayList<String>();
    private ArrayList<String> committees = new ArrayList<String>();
    private DetailedViewActivity that = this;
    private int count = 0;
    private String twitter_img_url = "";
    private int num_bills = 3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deatailed_view);

        Intent intent = getIntent();
        rep_id = intent.getStringExtra("BIO_ID");
        new CountyTask().execute(rep_id);



    }

    private class CountyTask extends AsyncTask<String, Void, Long> {
        private void doSunlight(){
            String result;
            try {
                URL url = new URL("http://congress.api.sunlightfoundation.com/legislators?bioguide_id=" + rep_id + "&apikey=bd670274fc7d4fedb637aed410b5a5a0");
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
                    name = entry.getString("title") + " " + entry.getString("first_name") + " " + entry.getString("last_name");
                    party = entry.getString("party");
                    switch(party){
                        case "D":
                            party = "Democrat";
                            break;
                        case "R":
                            party = "Republican";
                            break;
                        case "I":
                            party = "Independent";
                            break;
                        default:
                            break;
                    }
                    end_date = entry.getString("term_end");
                    twitter_id = entry.getString("twitter_id");
                    Log.d("T", "name: " + name);
                    Log.d("T", "party: " + party);
                    Log.d("T", "end_date: " + end_date);
                    Log.d("T", "twitter_id: " + twitter_id);
                }
            } catch (Exception e) {
                Log.d("T", "JSON: " + e.toString());
            }
        }

        private void doTwitter(){
            TwitterAuthConfig authConfig = new TwitterAuthConfig("XuQFKO5qzzQwqVDSEUA35bRvT", "a9ybVzgZxtcQrA1ZUohqoG8p72FL8Wgl1RQguGAeXKx7NOMn7v");
            Fabric.with(that, new Twitter(authConfig));
            TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
                @Override
                public void success(Result<AppSession> appSessionResult) {
                    AppSession session = appSessionResult.data;
                    TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                    twitterApiClient.getStatusesService().userTimeline(null, twitter_id, 1, null, null, false, false, false, true, new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> listResult) {
                            for (Tweet tweet : listResult.data) {
                                Log.d("fabricstuff", "result: " + tweet.text + "  " + tweet.createdAt);
                                twitter_img_url = tweet.user.profileImageUrl.replace("_normal.", ".");
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

                @Override
                public void failure(TwitterException e) {
                    e.printStackTrace();
                }
            });
        }

        private void doBills(){
            String result;
            try {
                URL url = new URL("http://congress.api.sunlightfoundation.com/bills?sponsor_id=" + rep_id + "&apikey=bd670274fc7d4fedb637aed410b5a5a0");
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
                for (int i=0; i < num_bills && i < results.length(); i++){
                    JSONObject entry = results.getJSONObject(i);
                    String date = entry.getString("introduced_on");
                    String title = entry.getString("official_title");
                    String bill = date + ": " + title;
                    bills.add(bill);
                }
            } catch (Exception e) {
                Log.d("T", "JSON: " + e.toString());
            }
        }

        private void doImage(){
            String urldisplay = twitter_img_url;
            Log.d("T", "URL is " + urldisplay);
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                img = mIcon11;

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }

        private void doCommittees(){
            String result;
            try {
                URL url = new URL("http://congress.api.sunlightfoundation.com/committees?member_ids=" + rep_id + "&apikey=bd670274fc7d4fedb637aed410b5a5a0");
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
                    committees.add(entry.getString("name"));
                }
            } catch (Exception e) {
                Log.d("T", "JSON: " + e.toString());
            }
        }


        @Override
        protected Long doInBackground(String... url_strs) {
            doSunlight();
            doTwitter();
            doBills();
            doCommittees();
            while (count != 1);
            doImage();
            return null;
        }

        @Override
        protected void onPostExecute(Long result) {
            TextView nameTxt = (TextView)findViewById(R.id.Name);
            nameTxt.setText(name);

            TextView partyTxt = (TextView)findViewById(R.id.Party);
            partyTxt.setText(party);

            TextView dateTxt = (TextView)findViewById(R.id.EndDate);
            dateTxt.setText("Term Ends " + end_date);

            String comms_str = "";
            for(int i = 0; i < committees.size(); i++) {
                comms_str += "•" + committees.get(i) + "\n";
            }
            comms_str = comms_str.substring(0, comms_str.length()-1);
            TextView commTxt = (TextView)findViewById(R.id.Committees);
            commTxt.setText(comms_str);

            String bills_str = "";
            for(int i = 0; i < bills.size(); i++) {
                bills_str += "•" + bills.get(i) + "\n";
            }
            bills_str = bills_str.substring(0, bills_str.length()-1);
            TextView billTxt = (TextView)findViewById(R.id.Bills);
            billTxt.setText(bills_str);

            ImageView imgView = (ImageView)findViewById(R.id.rep_img);
            imgView.setImageBitmap(img);
            ScrollView main_view = (ScrollView)findViewById(R.id.scroll_view);

            main_view.setVisibility(View.VISIBLE);

        }
    }


}
