package com.lu_xinghe.project600final.Favorites.FavDetails;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;

/**
* Created by Lu,Xinghe on 2/14/2016.
*/



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lu_xinghe.project600final.Comment.CommentActivity;
import com.lu_xinghe.project600final.Favorites.FavoritesActivity;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.Utility;
import com.lu_xinghe.project600final.newsPage.News;
import com.lu_xinghe.project600final.newsPage.NewsPageActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    Fragment mContent;
    ScreenSlidePagerAdapter1 mPageAdapter;
    ViewPager mViewPager;
    private String url,userName, uid;
    private int count, position;
    Toolbar mToolBar;
    NavigationView navigationView;
    ActionBar mActionBar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    Bundle extras;
    Firebase ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_details);
        setDrawer();
        Firebase.setAndroidContext(this);
        getInfoFromBundle();
        setViewPager();
        ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        getUserInfo();
        monitorAuthentication();
    }

    private void getInfoFromBundle(){
        extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("url");
            //userName = extras.getString("userName");
            count = extras.getInt("count");
            position = extras.getInt("position");
            //Log.e("Fav details userName:", "" + userName);
        }
    }

    private void setViewPager(){
        if(count!=0){
            mPageAdapter = new ScreenSlidePagerAdapter1(getSupportFragmentManager(), count, url);
            mViewPager = (ViewPager)findViewById(R.id.fav_pager);
            mViewPager.setOffscreenPageLimit(6);//preload 6 fragment up ahead
            mViewPager.setAdapter(mPageAdapter);
            mViewPager.setCurrentItem(position);
            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int currentPosition) {
                    position=currentPosition;
                }
            });
            customiseViewPager();
        }
        else//if there is no more favorite news to show
            startFavActivity();
    }

    private void startFavActivity(){
        Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
        //intent.putExtra("userName", userName);
        startActivity(intent);
    }

    private void customiseViewPager() {//animation for viewPager

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                //Scaling effect
                final float normalized_position = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalized_position / 2 + 0.5f);
                page.setScaleY(normalized_position / 2 + 0.5f);

            }
        });
    }

    private static class ScreenSlidePagerAdapter1 extends FragmentStatePagerAdapter {//viewPager adapter

        int count = 0;
        String url;
        public ScreenSlidePagerAdapter1(FragmentManager fm, int size, String url){
            super(fm);
            count = size;
            this.url = url;
        }

        @Override
        public Fragment getItem(int position) {return FavDetailsFragment.newInstance(position, url);}

        @Override
        public int getCount(){return count;}
    }

    private void setDrawer(){
        mToolBar = (Toolbar)findViewById(R.id.toolbar_fav_details);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Favorites");//set label
        mActionBar=getSupportActionBar();//tool bar as action bar
        navigationView = (NavigationView)findViewById(R.id.navigation_view);//navigation drawer
        navigationView.setNavigationItemSelectedListener(this);
        mActionBar.setDisplayHomeAsUpEnabled(true);
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
                //intent.putExtra("userName", userName);
                startActivity(intent);
                break;
            case R.id.item1:
                //startFavActivity();
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.fav_details_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        Intent intent;

        switch(id){//when favorite icon is clicked
            case R.id.fav1:
                unFavoriteNews();
                return true;
            case  R.id.comment:
                comment();
            return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void unFavoriteNews(){
        AlertDialog.Builder builder = new AlertDialog.Builder((this));
        builder.setMessage("You might never find this news again.")
                .setTitle("Unfavorite the news?");

        builder.setPositiveButton("Unfavorite", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                count--;
                deleteFav();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteFav(){//interact with UI can't put it to Utility
        final Firebase userRef = new Firebase("https://project6000fusers.firebaseio.com/users/" +uid);
        final Firebase favRef = userRef.child("favorites");
        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                if (dataSnapshot.exists()) {
                    //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                    for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {//loop through all kids
                        if (counter == position) {
                            favRef.child(favSnapshot.getKey()).removeValue();
                            setViewPager();
                            break;
                        }
                        counter++;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }

    private void comment(){
        final Firebase ref = new Firebase(url);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter=0;
                if(dataSnapshot.exists()){
                    //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                    for (DataSnapshot favSnapshot: dataSnapshot.getChildren()) {//loop through all kids
                        if(counter==position){
                            News favNews = favSnapshot.getValue(News.class);
                            Intent intent = new Intent(getApplicationContext().getApplicationContext(), CommentActivity.class);
                            //intent.putExtra("userName", userName);
                            intent.putExtra("position", Integer.parseInt(favNews.getId())-1);
                            intent.putExtra("url", "https://project-0403.firebaseio.com/news/"+favNews.getNewsType());
                            intent.putExtra("count", count);
                            intent.putExtra("newsType", favNews.getNewsType());
                            startActivity(intent);
                            break;
                        }
                        counter++;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
