package com.lu_xinghe.project600final;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

//import com.example.lu_xi.hw9_xinghe_lu.Movie;
//import com.example.lu_xi.hw9_xinghe_lu.R;

public class MyFirebaseRecylerAdapter extends FirebaseRecyclerAdapter<Movie,MyFirebaseRecylerAdapter.MovieViewHolder> {

    private static Context mContext ;
    private static OnItemClickListener mItemClickListener;

    public MyFirebaseRecylerAdapter(Class<Movie> modelClass, int modelLayout,
                                    Class<MovieViewHolder> holder, Query ref,Context context) {
        super(modelClass,modelLayout,holder,ref);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(MovieViewHolder movieViewHolder, Movie movie, int i) {

        //TODO: Populate viewHolder by setting the movie attributes to cardview fields
        movieViewHolder.vTitle.setText(movie.getName());
        movieViewHolder.vDescription.setText(movie.getDescription());
        Picasso.with(mContext).load(movie.getUrl()).into(movieViewHolder.vIcon);
        //movieViewHolder.vIcon.setTransitionName(movie.getName());
    }

    //TODO: Populate ViewHolder and add listeners.
    public static class MovieViewHolder extends RecyclerView.ViewHolder{
        ImageView vIcon;
        TextView vTitle;
        TextView vDescription;
        //CheckBox vCheckBox;
        ImageView vOverflowMenu;

        public MovieViewHolder(View v) {
                super(v);
            vIcon = (ImageView)v.findViewById(R.id.movieIcon);
            vTitle = (TextView)v.findViewById(R.id.movieTitle);
            vDescription = (TextView)v.findViewById(R.id.movieDescription);
            //vCheckBox = (CheckBox)v.findViewById(R.id.checkBox);
            vOverflowMenu = (ImageView)v.findViewById(R.id.pop_up_menu);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vIcon.setTransitionName(vTitle.getText().toString());
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(vIcon, getAdapterPosition());
                }
            });

            vOverflowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null)
                        mItemClickListener.onOverFlowMenuClick(v, getAdapterPosition());
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemLongClick(v, getPosition());
                    }
                    return true;
                }
            });
        }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
        void onOverFlowMenuClick(View v, int position);
    }

}
