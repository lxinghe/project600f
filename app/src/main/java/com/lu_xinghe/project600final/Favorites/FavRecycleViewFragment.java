package com.lu_xinghe.project600final.Favorites;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.lu_xinghe.project600final.Favorites.FavDetails.FavDetailsActivity;
import com.lu_xinghe.project600final.Favorites.FavDetails.News2;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.Utility;
import com.lu_xinghe.project600final.newsDetails.NewsDetailsActivity;
import com.lu_xinghe.project600final.newsPage.News;
import com.lu_xinghe.project600final.newsPage.newsListFirebaseRecyclerAdapter.NewsListFirebaseRecylerAdapter;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class FavRecycleViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutMannager;
    FavFirebaseRecylerAdapter newsListFirebaseRecylerAdapter;
    private String url = "https://project6000fusers.firebaseio.com/users/";
    private String newsType = "";
    private int count=0,selectCounter=0;
    private String uid;
    Context mContext;
    Firebase userRef;
    Menu _menu;
    Firebase ref;
    private Boolean hasCheckBox=false, select = true, unselect = false, select_all = false, delete = false, done = false, firstCall = true;
    private OnEmptyFavListener mListener;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public FavRecycleViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FavRecycleViewFragment newInstance() {
        FavRecycleViewFragment fragment = new FavRecycleViewFragment();
        Bundle args = new Bundle();
        //args.putString("userName", userName);
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

        final View rootView = inflater.inflate(R.layout.fragment_my_recycle_view, container, false);
        userRef = new Firebase("https://project6000fusers.firebaseio.com/users");
        getUserInfo();
        monitorAuthentication();
        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab1);
        fab.hide();
        if(savedInstanceState!=null){
            hasCheckBox=savedInstanceState.getBoolean("hasCheckBox");
            select=savedInstanceState.getBoolean("select");
            select_all=savedInstanceState.getBoolean("select_all");
            unselect=savedInstanceState.getBoolean("unselect");
            delete=savedInstanceState.getBoolean("delete");
            done=savedInstanceState.getBoolean("done");
        }
        url = url+uid+"/favorites";
        ref = new Firebase(url);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        mLayoutMannager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutMannager);
        if(hasCheckBox==false)
            setAdpter1();
        else
            setAdpter2();
        newsListFirebaseRecylerAdapter.setOnItemClickListener(new FavFirebaseRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String newsType) {
                count = newsListFirebaseRecylerAdapter.getItemCount();
                Intent intent = new Intent(getActivity().getApplicationContext(), FavDetailsActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("count", count);
                intent.putExtra("position", position);
                //intent.putExtra("newsType", newsType);
                //intent.putExtra("userName", userName);
                // Log.d("url", url);
                startActivity(intent);
            }

            @Override
            public void onOverFlowMenuClick(View view, final int position) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_delete:
                                deleteItem(position);
                                return true;

                            default:
                                return false;
                        }
                    }
                });

                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.pop_up_menu, popupMenu.getMenu());
                popupMenu.show();
            }

            @Override
            public void OnCheckBoxClickListener(int position, boolean isChecked) {
                Utility.changeSelect(userRef, uid, position, isChecked);
                count = newsListFirebaseRecylerAdapter.getItemCount();
                if (isChecked == true)
                    selectCounter++;
                else
                    selectCounter--;
                menuItemShowOrHide();
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

        emptyFavListener();
        return rootView;
    }

    private void monitorAuthentication(){

        userRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    // user is logged in
                    //Log.e("uid: ", "" + authData.getUid());
                    getUserInfo();
                } else {
                    // user is not logged in
                    //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void getUserInfo(){
        AuthData authData = userRef.getAuth();
        if (authData != null) {
            // user authenticated
            uid = authData.getUid();
        } else {
            // no user authenticated
            //userName = "stranger";
        }
    }

    public void deleteItem(final int position){
        //News news = newsListFirebaseRecylerAdapter.getItem(position);
        final Firebase fav = userRef.child(uid).child("favorites");
        fav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {//loop through all kids
                    News favNews = favSnapshot.getValue(News.class);
                    if (counter == position) {
                        fav.child(favSnapshot.getKey()).removeValue();
                        break;
                    }
                    counter++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,  MenuInflater inflater){
        //Inflate the menu; this adds items to the action bar if it is present
        if(menu.findItem(R.id.fav1)==null){
            //Log.d("menu", "hi");
            inflater.inflate(R.menu.fav_menu, menu);}
        _menu = menu;
        setMenuItemVisibility();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        Intent intent;

        switch(id){
            case R.id.select:
                item.setVisible(false);
                setAdpter2();
                select = false;
                select_all=true;
                done=true;
                _menu.findItem(R.id.done).setVisible(true);
                _menu.findItem(R.id.select_all).setVisible(true);
                hasCheckBox = true;
                return true;

            case R.id.unselect:
                Utility.unSelect(userRef,uid);
                selectCounter=0;
                menuItemShowOrHide();
                return true;

            case R.id.select_all:
                Utility.selectAll(userRef,uid);
                count = newsListFirebaseRecylerAdapter.getItemCount();
                selectCounter=count;
                menuItemShowOrHide();
                return true;

            case R.id.delete:
                unFavoriteNews();
                return true;

            case R.id.done:
                selectCounter=0;
                Utility.unSelectAll(userRef, uid);
                setAdpter1();
                item.setVisible(false);
                MenuItem select = _menu.findItem(R.id.select);
                select.setVisible(true);
                hasCheckBox = false;
                menuItemShowOrHide();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setAdpter1(){
        newsListFirebaseRecylerAdapter = new FavFirebaseRecylerAdapter(News2.class, R.layout.fav_cardview, FavFirebaseRecylerAdapter.NewsViewHolder.class, ref, getActivity());
        mRecyclerView.setAdapter(newsListFirebaseRecylerAdapter);
    }

    private void setAdpter2(){
        newsListFirebaseRecylerAdapter = new FavFirebaseRecylerAdapter(News2.class, R.layout.fav_cardview2, FavFirebaseRecylerAdapter.NewsViewHolder.class, ref, getActivity());
        mRecyclerView.setAdapter(newsListFirebaseRecylerAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("hasCheckBox", hasCheckBox);
        outState.putBoolean("select", select);
        outState.putBoolean("unselect", unselect);
        outState.putBoolean("select_all", select_all);
        outState.putBoolean("delete", delete);
        outState.putBoolean("done", done);
    }

    private void setMenuItemVisibility(){
        if(select==true)
            _menu.findItem(R.id.select).setVisible(true);
        else
            _menu.findItem(R.id.select).setVisible(false);
        if(unselect==true)
            _menu.findItem(R.id.unselect).setVisible(true);
        else
            _menu.findItem(R.id.unselect).setVisible(false);
        if(select_all==true)
            _menu.findItem(R.id.select_all).setVisible(true);
        else
            _menu.findItem(R.id.select_all).setVisible(false);
        if(delete==true)
            _menu.findItem(R.id.delete).setVisible(true);
        else
            _menu.findItem(R.id.delete).setVisible(false);
        if(done==true)
            _menu.findItem(R.id.done).setVisible(true);
        else
            _menu.findItem(R.id.done).setVisible(false);
    }

    private void menuItemShowOrHide(){
        count = newsListFirebaseRecylerAdapter.getItemCount();
        //Log.e("count",""+count);
        if(selectCounter>0){
            _menu.findItem(R.id.unselect).setVisible(true);
            unselect=true;
            _menu.findItem(R.id.delete).setVisible(true);
            delete=true;
        }
        else{
            _menu.findItem(R.id.unselect).setVisible(false);
            unselect=false;
            _menu.findItem(R.id.delete).setVisible(false);
            delete=false;
        }

        if(selectCounter==count){
            _menu.findItem(R.id.select_all).setVisible(false);
            select_all=false;
        }
        else{
            _menu.findItem(R.id.select_all).setVisible(true);
            select_all=true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmptyFavListener) {
            mListener = (OnEmptyFavListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onListItemClickListener");
        }
    }

    public interface OnEmptyFavListener {

        void OnEmptyFavListener();
    }

    private void emptyFavListener(){
        userRef.child(uid).child("favorites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    callActivityWhenEmpty();
                    userRef.removeEventListener(this);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void callActivityWhenEmpty(){
        if(count!=0&&firstCall){
            Log.e("this happened", "really");
            mListener.OnEmptyFavListener();
            firstCall=false;
        }
    }

    private void unFavoriteNews(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You might never find this news again.")
                .setTitle("Unfavorite the news?");

        builder.setPositiveButton("Unfavorite", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Utility.deleteSelectedFav(userRef,uid);
                selectCounter=0;
                menuItemShowOrHide();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}