package com.lu_xinghe.project600final.Comment;

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
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.lu_xinghe.project600final.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentRecycleViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutMannager;
    NewsListFirebaseRecylerAdapter newsListFirebaseRecylerAdapter;
    private String url = "";
    private String newsType = "";
    private int count=0;
    private String userName = "";
    Context mContext;
    //private OnNewsListItemClickListener mListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public CommentRecycleViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CommentRecycleViewFragment newInstance(String url) {
        CommentRecycleViewFragment fragment = new CommentRecycleViewFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
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
        final View rootView = inflater.inflate(R.layout.fragment_comment_recycle_view, container, false);
        url = getArguments().getString("url");
        final Firebase ref = new Firebase(url);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        mLayoutMannager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutMannager);
        newsListFirebaseRecylerAdapter = new NewsListFirebaseRecylerAdapter(Comment.class, R.layout.comment_cardview, NewsListFirebaseRecylerAdapter.NewsViewHolder.class, ref, getActivity());
        mRecyclerView.setAdapter(newsListFirebaseRecylerAdapter);

        return rootView;
    }

    private static class NewsListFirebaseRecylerAdapter extends FirebaseRecyclerAdapter<Comment,NewsListFirebaseRecylerAdapter.NewsViewHolder> {

        private  Context mContext ;
        //private static OnItemClickListener mItemClickListener;

        public NewsListFirebaseRecylerAdapter(Class<Comment> modelClass, int modelLayout,
                                              Class<NewsViewHolder> holder, Query ref, Context context) {
            super(modelClass, modelLayout, holder, ref);
            this.mContext = context;
        }

        @Override
        protected void populateViewHolder(NewsViewHolder newsViewHolder, Comment comment, int i) {

            //TODO: Populate viewHolder by setting the movie attributes to cardview fields
            newsViewHolder.vDate.setText(comment.getDate());
            newsViewHolder.vUserName.setText(comment.getUserName());
            newsViewHolder.vComment.setText(comment.getComment());
            if(!comment.getProfileImageUrl().equals("")){
                Picasso.with(mContext).load(comment.getProfileImageUrl()).into(newsViewHolder.vProfileImage);
            }

        }

        //TODO: Populate ViewHolder and add listeners.
        public static class NewsViewHolder extends RecyclerView.ViewHolder{

            TextView vDate;
            TextView vUserName;
            TextView vComment;
            CircleImageView vProfileImage;


            public NewsViewHolder(View view) {
                super(view);
                vDate = (TextView)view.findViewById(R.id.date_comment);
                vUserName = (TextView)view.findViewById(R.id.userName_comment);
                vComment = (TextView)view.findViewById(R.id.comment);
                vProfileImage = (CircleImageView) view.findViewById(R.id.profile_image);
            }
        }
    }
}


