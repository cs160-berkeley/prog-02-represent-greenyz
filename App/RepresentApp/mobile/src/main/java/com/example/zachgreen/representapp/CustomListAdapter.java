package com.example.zachgreen.representapp;

/**
 * Created by zachgreen on 2/27/16.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private Integer[] imgid;
    private String[] names;
    private String[] parties;
    private String[] emails;
    private String[] websites;
    private String[] tweets;

    public CustomListAdapter(Activity context, Integer[] imgid, String[] names,
                             String[] parties, String[] emails, String[] websites, String[] tweets) {
        super(context, R.layout.officials_list, names);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.imgid=imgid;
        this.names = names;
        this.parties = parties;
        this.emails = emails;
        this.websites = websites;
        this.tweets = tweets;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.officials_list, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView nameTxt = (TextView) rowView.findViewById(R.id.Name);
        TextView partyTxt = (TextView) rowView.findViewById(R.id.Party);
        TextView emailTxt = (TextView) rowView.findViewById(R.id.Email);
        TextView websiteTxt = (TextView) rowView.findViewById(R.id.Website);
        TextView tweetTxt = (TextView) rowView.findViewById(R.id.Tweet);

        imageView.setImageResource(imgid[position]);
        nameTxt.setText(names[position]);
        partyTxt.setText(parties[position]);
        emailTxt.setText("Email: " + emails[position]);
        websiteTxt.setText("Website: " + websites[position]);
        tweetTxt.setText("Latest Tweet: " + tweets[position]);

        return rowView;

    };
}
