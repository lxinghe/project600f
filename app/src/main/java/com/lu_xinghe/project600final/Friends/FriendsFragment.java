package com.lu_xinghe.project600final.Friends;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lu_xinghe.project600final.R;

import java.util.HashMap;


public class FriendsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutMannager;
    String uid;
    Firebase ref;
    MyRecyclerViewAdapter mRecyclerViewAdpater;
    FriendData friendData;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public FriendsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String uid) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putString("uid", uid);
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
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        uid = getArguments().getString("uid");
        ref = new Firebase("https://project6000fusers.firebaseio.com/users"+uid);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardList);
        mLayoutMannager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutMannager);

        childListener();

        return view;
    }

   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

    private void childListener(){
        Firebase friendRef = ref.child("friends");

        friendRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap<String, String> friendUid = (HashMap<String, String>)dataSnapshot.getValue();
                Firebase friendInfoRef = ref.child(friendUid.get("friendUid")).child("info");

                friendInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> friendInfo = (HashMap<String, String>)dataSnapshot.getValue();
                        friendData.addItem(friendData.getSize(),friendInfo);
                        if(friendData.getSize()==1){
                            mRecyclerViewAdpater = new MyRecyclerViewAdapter(getActivity(), friendData.getfriendList());
                            mRecyclerView.setAdapter(mRecyclerViewAdpater);
                        }
                        mRecyclerViewAdpater.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
