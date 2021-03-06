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
import android.widget.TextView;


import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lu_xinghe.project600final.Authentication.AuthenticationActivity;
import com.lu_xinghe.project600final.Favorites.FavoritesActivity;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.Utility;
import com.lu_xinghe.project600final.newsPage.NewsPageActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsDetailsActivity extends AppCompatActivity
                                implements NavigationView.OnNavigationItemSelectedListener,
                                 NewsDetailsViewPagerFragment.onPositionChangedListener {

    Fragment mContent;
    private String newsId, url, newsType, userName="stranger", pageTitle, uid;
    private int count, position;
    Toolbar mToolBar;
    NavigationView navigationView;
    ActionBar mActionBar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    ImageView arrow;
    private boolean down = true;
    Bundle extras;
    private int arrowId=R.drawable.ic_keyboard_arrow_down_black_24dp;
    Firebase ref;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        getBundleSavedInfo();
        setDrawer();//set drawer menu
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        arrow = (ImageView) findViewById(R.id.change_news_menu_down);
        getBundleSavedInfo();
        loadDetailsFragment(savedInstanceState);
        getUserInfo();
        monitorAuthentication();

        arrow.setOnClickListener(new View.OnClickListener() {//change status of arrow and load corresponding fragment
            @Override
            public void onClick(View v) {
                if (down) {
                    arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
                    mContent = HeadlinesFragment.newInstance(newsType);
                    arrowId = R.drawable.ic_keyboard_arrow_up_black_24dp;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, mContent)
                            .commit();
                } else {
                    arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
                    mContent = NewsDetailsViewPagerFragment.newInstance(count, position, url, newsType);
                    arrowId = R.drawable.ic_keyboard_arrow_down_black_24dp;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, mContent)
                            .commit();
                }
                down = !down;
            }
        });
    }

    private void getBundleSavedInfo(){
        extras = getIntent().getExtras();
        if (extras != null) {
            newsId = extras.getString("newsId");
            url = extras.getString("url");
            count = extras.getInt("count");
            position = extras.getInt("position");
            newsType = extras.getString("newsType");
            switch (newsType) {
                case "topNews":
                    pageTitle = "Top News";
                    break;
                case "sports":
                    pageTitle = "Sports";
                    break;
                default:
                    pageTitle = "Academia";
            }
            //userName = (String) extras.get("userName");
        }
    }

    private void loadDetailsFragment(Bundle savedInstanceState){//load details fragment
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
            arrow.setImageDrawable(getResources().getDrawable(savedInstanceState.getInt("arrowId")));//recover arrow status
        } else {
            mContent = NewsDetailsViewPagerFragment.newInstance(count, position, url, newsType);
            arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent)
                .commit();
    }

    private void setDrawer() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(mToolBar);
        mActionBar = getSupportActionBar();
        getSupportActionBar().setTitle(pageTitle);//set label
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
                //intent.putExtra("userName", userName);
                startActivity(intent);
                break;
            case R.id.item1:
                if(userName.equals("stranger")){
                    intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                    startActivity(intent);
                }
                else
                {intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                    //intent.putExtra("userName", userName);
                    startActivity(intent);}
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        outState.putBoolean("down", down);
        outState.putInt("arrowId", arrowId);
        outState.putInt("position", position);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        down = savedInstanceState.getBoolean("down");//restore the navigation arrow status
        arrowId = savedInstanceState.getInt("arrowId");
        position = savedInstanceState.getInt("position");
    }


    private void monitorAuthentication(){
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    // user is logged in
                    //Log.e("uid: ", "" + authData.getUid());
                    getUserInfo();
                } else {
                    // user is not logged in
                    //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void getUserInfo(){
        final AuthData authData = ref.getAuth();
        if (authData != null) {
            // user authenticated
            uid = authData.getUid();
            ref.child(authData.getUid()).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, String> userInfo = (HashMap<String, String>) dataSnapshot.getValue();
                    TextView userNameIV = (TextView) navigationView.findViewById(R.id.userName_drawer);
                    TextView userEmailIV = (TextView) navigationView.findViewById(R.id.email_drawer);
                    userNameIV.setText(userInfo.get("userName"));
                    userEmailIV.setText(userInfo.get("email"));
                    userName = userInfo.get("userName");
                    CircleImageView profileImage = (CircleImageView) navigationView.findViewById(R.id.profile_image);
                    Picasso.with(getApplicationContext()).load((String) authData.getProviderData().get("profileImageURL")).into(profileImage);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } else {
            // no user authenticated
            userName = "stranger";
        }
    }
}
