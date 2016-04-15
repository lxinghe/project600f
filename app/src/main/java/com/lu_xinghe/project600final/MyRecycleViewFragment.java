package com.lu_xinghe.project600final;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;


public class MyRecycleViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutMannager;
    MyFirebaseRecylerAdapter2 myFirebaseRecylerAdapter;
    String url = "https://project-0403.firebaseio.com/news/";
    String newsType = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public MyRecycleViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyRecycleViewFragment newInstance(String newsType) {
        MyRecycleViewFragment fragment = new MyRecycleViewFragment();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        myFirebaseRecylerAdapter = new MyFirebaseRecylerAdapter2(News.class, R.layout.news_cardview, MyFirebaseRecylerAdapter2.NewsViewHolder.class, ref, getActivity());
        mRecyclerView.setAdapter(myFirebaseRecylerAdapter);

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    /*@Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/


    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
