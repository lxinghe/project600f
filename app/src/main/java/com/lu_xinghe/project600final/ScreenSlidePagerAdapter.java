package com.lu_xinghe.project600final;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 *
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.Locale;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    int count = 3;

    public ScreenSlidePagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        String newsType = "";
        switch (position) {
            case 0:
                //Log.d("pager position",Integer.toString(position));
                newsType = newsType+"topNews";
                //return NewsListRecycleViewFragment.newInstance("topNews");
                break;
            case 1:
                //Log.d("pager position",Integer.toString(position));
                newsType = newsType+"sports";
                //return NewsListRecycleViewFragment.newInstance("sports");
                break;
            case  2:
                //Log.d("pager position",Integer.toString(position));
                newsType = newsType+"academia";
                //return NewsListRecycleViewFragment.newInstance("academia");
                break;
        }
        Log.d("pager position",Integer.toString(position));
        return NewsListRecycleViewFragment.newInstance(newsType);
    }

    @Override
    public int getCount(){return count;}

    @Override
    public CharSequence getPageTitle(int position){
        Locale l = Locale.getDefault();
        String name ="";
        switch (position){
            case 0:
                name=name+"Top News";
                break;
            case 1:
                name = name + "Sports";
                break;
            case 2:
                name = name + "Academia";
                break;
        }
        return name;
    }
}
