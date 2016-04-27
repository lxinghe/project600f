package com.lu_xinghe.project600final.Favorites;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.lu_xinghe.project600final.Favorites.FavDetails.News2;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.newsPage.News;
import com.squareup.picasso.Picasso;


public class FavFirebaseRecylerAdapter extends FirebaseRecyclerAdapter<News2,FavFirebaseRecylerAdapter.NewsViewHolder> {

    private static Context mContext ;
    private static OnItemClickListener mItemClickListener;
    Firebase userRef;

    public FavFirebaseRecylerAdapter(Class<News2> modelClass, int modelLayout,
                                     Class<NewsViewHolder> holder, Query ref, Context context) {
        super(modelClass, modelLayout, holder, ref);
        this.mContext = context;
        this.userRef = userRef;
    }

    @Override
    protected void populateViewHolder(NewsViewHolder newsViewHolder, News2 news, int i) {

        //TODO: Populate viewHolder by setting the movie attributes to cardview fields
        newsViewHolder.vTitle.setText(news.getTitle());
        String date = news.getMonth()+"/"+news.getDate()+"/"+news.getYear();
        newsViewHolder.vDate.setText(date);
        Picasso.with(mContext).load(news.getImage1()).into(newsViewHolder.vIcon);
        newsViewHolder.newsType = news.getNewsType();
        if(newsViewHolder.vCheckBox!=null){
            if(news.getSelect().equals("false"))
                newsViewHolder.vCheckBox.setChecked(false);
            else
                newsViewHolder.vCheckBox.setChecked(true);
        }

    }

    //TODO: Populate ViewHolder and add listeners.
    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        ImageView vIcon;
        TextView vTitle;
        TextView vDate;
        String newsType;
        ImageView vOverflowMenu;
        CheckBox vCheckBox;


        public NewsViewHolder(View view) {
                super(view);
            vIcon = (ImageView)view.findViewById(R.id.newsImage);
            vTitle = (TextView)view.findViewById(R.id.newsTitle);
            vDate = (TextView)view.findViewById(R.id.date);
            vOverflowMenu = (ImageView)view.findViewById(R.id.pop_up_menu);
            if(view.findViewById(R.id.checkBox)!=null)
                vCheckBox=(CheckBox)view.findViewById(R.id.checkBox);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(getAdapterPosition(), newsType);
                }
            });

            vOverflowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null)
                        mItemClickListener.onOverFlowMenuClick(v, getAdapterPosition());
                }
            });

            vCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null)
                        mItemClickListener.OnCheckBoxClickListener(getAdapterPosition(), vCheckBox.isChecked());
                    Log.e("check",""+vCheckBox.isChecked());
                }
            });
        }
    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position, String newsType);
        void onOverFlowMenuClick(View v, int position);
        void OnCheckBoxClickListener(int position, boolean isChecked);
        //void onItemLongClick(View view, int position);
        //void onOverFlowMenuClick(View v, int position);
    }

}
