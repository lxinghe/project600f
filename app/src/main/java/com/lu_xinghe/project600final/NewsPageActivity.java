package com.lu_xinghe.project600final;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class NewsPageActivity extends AppCompatActivity
                                //implements NewsListViewPagerFragment.OnNewsListItemClickListener2
{

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
            mContent= NewsListViewPagerFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent)
                .commit();
    }

    /*public void OnNewsListItemClickListener2(String newsId, String url){
        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        Log.d("newsId", newsId);
        Log.d("url", url);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, NewsDetailsFragment.newInstance(newsId, url))
                .addToBackStack(null).commit();
    }*/
}
