package com.example.android.roomrent.Adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.roomrent.Fragment.AskFragment;
import com.example.android.roomrent.Fragment.DashboardFragment;
import com.example.android.roomrent.Fragment.OfferFragment;

public class CategoryAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"Offer", "Ask", "Dashboard"};

    public CategoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        if (position == 0) {
            return new OfferFragment();
        } else if (position == 1) {
            return new AskFragment();
        } else {
            return new DashboardFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
