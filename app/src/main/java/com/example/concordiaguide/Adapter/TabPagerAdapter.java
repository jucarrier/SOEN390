package com.example.concordiaguide.Adapter;

import com.example.concordiaguide.Fragments.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private static final int total_pages = 2;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NearByFragment();
            case 1:
                return new MyLocationFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return total_pages;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Near By";
            case 1:
                return "My Location";
        }
        return null;
    }
}