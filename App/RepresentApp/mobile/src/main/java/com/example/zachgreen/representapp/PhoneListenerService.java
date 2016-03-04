package com.example.zachgreen.representapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by zachgreen on 2/28/16.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
//    private static final String TOAST = "/send_toast";
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
            intent.putExtra("NAME", value);
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
//
//            Context context = getApplicationContext();
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, "Zip Code: " + value, duration);
//            toast.show();

            Intent intent = new Intent(this, CongressionalViewActivity.class);
            intent.putExtra("ZIP_CODE", value);
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            return;

            // so you may notice this crashes the phone because it's
            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
            // replace sending a toast with, like, starting a new activity or something.
            // who said skeleton code is untouchable? #breakCSconceptions
        }
//        if( messageEvent.getPath().equalsIgnoreCase(TOAST) ) {
//
//            // Value contains the String we sent over in WatchToPhoneService, "good job"
//            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
//
//            // Make a toast with the String
//            Context context = getApplicationContext();
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, value, duration);
//            toast.show();
//
//            // so you may notice this crashes the phone because it's
//            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
//            // replace sending a toast with, like, starting a new activity or something.
//            // who said skeleton code is untouchable? #breakCSconceptions

        else {
            super.onMessageReceived( messageEvent );
        }

    }
}
