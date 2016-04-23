package com.lu_xinghe.project600final.newsDetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.Utility;
import com.lu_xinghe.project600final.newsPage.News;

import java.util.HashMap;

/**
 *Created by Lu, Xinghe on 1/21/2016
 */
public class NewsDetailsViewPagerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ScreenSlidePagerAdapter mPageAdapter;
    ViewPager mViewPager;
    private int count, position;
    private String url;
    private onPositionChangedListener mListener;
    private boolean fav;
    private String userName, newsType;
    private Menu _menu;


    public NewsDetailsViewPagerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewsDetailsViewPagerFragment newInstance(int count, int position, String url, String userName, String newsType) {
        NewsDetailsViewPagerFragment fragment = new NewsDetailsViewPagerFragment();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        args.putString("url", url);
        args.putString("newsType", newsType);
        args.putInt("count", count);
        args.putInt("position", position);
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);//need landscape layout
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
        View view = inflater.inflate(R.layout.fragment_news_details_view_pager, container, false);
        position = getArguments().getInt("position");
        count = getArguments().getInt("count");
        url = getArguments().getString("url");
        userName = getArguments().getString("userName");
        newsType = getArguments().getString("newsType");
        setPageAdapter(view);
        return view;
    }

    private void setPageAdapter(final View view){
        mPageAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), count, url);
        mViewPager = (ViewPager) view.findViewById(R.id.newsDetailsPager);
        mViewPager.setOffscreenPageLimit(6);//preload 6 fragment up ahead
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int currentPosition) {
                position = currentPosition;
                mListener.onPositionChangedListener(currentPosition);//tell activity the page changed
                //setFavIcon(currentPosition);
            }
        });

        customiseViewPager();
    }

    private void setFavIcon(final int currentPosition){
        fav = false;
        //Log.d("hey!!","I am here!!");
        Firebase userRef = new Firebase("https://project6000fusers.firebaseio.com/users/"+userName);
        final Firebase favRef = userRef.child("favorites");
        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int i = 0;
                    while (i<(int)dataSnapshot.getChildrenCount()){
                        HashMap<String, String> favNews = (HashMap<String, String>) dataSnapshot.child("fav"+Integer.toString(i+1)).getValue();
                        if(favNews.get("newsType").equals(newsType)){
                            if(Integer.parseInt(favNews.get("id"))-1==currentPosition){
                                fav = true;
                                i = (int)dataSnapshot.getChildrenCount();
                            }
                        }
                        i++;
                    }
                }
                if(fav==false) {
                    if (_menu.findItem(R.id.fav1).getIcon()!=getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp))
                        _menu.findItem(R.id.fav1).setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                }
                else{
                    if (_menu.findItem(R.id.fav1).getIcon()!=getResources().getDrawable(R.drawable.ic_favorite_black_24dp))
                        _menu.findItem(R.id.fav1).setIcon(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    private void customiseViewPager() {//animation for viewPager

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                //Scaling effect
                final float normalized_position = Math.abs(Math.abs(position)-1);
                page.setScaleX(normalized_position / 2 +0.5f);
                page.setScaleY(normalized_position/2+0.5f);

            }
        });
    }

    public static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {//viewPager adapter

        int count = 0;
        String url;
        String newsId = "";
        public ScreenSlidePagerAdapter(FragmentManager fm, int size, String url){
            super(fm);
            count = size;
            this.url = url;
        }

        @Override
        public Fragment getItem(int position) {
            newsId="news"+Integer.toString(position+1);
            return NewsDetailsFragment.newInstance(newsId, url);
        }

        @Override
        public int getCount(){return count;}
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onPositionChangedListener) {
            mListener = (onPositionChangedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onListItemClickListener");
        }
    }

    public interface onPositionChangedListener {

        void onPositionChangedListener(int position);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        if(menu.findItem(R.id.fav1)==null)
            inflater.inflate(R.menu.news_details_viewpager_fragment_menu, menu);
        _menu = menu;
        setFavIcon(position);//set favorite icon status
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        Intent intent;

        switch(id){//when favorite icon is clicked
            case R.id.fav1:
                if(fav==false){
                    item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    final Firebase ref = new Firebase(url);
                    String newsId = "news"+Integer.toString(position+1);
                    final String username = userName;
                    Log.d("url",url);
                    Log.d("newsId", newsId);
                    ref.child(newsId).addListenerForSingleValueEvent(new ValueEventListener() {//get data from database
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final HashMap<String, String> news = (HashMap<String, String>) dataSnapshot.getValue();
                            final Firebase userRef = new Firebase("https://project6000fusers.firebaseio.com/users/"+username);
                            final Firebase favRef = userRef.child("favorites");

                            favRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.e("Count ", "" + dataSnapshot.getChildrenCount());
                                    final int favCount = (int)dataSnapshot.getChildrenCount();
                                    News favNews = Utility.setFavNews(news);
                                    favRef.child("fav"+Integer.toString(favCount+1)).setValue(favNews);
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }
                else{
                    item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));

                }
                fav = !fav;
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
