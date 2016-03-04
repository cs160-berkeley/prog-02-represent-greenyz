package com.example.zachgreen.representapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by zachgreen on 2/27/16.
 */
public class DetailedViewActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deatailed_view);

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        TextView nameTxt = (TextView)findViewById(R.id.Name);
        nameTxt.setText(name);

        String party = "Democrat";
        TextView partyTxt = (TextView)findViewById(R.id.Party);
        partyTxt.setText(party);

        String endDate = "Term End Date: January 8, 2017";
        TextView dateTxt = (TextView)findViewById(R.id.EndDate);
        dateTxt.setText(endDate);

        String[] committees =
                {
                        "Senate Select Committee on Ethics",
                        "Senate Committee on Environment and Public Works",
                        "Senate Committee on Foreign Relations"
                };
        String comms_str = "Committees:\n";
        for(int i = 0; i < committees.length; i++) {
            comms_str += "-" + committees[i] + "\n";
        }
        TextView commTxt = (TextView)findViewById(R.id.Committees);
        commTxt.setText(comms_str);

        String[] bills =
                {
                        "02/25/2016: Bill S.2588 - A bill to provide grants to eligible entities to reduce lead in drinking water.",
                        "02/24/2016: Bill S.2579 - A bill to provide additional support to ensure safe drinking water.",
                        "11/10/2015: Bill S.2276 - SAFE PIPES Act"
                };
        String bills_str = "Bills Endoresed:\n";
        for(int i = 0; i < bills.length; i++) {
            bills_str += "-" + bills[i] + "\n";
        }
        TextView billTxt = (TextView)findViewById(R.id.Bills);
        billTxt.setText(bills_str);


    }


}
