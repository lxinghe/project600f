package com.lu_xinghe.project600final.newsDetails;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lu_xinghe.project600final.R;

public class HeadlinesFragment extends ListFragment
                implements AdapterView.OnItemClickListener
{
    private String userName="";
    private String url ="";
    private String newsType = "";

    public static HeadlinesFragment newInstance(String userName, String newsType) {
        HeadlinesFragment fragment = new HeadlinesFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        args.putString("userName", userName);
        args.putString("newsType", newsType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_headlines, container, false);
        userName = getArguments().getString("userName");
        newsType = getArguments().getString("newsType");
        monitorAuthentication();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Headlines, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        //Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        switch (position){//set url and news type
            case 0:
                url = "https://project-0403.firebaseio.com/news/topNews";
                newsType = "topNews";
                break;
            case 1:
                url = "https://project-0403.firebaseio.com/news/sports";
                newsType = "sports";
                break;
            case 2:
                url = "https://project-0403.firebaseio.com/news/academia";
                newsType = "academia";
                break;
        }
        switchNews();
    }

    private void switchNews(){//switch news category
        Firebase ref = new Firebase(url);
        //addValueEventListener
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.e("Count ", "" + dataSnapshot.getChildrenCount());
                Intent intent = new Intent(getActivity().getApplicationContext(), NewsDetailsActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("count", (int) dataSnapshot.getChildrenCount());
                intent.putExtra("position", 0);
                intent.putExtra("newsType", newsType);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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