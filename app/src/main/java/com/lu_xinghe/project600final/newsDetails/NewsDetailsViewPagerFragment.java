package com.lu_xinghe.project600final.newsDetails;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lu_xinghe.project600final.R;

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


    public NewsDetailsViewPagerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewsDetailsViewPagerFragment newInstance(int count, int position, String url) {
        NewsDetailsViewPagerFragment fragment = new NewsDetailsViewPagerFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
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
        View view = inflater.inflate(R.layout.fragment_news_details_view_pager, container, false);
        position = getArguments().getInt("position");
        count = getArguments().getInt("count");
        url = getArguments().getString("url");
        setPageAdapter(view);
        return view;
    }

    private void setPageAdapter(View view){
        mPageAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), count, url);
        mViewPager = (ViewPager) view.findViewById(R.id.newsDetailsPager);
        mViewPager.setOffscreenPageLimit(6);//preload 6 fragment up ahead
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int currentPosition) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                //invalidateOptionsMenu();
                //Log.d("current page", Integer.toString(position));
                position = currentPosition;
                Log.d("current page", Integer.toString(position));
                mListener.onPositionChangedListener(currentPosition);//tell activity the page changed
            }
        });

        customiseViewPager();
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
}
