package com.lu_xinghe.project600final;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;

import com.firebase.client.Firebase;


public class RecyclerViewFragment extends Fragment
                                    //implements View.OnClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutMannager;
    //MyRecyclerViewAdapter mRecyclerViewAdpater;
    MyFirebaseRecylerAdapter myFirebaseRecylerAdapter;
    //MovieData movieData = new MovieData();
    CheckBox checkBox;
    final Firebase ref = new Firebase("https://crackling-fire-8001.firebaseio.com/moviedata");


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

   //private onListItemClickListener mListener;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RecyclerViewFragment newInstance() {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);

        //mRecyclerView.setHasFixedSize(true);

        mLayoutMannager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutMannager);

        //mRecyclerViewAdpater = new MyRecyclerViewAdapter(getActivity(), movieData.getMoviesList());

        myFirebaseRecylerAdapter = new MyFirebaseRecylerAdapter(Movie.class, R.layout.cardview, MyFirebaseRecylerAdapter.MovieViewHolder.class, ref, getActivity());

        //mRecyclerView.setAdapter(mRecyclerViewAdpater);
        mRecyclerView.setAdapter(myFirebaseRecylerAdapter);

        //defaultAnimation();

        myFirebaseRecylerAdapter.setOnItemClickListener(new MyFirebaseRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Movie movie = myFirebaseRecylerAdapter.getItem(position);
                //mListener.onListItemClickListener(position, movie.getId(), v);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                getActivity().startActionMode(new ActionBarCallBack(position));
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

                            case R.id.popup_duplicate:
                                duplicateItem(position);
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
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        if(menu.findItem(R.id.action_search)==null)
            inflater.inflate(R.menu.fragment_menu_1, menu);
        SearchView search = (SearchView)menu.findItem(R.id.action_search).getActionView();
        search.setQueryHint("Type the name of the movie");
        if(search!=null){
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
                @Override
                public boolean onQueryTextSubmit(String query){
                    for(int i =0;i<myFirebaseRecylerAdapter.getItemCount();i++){Movie movie = myFirebaseRecylerAdapter.getItem(i);
                        String movieName="";
                        movieName = movieName + movie.getName();
                        if(query.toLowerCase().equals(movieName.toLowerCase()))
                            mRecyclerView.scrollToPosition(i);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query){
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*@Override
    public void onAttach(Context context) {
        *//*super.onAttach(context);
        if (context instanceof onListItemClickListener) {
            mListener = (onListItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onListItemClickListener");
        }*//*
    }*/

    /*public interface onListItemClickListener {

        void onListItemClickListener(int position, String movieID, View view);
    }*/

    public void deleteItem(int position){
        Movie movieDelete = myFirebaseRecylerAdapter.getItem(position);
        ref.child(movieDelete.getId()).removeValue();
    }
    public void duplicateItem(int position){
        Movie newMovie = myFirebaseRecylerAdapter.getItem(position);
        newMovie.setName(newMovie.getName()+"- new");
        newMovie.setId(newMovie.getId() + "-new");
        ref.child(newMovie.getId()).setValue(newMovie);
    }

    private void defaultAnimation(){
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(800);
        animator.setRemoveDuration(800);
        //animator.animateAdd();

        mRecyclerView.setItemAnimator(animator);
    }

    class ActionBarCallBack implements ActionMode.Callback{
        int position;

        public ActionBarCallBack(int position){this.position=position;}

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item){
            int id=item.getItemId();
            switch (id){
                case R.id.action_delete:
                    deleteItem(position);
                    mode.finish();
                    break;
                case R.id.action_duplicate:
                    duplicateItem(position);
                    mode.finish();
                    break;
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu){
            mode.getMenuInflater().inflate(R.menu.fragment_menu_3, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode){}

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu){return false;}
    }

}
