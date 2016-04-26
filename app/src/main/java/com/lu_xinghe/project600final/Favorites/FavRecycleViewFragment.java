package com.lu_xinghe.project600final.Favorites;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.lu_xinghe.project600final.Favorites.FavDetails.FavDetailsActivity;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.newsDetails.NewsDetailsActivity;
import com.lu_xinghe.project600final.newsPage.News;
import com.lu_xinghe.project600final.newsPage.newsListFirebaseRecyclerAdapter.NewsListFirebaseRecylerAdapter;


public class FavRecycleViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutMannager;
    NewsListFirebaseRecylerAdapter newsListFirebaseRecylerAdapter;
    private String url = "https://project6000fusers.firebaseio.com/users/";
    private String newsType = "";
    private int count=0;
    private String userName = "";
    Context mContext;
    //private OnNewsListItemClickListener mListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public FavRecycleViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FavRecycleViewFragment newInstance(String userName) {
        FavRecycleViewFragment fragment = new FavRecycleViewFragment();
        Bundle args = new Bundle();
        args.putString("userName", userName);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_my_recycle_view, container, false);
        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab1);
        fab.hide();
        userName = getArguments().getString("userName");
        url = url+userName+"/favorites";
        final Firebase ref = new Firebase(url);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        mLayoutMannager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutMannager);
        newsListFirebaseRecylerAdapter = new NewsListFirebaseRecylerAdapter(News.class, R.layout.news_cardview, NewsListFirebaseRecylerAdapter.NewsViewHolder.class, ref, getActivity());
        mRecyclerView.setAdapter(newsListFirebaseRecylerAdapter);

        newsListFirebaseRecylerAdapter.setOnItemClickListener(new NewsListFirebaseRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String newsType) {
               // mListener.OnNewsListItemClickListener(position, newsType);
                //News news = newsListFirebaseRecylerAdapter.getItem(position);
                count = newsListFirebaseRecylerAdapter.getItemCount();
                Intent intent = new Intent(getActivity().getApplicationContext(), FavDetailsActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("count", count);
                intent.putExtra("position", position);
                //intent.putExtra("newsType", newsType);
                intent.putExtra("userName", userName);
                // Log.d("url", url);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {//when the fab is clicked
            @Override
            public void onClick(View v) {
                mRecyclerView.scrollToPosition(0);//scroll to the top
                //Log.d("Fragment position", "1");
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {//determine either show or hide fab when scroll
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLayoutMannager.findFirstVisibleItemPosition() == 0)
                    fab.hide();
                else
                    fab.show();
            }
        });

        monitorAuthentication();

        return rootView;
    }

    private void monitorAuthentication(){
        final Firebase ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    userName = authData.getUid();
                    ref.removeAuthStateListener(this);
                } else {
                }
            }
        });
    }
}
