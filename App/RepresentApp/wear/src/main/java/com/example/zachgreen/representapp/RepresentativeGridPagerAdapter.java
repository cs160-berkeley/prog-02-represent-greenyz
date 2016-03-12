package com.example.zachgreen.representapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zachgreen on 3/1/16.
 */
public class RepresentativeGridPagerAdapter extends FragmentGridPagerAdapter {

    private Context mContext;
    private Bundle args;
    private JSONObject data;
    private String county = "Unknown County";
    private double obama_percentage = 50;
    private double romney_percentage = 50;
    private ArrayList<String> rep_ids = new ArrayList<String>();
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> parties = new ArrayList<String>();

    public RepresentativeGridPagerAdapter(Context ctx, FragmentManager fm, Bundle bundle) {
        super(fm);
        mContext = ctx;
        args = bundle;
        try {
            data = new JSONObject(args.getString("rep_data"));
            county = data.getString("county");
            obama_percentage = data.getDouble("obama_percentage");
            romney_percentage = data.getDouble("romney_percentage");
//            for (int i = 0; i < rep_names.size(); i++){
//                JSONObject rep_entry = new JSONObject();
//                rep_entry.put("id", rep_ids.get(i));
//                rep_entry.put("name", rep_names.get(i));
//                rep_entry.put("party",rep_parties.get(i));
//                reps_arr.put(rep_entry);
//            }
//            watchJson.put("reps", reps_arr);
            JSONArray reps_arr = data.getJSONArray("reps");
            for (int i = 0; i < reps_arr.length(); i++){
                JSONObject rep_entry = reps_arr.getJSONObject(i);
                rep_ids.add(rep_entry.getString("id"));
                names.add(rep_entry.getString("name"));
                parties.add(rep_entry.getString("party"));
            }
        } catch (Exception e){
            Log.d("T", "Cannot recreate JSON: " + e.toString());
        }

    }

//    static final int[] BG_IMAGES = new int[] {
//            R.drawable.debug_background_1, ...
//    R.drawable.debug_background_5
//};

    // A simple container for static data in each page
    private static class Page {
        // static resources
        String title;
        String text;
        int iconRes;

        public Page(String title, String text, int iconRes) {
            this.title = title;
            this.text = text;
            this.iconRes = iconRes;

        }
    }

    // Create a static set of pages in a 2D array
private final Page[][] PAGES =
            {
                    {
                            new Page("Sen. Barbara Boxer (1)", "Democrat", R.drawable.bboxer_small),
                            new Page("Sen. Barbara Boxer (2)", "Democrat", R.drawable.bboxer_small),
                            new Page("Sen. Barbara Boxer (3)", "Democrat", R.drawable.bboxer_small),
                    }
            };

    public Fragment getFragment(int row, int col) {

        if (col != rep_ids.size()) {

            RepresentativeCardFragment fragment = new RepresentativeCardFragment();

            Bundle arguments = new Bundle();
            arguments.putString("id", rep_ids.get(col));
            arguments.putString("name", names.get(col));
            arguments.putString("party", parties.get(col));
            fragment.setArguments(arguments);
            return fragment;
        } else {
            ElectionCardFragment electionFrag = new ElectionCardFragment();
            Bundle arguments = new Bundle();
            arguments.putString("county", county);
            arguments.putDouble("obama_percentage", obama_percentage);
            arguments.putDouble("romney_percentage", romney_percentage);
            electionFrag.setArguments(arguments);
            return electionFrag;
        }
    }

    public Drawable getBackgroundForPage(int row, int column) {

            return mContext.getResources().getDrawable(R.drawable.ic_flag, null);
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return 1;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return rep_ids.size()+1;
    }
}
