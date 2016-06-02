package com.lu_xinghe.project600final.Friends;

import android.content.Intent;
import android.graphics.Color;
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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lu_xinghe.project600final.Account.AccountActivity;
import com.lu_xinghe.project600final.Account.AccountDetailsFragment;
import com.lu_xinghe.project600final.Authentication.AuthenticationActivity;
import com.lu_xinghe.project600final.Favorites.FavoritesActivity;
import com.lu_xinghe.project600final.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener
{
    private static String userName="stranger", uid;
    Toolbar mToolBar;
    NavigationView navigationView;
    ActionBar mActionBar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    Firebase ref;
    Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Friends");//set label
        setDrawer();
        Firebase.setAndroidContext(this);

        ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        getUserInfo(savedInstanceState);
        monitorAuthentication(savedInstanceState);

    }

    private void setDrawer(){// set drawer
        mActionBar=getSupportActionBar();//tool bar as action bar
        navigationView = (NavigationView)findViewById(R.id.navigation_view_news_page);//navigation drawer
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
                //do nothing coz it's now on news page
                break;
            case R.id.item1:
                if(userName.equals("stranger")){
                    intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                    startActivity(intent);
                    break;
                }
                else {
                    intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                    startActivity(intent);
                    break;
                }

            case  R.id.item2:
                if(userName.equals("stranger")){
                    intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(getApplicationContext(), FriendsActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.item3:
                if(userName.equals("stranger")){
                    intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(getApplicationContext(), AccountActivity.class);
                    startActivity(intent);
                }
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
            uid = authData.getUid();
            final Firebase userInfoRef = ref.child(authData.getUid()).child("info");
            userInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    userInfoRef.child("profileImageURL").setValue(authData.getProviderData().get("profileImageURL"));//update the profile image url when news app is opened

                    if (savedInstanceState != null) {
                        mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, mContent)
                                .commitAllowingStateLoss();
                    }
                    else{

                        mContent = FriendsFragment.newInstance(uid);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, mContent)
                                .commitAllowingStateLoss();
                    }
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
