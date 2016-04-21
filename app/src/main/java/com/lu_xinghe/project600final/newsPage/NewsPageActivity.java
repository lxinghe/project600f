package com.lu_xinghe.project600final.newsPage;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.client.Firebase;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.newsPage.newsListRecycleViewFragment.NewsListRecycleViewFragment;
import com.lu_xinghe.project600final.newsPage.newsListRecycleViewFragment.NewsListRecycleViewFragment2;
import com.lu_xinghe.project600final.newsPage.newsListRecycleViewFragment.NewsListRecycleViewFragment3;

public class NewsPageActivity extends AppCompatActivity

{

    ScreenSlidePagerAdapter1 mPageAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        Firebase.setAndroidContext(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SU News");

        mPageAdapter = new ScreenSlidePagerAdapter1(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);//preload 3 fragment up ahead
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setCurrentItem(0);
        customiseViewPager();//animation
        TabLayout tabLayout =(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void customiseViewPager() {//animation for viewPager

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {

                //Rotation effect
                page.setRotationY(position * -20);
            }
        });
    }

    public static class ScreenSlidePagerAdapter1 extends FragmentStatePagerAdapter {//viewPager adapter

        public ScreenSlidePagerAdapter1(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        //now the best way I found to fix the but is using 3 different fragment, firebase adapter
            //I know it's not clean but I tried my best
            //Bug description: if I don't do this, when you are clicking on the card of the list
            //it's probably another firebase adapter's responding, and give you some unexpected results
            //the reason, I guess, view and firebase adapter cannot be reused if click on the item is supposed
            //trigger an event. Otherwise, it doesn't matter. --Xinghe Lu 04/16/2016
            if(position==1)
                return NewsListRecycleViewFragment2.newInstance(position);
            else{
                if(position==0)
                    return NewsListRecycleViewFragment.newInstance(0);
                else
                    return NewsListRecycleViewFragment3.newInstance(2);
            }
        }


        @Override
        public int getCount(){return 3;}

        @Override
        public CharSequence getPageTitle(int position){//set tab name
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


}
