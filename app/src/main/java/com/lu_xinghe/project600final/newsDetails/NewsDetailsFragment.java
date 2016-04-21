package com.lu_xinghe.project600final.newsDetails;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lu_xinghe.project600final.R;
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
    ImageView newsImage1IV,newsImage2IV;
    TextView newsArticle1IV,newsArticle2IV, newsTitleIV,newsImageDescription1IV,newsImageDescription2IV,
            newsSubtitle1IV,newsSubtitle2IV, newsAuthorIV, newsDate;
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
        //setRetainInstance(true);
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
        final View view = inflater.inflate(R.layout.fragment_news_details, container, false);
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab4);
        final NestedScrollView scrollView = (NestedScrollView)view.findViewById(R.id.scrollView);
        fab.hide();
        url = getArguments().getString("url");
        newsId = getArguments().getString("newsId");
        //Log.d("News ID: ", newsId);

        final Firebase ref = new Firebase(url);
        ref.child(newsId).addListenerForSingleValueEvent(new ValueEventListener() {//get data from database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("OnDataChange : ", dataSnapshot.toString());
                HashMap<String, String> news = (HashMap<String, String>) dataSnapshot.getValue();
                //Log.d("Movie description: ", movie.get("description"));
                setNews(news);
                setPage(view);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        // FloatingActionButton fab4=(FloatingActionButton)getActivity().findViewById(R.id.share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollTo(0, 0);
                Log.d("View id Name: ", v.getResources().getResourceName(view.getId()));
            }
        });

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {//api level min 23?
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollX != 0 || scrollY != 0)
                    fab.show();
                else
                    fab.hide();
            }
        });
        return view;
    }

    private void setPage(View view){//used to set movie page

        newsTitleIV = (TextView)view.findViewById(R.id.title);
        newsAuthorIV = (TextView)view.findViewById(R.id.author);
        newsImage1IV = (ImageView)view.findViewById(R.id.image1);
        newsImageDescription1IV = (TextView)view.findViewById(R.id.imageDes1);
        newsSubtitle1IV = (TextView)view.findViewById(R.id.subtitle1);
        newsArticle1IV = (TextView)view.findViewById(R.id.article1);
        newsImage2IV = (ImageView)view.findViewById(R.id.image2);
        newsImageDescription2IV = (TextView)view.findViewById(R.id.imageDes2);
        newsSubtitle2IV = (TextView)view.findViewById(R.id.subtitle2);
        newsArticle2IV = (TextView)view.findViewById(R.id.article2);
        newsDate = (TextView)view.findViewById(R.id.date);
        //Log.d("url", news.get("image"));
        newsTitleIV.setText(news.get("title"));
        newsAuthorIV.setText("by " + news.get("author"));
        newsDate.setText(news.get("month") + "/" + news.get("date") + "/" + news.get("year"));
        Picasso.with(getContext()).load(news.get("image1")).into(newsImage1IV);
        newsImageDescription1IV.setText(news.get("imageDescription1"));
        newsSubtitle1IV.setText(news.get("subtitle1"));
        newsArticle1IV.setText(news.get("article1"));


        if(!news.get("image2").equals(""))
            Picasso.with(getContext()).load(news.get("image2")).into(newsImage2IV);
        else
            newsImage2IV.setVisibility(View.GONE);
        if(!news.get("imageDescription2").equals(""))
            newsImageDescription2IV.setText(news.get("imageDescription2"));
        else
            newsImageDescription2IV.setVisibility(View.GONE);
        if(!news.get("subtitle2").equals(""))
            newsSubtitle2IV.setText(news.get("subtitle2"));
        else
            newsSubtitle2IV.setVisibility(View.GONE);
        if(!news.get("article2").equals(""))
            newsArticle2IV.setText(news.get("article2"));
        else
            newsArticle2IV.setVisibility(View.GONE);
    }

    private void setNews(HashMap<String, String> news){
        this.news = news;
    }


}
