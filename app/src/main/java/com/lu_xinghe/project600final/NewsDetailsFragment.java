package com.lu_xinghe.project600final;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class NewsDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //HashMap<String,?> movie;
    private String newsId, url;
    //MovieData movieData = null;
    ImageView newsImageIV;
    TextView newsArticleIV;
    //final Firebase ref = new Firebase("https://crackling-fire-8001.firebaseio.com/moviedata");
    HashMap<String, String> news;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public NewsDetailsFragment() {
        // Required empty public constructor
    }

    public static NewsDetailsFragment newInstance(String newsId, String url) {
        NewsDetailsFragment fragment = new NewsDetailsFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        args.putString("newsId", newsId);
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_news_details, container, false);
        url = getArguments().getString("url");
        newsId = getArguments().getString("newsId");
        Log.d("News ID: ", newsId);

        final Firebase ref = new Firebase(url);
        ref.child(newsId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("OnDataChange : ", dataSnapshot.toString());
                HashMap<String, String> news = (HashMap<String, String>) dataSnapshot.getValue();
                //Log.d("Movie description: ", movie.get("description"));
                setNews(news);
                setPage(v);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
        return v;
    }

    public void setPage(View view){//used to set movie page

        newsImageIV = (ImageView)view.findViewById(R.id.image);
        newsArticleIV = (TextView)view.findViewById(R.id.article);
        Log.d("url", news.get("image"));
        Picasso.with(getContext()).load(news.get("image")).into(newsImageIV);
        newsArticleIV.setText(news.get("article"));
    }

    public void setNews(HashMap<String, String> news){
        this.news = news;
    }


}
