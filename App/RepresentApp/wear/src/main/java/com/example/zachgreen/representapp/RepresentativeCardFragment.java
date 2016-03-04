package com.example.zachgreen.representapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by zachgreen on 3/1/16.
 */
public class RepresentativeCardFragment extends Fragment {
//        implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.representative_view, container, false);
        LinearLayout ll = (LinearLayout)v.findViewById(R.id.rep_container);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchToPhoneServiceTap.class);
                intent.putExtra("DEATILED", "Barbara_Boxer");
                getActivity().startService(intent);
            }
        });
//        String name = savedInstanceState.getString("name");
//        String party = savedInstanceState.getString("party");
//        int pic_id = savedInstanceState.getInt("pic_id");
//        TextView nameView = (TextView) v.findViewById(R.id.rep_name);
//        nameView.setText(name);
//        TextView partyView = (TextView) v.findViewById(R.id.rep_party);
//        partyView.setText(party);
//        ImageView imageView = (ImageView) v.findViewById(R.id.rep_pic);
//        imageView.setImageResource(pic_id);
          return v;
        }

}
