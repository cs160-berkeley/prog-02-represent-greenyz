package com.example.zachgreen.representapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zachgreen on 3/1/16.
 */
public class RepresentativeCardFragment extends Fragment {
//        implements View.OnClickListener {
    private String rep_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.representative_view, container, false);
        try {
            Bundle args = getArguments();

            TextView rep_name_t = (TextView) v.findViewById(R.id.rep_name);
            rep_name_t.setText(args.getString("name"));

            TextView party_t = (TextView) v.findViewById(R.id.rep_party);
            String party = args.getString("party");
            party_t.setText(party);

            rep_id = args.getString("id");
        } catch (Exception e) {
            Log.d("T", "Representative Exception: " + e.toString());
        }

        LinearLayout ll = (LinearLayout)v.findViewById(R.id.rep_container);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchToPhoneServiceTap.class);
                intent.putExtra("id", rep_id);
                getActivity().startService(intent);
            }
        });

          return v;
        }

}
