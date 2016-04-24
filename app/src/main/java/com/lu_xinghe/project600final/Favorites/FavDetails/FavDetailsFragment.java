package com.lu_xinghe.project600final.Favorites.FavDetails;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.newsPage.News;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class FavDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String userName, url;
    ImageView newsImage1IV,newsImage2IV;
    TextView newsArticle1IV,newsArticle2IV, newsTitleIV,newsImageDescription1IV,newsImageDescription2IV,
            newsSubtitle1IV,newsSubtitle2IV, newsAuthorIV, newsDate;
    News news;
    private int position;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public FavDetailsFragment() {
        // Required empty public constructor
    }

    public static FavDetailsFragment newInstance(int position, String url) {
        FavDetailsFragment fragment = new FavDetailsFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        args.putInt("position", position);
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
        position = getArguments().getInt("position");


        downLoadData(view);//download data

        fab.setOnClickListener(new View.OnClickListener() {//when fab is clicked
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

    private void downLoadData(final View view){
        final Firebase ref = new Firebase(url);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                if(dataSnapshot.exists()){
                    //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                    for (DataSnapshot favSnapshot: dataSnapshot.getChildren()) {//loop through all kids
                        if(counter==position){
                            News favNews = favSnapshot.getValue(News.class);
                            setNews(favNews);
                            setPage(view);
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
        newsTitleIV.setText(news.getTitle());
        newsAuthorIV.setText("by " + news.getAuthor());
        newsDate.setText(news.getMonth() + "/" + news.getDate() + "/" + news.getYear());
        Picasso.with(getContext()).load(news.getImage1()).into(newsImage1IV);

        if(!news.getImageDescription1().equals(""))
            newsImageDescription1IV.setText(news.getImageDescription1());
        else
            newsImageDescription1IV.setVisibility(View.GONE);
        if(!news.getSubtitle1().equals(""))
            newsSubtitle1IV.setText(news.getSubtitle1());
        else
            newsSubtitle1IV.setVisibility(View.GONE);

        newsArticle1IV.setText(news.getArticle1());

        if(!news.getImage2().equals(""))
            Picasso.with(getContext()).load(news.getImage2()).into(newsImage2IV);
        else
            newsImage2IV.setVisibility(View.GONE);
        if(!news.getImageDescription2().equals(""))
            newsImageDescription2IV.setText(news.getImageDescription2());
        else
            newsImageDescription2IV.setVisibility(View.GONE);
        if(!news.getSubtitle2().equals(""))
            newsSubtitle2IV.setText(news.getSubtitle2());
        else
            newsSubtitle2IV.setVisibility(View.GONE);
        if(!news.getArticle2().equals(""))
            newsArticle2IV.setText(news.getArticle2());
        else
            newsArticle2IV.setVisibility(View.GONE);
    }

    private void setNews(News news){
        this.news = news;
    }

}
