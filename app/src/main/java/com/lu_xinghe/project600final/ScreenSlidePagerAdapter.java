package com.lu_xinghe.project600final;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 *
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.Locale;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    int count;

    public ScreenSlidePagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MyRecycleViewFragment.newInstance("topNews");
            case 1:
                return MyRecycleViewFragment.newInstance("sports");
            default:
                return MyRecycleViewFragment.newInstance("academia");
        }
    }

    @Override
    public int getCount(){return 3;}

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
