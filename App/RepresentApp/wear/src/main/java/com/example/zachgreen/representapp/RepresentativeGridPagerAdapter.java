package com.example.zachgreen.representapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.FragmentGridPagerAdapter;

/**
 * Created by zachgreen on 3/1/16.
 */
public class RepresentativeGridPagerAdapter extends FragmentGridPagerAdapter {

    private Context mContext;
    private Bundle args;

    public RepresentativeGridPagerAdapter(Context ctx, FragmentManager fm, Bundle bundle) {
        super(fm);
        mContext = ctx;
        args = bundle;
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
//
//        // Override methods in FragmentGridPagerAdapter
////        ...
//                }
    public Fragment getFragment(int row, int col) {
//        String title =
//                page.titleRes != 0 ? mContext.getString(page.titleRes) : null;
//        String text =
//                page.textRes != 0 ? mContext.getString(page.textRes) : null;

        if (col != PAGES[0].length) {
            Page page = PAGES[row][col];
            RepresentativeCardFragment fragment = new RepresentativeCardFragment();
//            bundle.putString("name", page.title);
//            bundle.putString("party", page.text);
//            bundle.putInt("pic_id", page.iconRes);
            fragment.setArguments(args);
            return fragment;
        } else {
            ElectionCardFragment electionFrag = new ElectionCardFragment();
            electionFrag.setArguments(args);
            return electionFrag;
        }

        // Advanced settings (card gravity, card expansion/scrolling)
//        fragment.setCardGravity(page.cardGravity);
//        fragment.setExpansionEnabled(page.expansionEnabled);
//        fragment.setExpansionDirection(page.expansionDirection);
//        fragment.setExpansionEnabled (false);
//        fragment.setExpansionFactor((float).5);
    }

    public Drawable getBackgroundForPage(int row, int column) {
//        if( column != PAGES[0].length ) {
            // Place image at specified position
            return mContext.getResources().getDrawable(R.drawable.ic_flag, null);
//        } else {
//            // Default to background image for row
//            return GridPagerAdapter.BACKGROUND_NONE;
//        }
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return PAGES.length;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return PAGES[rowNum].length + 1;
    }
}
