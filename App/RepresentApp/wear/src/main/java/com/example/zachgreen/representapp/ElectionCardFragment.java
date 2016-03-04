package com.example.zachgreen.representapp;

import android.app.Fragment;
import android.os.Bundle;
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
        TextView t = (TextView)v.findViewById(R.id.county);
        Bundle args = getArguments();
        if (args != null) {
            String zip_code = args.getString("ZIP_CODE");
            switch(zip_code){
                case "91301":
                    t.setText("LA County");
                    break;
                case "94704":
                    t.setText("Alameda County");
                    break;
                case "11101":
                    t.setText("Queens County");
                    break;
                case "33131":
                    t.setText("Miami-Dade County");
                    break;
                default:
                    t.setText("Default County");
                    break;
            }
        }
        return v;
    }

}
