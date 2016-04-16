package com.lu_xinghe.project600final;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;


public class NewsListRecycleViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutMannager;
    NewsListFirebaseRecylerAdapter newsListFirebaseRecylerAdapter;
    private String url = "https://project-0403.firebaseio.com/news/";
    private String newsType = "";
    Context mContext;
    private OnNewsListItemClickListener mListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public NewsListRecycleViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewsListRecycleViewFragment newInstance(String newsType) {
        NewsListRecycleViewFragment fragment = new NewsListRecycleViewFragment();
        Bundle args = new Bundle();
        args.putString("newsType", newsType);
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
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

        //this.mContext = getActivity().getApplicationContext();
        this.mContext = getContext();
        onAttachFragment(getParentFragment());//hook parent fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_recycle_view, container, false);
        newsType = newsType+getArguments().getString("newsType");
        url = url+newsType;
        final Firebase ref = new Firebase(url);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        mLayoutMannager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutMannager);
        newsListFirebaseRecylerAdapter = new NewsListFirebaseRecylerAdapter(News.class, R.layout.news_cardview, NewsListFirebaseRecylerAdapter.NewsViewHolder.class, ref, getActivity());
        mRecyclerView.setAdapter(newsListFirebaseRecylerAdapter);

        newsListFirebaseRecylerAdapter.setOnItemClickListener(new NewsListFirebaseRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                News news = newsListFirebaseRecylerAdapter.getItem(position);
                mListener.OnNewsListItemClickListener(news.getId(), url);
            }
        });

        return rootView;

    }

    public void onAttachFragment(Fragment fragment){
        try {
            mListener = (OnNewsListItemClickListener)fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }

    public interface OnNewsListItemClickListener {
        // TODO: Update argument type and name
        void OnNewsListItemClickListener(String newsId, String url);
    }
}
