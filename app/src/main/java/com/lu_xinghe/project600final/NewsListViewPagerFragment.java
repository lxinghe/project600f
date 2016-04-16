package com.lu_xinghe.project600final;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NewsListViewPagerFragment extends Fragment
                                implements NewsListRecycleViewFragment.OnNewsListItemClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ScreenSlidePagerAdapter mPageAdapter;
    ViewPager mViewPager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnNewsListItemClickListener2 mListener;

    public NewsListViewPagerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewsListViewPagerFragment newInstance() {
        NewsListViewPagerFragment fragment = new NewsListViewPagerFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        mPageAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager)view.findViewById(R.id.pager);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setCurrentItem(0);

        customiseViewPager();

        TabLayout tabLayout =(TabLayout)view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void customiseViewPager() {

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                //Fading out
                //final float normalized_position = Math.abs(Math.abs(position) - 1);
                //page.setAlpha(normalized_position);

                //Scaling effect
                //final float normalized_position = Math.abs(Math.abs(position)-1);
                //page.setScaleX(normalized_position/2+0.5f);
                //page.setScaleY(normalized_position/2+0.5f);

                //Rotation effect
                page.setRotationY(position * -20);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewsListItemClickListener2) {
            mListener = (OnNewsListItemClickListener2) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnNewsListItemClickListener2 {
        // TODO: Update argument type and name
        void OnNewsListItemClickListener2(String newsId, String url);
    }

    public void OnNewsListItemClickListener(String newsId, String url){
            mListener.OnNewsListItemClickListener2(newsId,url);
    }
}
