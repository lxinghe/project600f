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
import android.view.ViewGroup;
import java.util.Locale;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    int count = 3;

    public ScreenSlidePagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if(position==1)
            return NewsListRecycleViewFragment2.newInstance(position);
        else{
            if(position==0)
                return NewsListRecycleViewFragment.newInstance(0);
            else
                return NewsListRecycleViewFragment.newInstance(2);
        }
    }


    @Override
    public int getCount(){return count;}

    @Override
    public CharSequence getPageTitle(int position){
        //Locale l = Locale.getDefault();
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
