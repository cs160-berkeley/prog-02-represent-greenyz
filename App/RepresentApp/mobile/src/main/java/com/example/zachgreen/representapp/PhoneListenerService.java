package com.example.zachgreen.representapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * Created by zachgreen on 2/28/16.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String DETAILED = "/detailed";
    private static final String SHAKE = "/shake";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(DETAILED) ) {

            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Context context = getApplicationContext();
            Intent intent = new Intent(this, DetailedViewActivity.class);
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.putExtra("BIO_ID", value);
            startActivity(intent);
            return;

            // so you may notice this crashes the phone because it's
            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
            // replace sending a toast with, like, starting a new activity or something.
            // who said skeleton code is untouchable? #breakCSconceptions
        }
        if( messageEvent.getPath().equalsIgnoreCase(SHAKE) ) {

//            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);


            Intent intent = new Intent(this, CongressionalViewActivity.class);
            intent.putExtra("ZIP_CODE", value);
            try {
                JSONObject jObj = new JSONObject(value);
                intent.putExtra("loc", true);
                intent.putExtra("longitude", jObj.getDouble("longitude"));
                intent.putExtra("latitude", jObj.getDouble("latitude"));
            } catch (Exception e){
            }
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            return;

            // so you may notice this crashes the phone because it's
            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
            // replace sending a toast with, like, starting a new activity or something.
            // who said skeleton code is untouchable? #breakCSconceptions
        }


        else {
            super.onMessageReceived( messageEvent );
        }

    }
}
