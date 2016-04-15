package com.lu_xinghe.project600final;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.client.Firebase;

public class NewsPageActivity extends AppCompatActivity {

    Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        Firebase.setAndroidContext(this);

        if(savedInstanceState!=null){
            mContent=getSupportFragmentManager().getFragment(savedInstanceState,"mContent");
        }
        else {
            mContent=ViewPagerFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent)
                .commit();
    }
}
