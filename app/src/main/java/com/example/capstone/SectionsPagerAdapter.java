package com.example.capstone;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.restaurants_tab, R.string.dishes_tab, R.string.map_tab};
    private final Context mContext;
    List<FoursquareResults> frs;
    List<FoursquareVenue> details;
    ArrayList<dish> dishList;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public SectionsPagerAdapter(Context context, FragmentManager fm, List<FoursquareResults> frs, List<FoursquareVenue> details, ArrayList<dish> dishList) {
        super(fm);
        mContext = context;
        this.frs = frs;
        this.details = details;
        this.dishList = dishList;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position == 0) return new RestaurantPage();
        if(position == 1) return new DishPage();
        if(position == 2) return new MapFragment();
        return null; //there are three tabs, any higher number is invalid
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // There are 3 tabs.
        return 3;
    }
}