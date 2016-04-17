package com.lu_xinghe.project600final.newsList;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.lu_xinghe.project600final.News;
import com.lu_xinghe.project600final.R;
import com.squareup.picasso.Picasso;


public class NewsListFirebaseRecylerAdapter2 extends FirebaseRecyclerAdapter<News,NewsListFirebaseRecylerAdapter2.NewsViewHolder> {

    private static Context mContext ;
    private static OnItemClickListener mItemClickListener;

    public NewsListFirebaseRecylerAdapter2(Class<News> modelClass, int modelLayout,
                                           Class<NewsViewHolder> holder, Query ref, Context context) {
        super(modelClass, modelLayout, holder, ref);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(NewsViewHolder newsViewHolder, News news, int i) {

        //TODO: Populate viewHolder by setting the movie attributes to cardview fields
        newsViewHolder.vTitle.setText(news.getTitle());
        String date = news.getMonth()+"/"+news.getDate()+"/"+news.getYear();
        newsViewHolder.vDate.setText(date);
        Picasso.with(mContext).load(news.getImage1()).into(newsViewHolder.vIcon);
    }

    //TODO: Populate ViewHolder and add listeners.
    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        ImageView vIcon;
        TextView vTitle;
        TextView vDate;


        public NewsViewHolder(View view) {
                super(view);
            vIcon = (ImageView)view.findViewById(R.id.newsImage);
            vTitle = (TextView)view.findViewById(R.id.newsTitle);
            vDate = (TextView)view.findViewById(R.id.date);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(getAdapterPosition());
                }
            });

            /*vOverflowMenu.setOnClickListener(new View.OnClickListener() {
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
            });*/
        }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        //void onItemLongClick(View view, int position);
        //void onOverFlowMenuClick(View v, int position);
    }

}
