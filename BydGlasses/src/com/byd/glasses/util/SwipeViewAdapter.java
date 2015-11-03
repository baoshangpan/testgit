package com.byd.glasses.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class SwipeViewAdapter extends FragmentPagerAdapter {

    private static final String TAG = "-->SwipeViewAdapter";
    public SwipeViewAdapter(FragmentManager fm) {
        super(fm);
        Log.v(TAG, "Create");
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        PagerFragment pf = PagerFragment.newInstance(arg0);
        Log.v(TAG, "getItem");
        return pf;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }
    
    public CharSequence getPageTitle(int position){
        return "Object:" + (position + 1);
    }

}
