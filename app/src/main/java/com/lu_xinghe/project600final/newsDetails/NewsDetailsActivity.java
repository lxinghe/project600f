package com.lu_xinghe.project600final.newsDetails;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */


import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.firebase.client.Firebase;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.newsPage.NewsPageActivity;

public class NewsDetailsActivity extends AppCompatActivity
                                implements NavigationView.OnNavigationItemSelectedListener,
                                 NewsDetailsViewPagerFragment.onPositionChangedListener {

    Fragment mContent;
    private String newsId, url, newsType, userName;
    private int count, position;
    Toolbar mToolBar;
    NavigationView navigationView;
    ActionBar mActionBar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    ImageView arrow;
    private boolean down = true;


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
            newsType = extras.getString("newsType");
            switch (newsType) {
                case "topNews":
                    newsType = "Top News";
                    break;
                case "sports":
                    newsType = "Sports";
                    break;
                default:
                    newsType = "Academia";
            }

            //if(extras.getString("userName")!=null)
            userName = (String) extras.get("userName");
            Log.d("user Name: ", userName);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(newsType);//set label

        setDrawer();

        if (savedInstanceState != null) {//load details fragment
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        } else {
            mContent = NewsDetailsViewPagerFragment.newInstance(count, position, url);
        }


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent)
                .commit();

        arrow = (ImageView) findViewById(R.id.change_news_menu_down);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (down) {
                    arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, HeadlinesFragment.newInstance(userName, newsType))
                            .commit();
                } else {
                    arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
                    mContent=NewsDetailsViewPagerFragment.newInstance(count, position, url);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, mContent)
                            .commit();
                }
                down = !down;
            }
        });
    }

    private void setDrawer() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(mToolBar);
        mActionBar = getSupportActionBar();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);//navigation drawer
        navigationView.setNavigationItemSelectedListener(this);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolBar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.item0://jump to news page
                intent = new Intent(getApplicationContext(), NewsPageActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
                break;
            case R.id.item1:
                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, AboutMeFragment.newInstance())
                        .addToBackStack(null).commit();
                onHomePage=false;*/
                break;
            case R.id.item2:
                /*intent = new Intent(this, TaskTwoActivity.class);
                startActivity(intent);*/
                break;
            case R.id.item3:
                /*intent = new Intent(this, TaskThreeActivity.class);
                startActivity(intent);*/
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onPositionChangedListener(int position) {
        this.position = position;
    }
}
