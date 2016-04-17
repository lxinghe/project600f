package com.lu_xinghe.project600final;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.client.Firebase;

public class NewsDetailsActivity extends AppCompatActivity {

    Fragment mContent;
    String newsId, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Firebase.setAndroidContext(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             newsId = extras.getString("newsId");
             url = extras.getString("url");
        }

        if(savedInstanceState!=null){
            mContent=getSupportFragmentManager().getFragment(savedInstanceState,"mContent");
        }
        else {
            mContent= NewsDetailsFragment.newInstance(newsId, url);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.news_container, mContent)
                .commit();
    }

}
