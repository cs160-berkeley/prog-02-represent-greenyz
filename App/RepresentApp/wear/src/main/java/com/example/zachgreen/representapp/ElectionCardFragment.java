package com.example.zachgreen.representapp;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zachgreen on 3/2/16.
 */
public class ElectionCardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.election_view, container, false);
        try {
            Bundle args = getArguments();

            TextView county_t = (TextView) v.findViewById(R.id.county);
            county_t.setText(args.getString("county"));

            TextView obama_t = (TextView) v.findViewById(R.id.obama_num);
            obama_t.setText(args.getDouble("obama_percentage") + "%");

            TextView romney_t = (TextView) v.findViewById(R.id.romney_num);
            romney_t.setText(args.getDouble("romney_percentage") + "%");
        } catch (Exception e){
            Log.d("T", e.toString());
        }

        return v;
    }

}
