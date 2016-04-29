package com.lu_xinghe.project600final.Account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.lu_xinghe.project600final.newsDetails.NewsDetailsViewPagerFragment;
import com.lu_xinghe.project600final.newsPage.NewsPageActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                        AccountDetailsFragment.OnFragmentInteractionListener
{

    Toolbar mToolBar;
    NavigationView navigationView;
    ActionBar mActionBar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    Fragment mContent;
    String uid="",userName="stranger", major, status, about, email;
    Firebase ref;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mToolBar = (Toolbar)findViewById(R.id.toolbar_account);
        setSupportActionBar(mToolBar);
        mActionBar=getSupportActionBar();//tool bar as action bar
        getSupportActionBar().setTitle("");//set label
        setDrawer();
        ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        getUserInfo(savedInstanceState);
        monitorAuthentication(savedInstanceState);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                startActivity(intent);
                //finish();
            }
        });

       /* IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.CUSTOM_INTENT");
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("==>", "Broadcast Recieved.");
                finish();

            }
        };
        registerReceiver(receiver, filter);*/
    }

   /* @Override
    protected void onStop()
    {
        unregisterReceiver(receiver);
        super.onStop();
    }*/

    private void loadAccountDetailsFragment(String userName, String email, String major, String about, String status){
        //mActionBar.setTitle(userName);
        mContent = AccountDetailsFragment.newInstance( userName,  email,  major,  about,  status);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent)
                .commitAllowingStateLoss();
    }

    private void setDrawer(){// set drawer
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
                /*if(userName.equals("stranger")){
                    intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                    startActivity(intent);
                }
                else
                {intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                    //intent.putExtra("userName", userName);
                    startActivity(intent);}*/
                break;
            case  R.id.item2:
                /*intent = new Intent(this, TaskTwoActivity.class);
                startActivity(intent);*/
                break;
            case R.id.item3:
                /*if(userName.equals("stranger")){
                    intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                    startActivity(intent);
                }
                else
                {intent = new Intent(getApplicationContext(), AccountActivity.class);
                    //intent.putExtra("userName", userName);
                    startActivity(intent);}*/
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
                    TextView userNameProfile = (TextView) findViewById(R.id.userName_profile);
                    userName = userInfo.get("userName");
                    //mActionBar.setTitle(userName);//set label
                    email = userInfo.get("email");
                    userNameIV.setText(userName);
                    userEmailIV.setText(email);
                    ImageView accountProfileImage = (ImageView) findViewById(R.id.profile_image_account);
                    Picasso.with(getApplicationContext()).load((String) authData.getProviderData().get("profileImageURL")).into(accountProfileImage);
                    CircleImageView profileImage = (CircleImageView) navigationView.findViewById(R.id.profile_image);
                    Picasso.with(getApplicationContext()).load((String) authData.getProviderData().get("profileImageURL")).into(profileImage);
                    userInfoRef.child("profileImageURL").setValue(authData.getProviderData().get("profileImageURL"));//update the profile image url when news app is opened
                    about = userInfo.get("about");
                    major = userInfo.get("major");
                    status = userInfo.get("mood");

                    if (savedInstanceState != null) {
                        mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, mContent)
                                .commitAllowingStateLoss();
                    }
                    else
                        loadAccountDetailsFragment(userName, email,  major,  about,  status);
                    //Log.e("HEY!!", "" + "HERE!!");
                    userNameProfile.setText(userName);

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        /*outState.putString("userName", userName);
        outState.putString("major", major);
        outState.putString("po", position);*/
    }

    public void onFragmentInteraction(){
        Firebase ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        ref.unauth();
        Intent intent = new Intent(getApplicationContext(), NewsPageActivity.class);
// set the new task and clear flags
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
