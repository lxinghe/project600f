package com.lu_xinghe.project600final.Favorites;

/**
 * Created by Lu,Xinghe on 4/23/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.Firebase;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.newsDetails.NewsDetailsFragment;
import com.lu_xinghe.project600final.newsDetails.NewsDetailsViewPagerFragment;
import com.lu_xinghe.project600final.newsPage.NewsPageActivity;

public class FavoritesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
                    //FavRecycleViewFragment.OnNewsListItemClickListener
{

    Fragment mContent;
    private String userName;
    Bundle extras;
    Toolbar mToolBar;
    NavigationView navigationView;
    ActionBar mActionBar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Firebase.setAndroidContext(this);
        mToolBar = (Toolbar)findViewById(R.id.toolbar_fav);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Favorites");//set label
        extras = getIntent().getExtras();
        userName = (String) extras.get("userName");
        //Log.e("Fav details userName:", "" + userName);

        setDrawer();

        if (savedInstanceState != null) {//load details fragment
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        } else {
            mContent = FavRecycleViewFragment.newInstance(userName);

        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent)
                .commit();
    }

    private void setDrawer(){
        mActionBar=getSupportActionBar();//tool bar as action bar

        navigationView = (NavigationView)findViewById(R.id.navigation_view);//navigation drawer
        navigationView.setNavigationItemSelectedListener(this);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        //mActionBar.setLogo();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,mToolBar, R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }
        };

        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){//handle events when one of menu item's clicked
        int id = item.getItemId();
        Intent intent;

        switch (id){
            case  R.id.item0:
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
            case  R.id.item2:
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    /*public void OnNewsListItemClickListener(int position, String newsType){
        mContent = NewsDetailsFragment.newInstance("news"+Integer.toString(position+1), "https://project-0403.firebaseio.com/news/"+newsType);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent)
                .commit();
    }*/
}
