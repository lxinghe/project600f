package com.lu_xinghe.project600final;

/**
 * Created by lu_xi on 2/14/2016.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;
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
                // The first section of the app is the most interesting -- it offers
                // a launchpad into the other demonstrations in this example application.
                return RecyclerViewFragment.newInstance();

            default:
                // The other sections of the app are dummy placeholders.

                return RecyclerViewFragment.newInstance();
        }
    }

    @Override
    public int getCount(){return 3;}

    @Override
    public CharSequence getPageTitle(int position){
        Locale l = Locale.getDefault();
        /*MovieData movieData = new MovieData();
        HashMap<String,?> movie =  movieData.getItem(position);
        String name = (String)movie.get("name");*/
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
        return name.toUpperCase(l);
    }


}
