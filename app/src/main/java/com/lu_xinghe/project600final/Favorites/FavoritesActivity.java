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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.Utility;
import com.lu_xinghe.project600final.newsDetails.NewsDetailsFragment;
import com.lu_xinghe.project600final.newsDetails.NewsDetailsViewPagerFragment;
import com.lu_xinghe.project600final.newsPage.NewsPageActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    FavRecycleViewFragment.OnEmptyFavListener

{
    Fragment mContent;
    private String userName, uid;
    Bundle extras;
    Toolbar mToolBar;
    NavigationView navigationView;
    ActionBar mActionBar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    Firebase ref;
    Bundle savedInstanceState;
    Menu _menu;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setDrawer();
        Firebase.setAndroidContext(this);
        extras = getIntent().getExtras();
        //userName = (String) extras.get("userName");
        ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        getUserInfo(savedInstanceState);
        monitorAuthentication(savedInstanceState);
        //Log.e("Fav details userName:", "" + userName);
        this.savedInstanceState = savedInstanceState;
    }

    private void checkIfFavEmptyAndLoadFragment(final Bundle savedInstanceState){
        Firebase ref = new Firebase("https://project6000fusers.firebaseio.com/users/"+uid+"/favorites");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    loadFavFragment(savedInstanceState);
                else
                    loadBlankFavFragment(savedInstanceState);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void loadBlankFavFragment(Bundle savedInstanceState){
        if (savedInstanceState != null) {//load details fragment
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        } else {
            mContent = FavBlankFragment.newInstance();

        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent)
                .commit();
    }

    private void loadFavFragment(Bundle savedInstanceState){
        if (savedInstanceState != null) {//load details fragment
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        } else {
            mContent = FavRecycleViewFragment.newInstance();

        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent)
                .commit();
    }

    private void setDrawer(){
        mToolBar = (Toolbar)findViewById(R.id.toolbar_fav);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Favorites");//set label
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
                //intent.putExtra("userName", userName);
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

    private void monitorAuthentication(final Bundle savedInstanceState){

        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    // user is logged in
                    //Log.e("uid: ", "" + authData.getUid());
                    getUserInfo(savedInstanceState);
                } else {
                    // user is not logged in
                    //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void getUserInfo(final Bundle savedInstanceState){
        final AuthData authData = ref.getAuth();
        if (authData != null) {
            // user authenticated
            uid = authData.getUid().toString();
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
                    checkIfFavEmptyAndLoadFragment(savedInstanceState);
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

    public void OnEmptyFavListener(){
        //checkIfFavEmptyAndLoadFragment(savedInstanceState);
        mContent=FavBlankFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent)
                .commit();
    }
}
