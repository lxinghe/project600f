package com.lu_xinghe.project600final;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.Firebase;
import com.lu_xinghe.project600final.newsList.NewsListRecycleViewFragment;
import com.lu_xinghe.project600final.newsList.NewsListRecycleViewFragment2;
import com.lu_xinghe.project600final.newsList.NewsListRecycleViewFragment3;

public class NewsDetailsActivity extends AppCompatActivity {

    Fragment mContent;
    ScreenSlidePagerAdapter1 mPageAdapter;
    ViewPager mViewPager;
    private String newsId, url;
    private int count, position;
    Toolbar mToolBar2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Firebase.setAndroidContext(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             newsId = extras.getString("newsId");
             url = extras.getString("url");
            count = extras.getInt("count");
            position = extras.getInt("position");
        }

        mPageAdapter = new ScreenSlidePagerAdapter1(getSupportFragmentManager(), count, url);
        mViewPager = (ViewPager)findViewById(R.id.newsDetailsPager);
        mViewPager.setOffscreenPageLimit(6);//preload 6 fragment up ahead
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });
        customiseViewPager();
    }

    private void customiseViewPager() {//animation for viewPager

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                //Scaling effect
                final float normalized_position = Math.abs(Math.abs(position)-1);
                page.setScaleX(normalized_position / 2 +0.5f);
                page.setScaleY(normalized_position/2+0.5f);

            }
        });
    }

    public static class ScreenSlidePagerAdapter1 extends FragmentStatePagerAdapter {//viewPager adapter

        int count = 0;
        String url;
        String newsId = "";
        public ScreenSlidePagerAdapter1(FragmentManager fm, int size, String url){
            super(fm);
            count = size;
            this.url = url;
        }

        @Override
        public Fragment getItem(int position) {
            newsId="news"+Integer.toString(position+1);
            return NewsDetailsFragment.newInstance(newsId, url);
        }

        @Override
        public int getCount(){return count;}
    }
}
