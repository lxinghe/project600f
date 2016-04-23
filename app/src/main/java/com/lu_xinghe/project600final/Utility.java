package com.lu_xinghe.project600final;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lu_xinghe.project600final.newsPage.News;

import java.util.HashMap;

/**
 * Created by Lu,Xinghe on 4/22/2016.
 */
public class Utility {

    public static News setFavNews(HashMap<String, String> news){//set and return a favorite news
        News favNews = new News();
        favNews.setId(news.get("id"));
        favNews.setTitle(news.get("title"));
        favNews.setAuthor(news.get("author"));
        favNews.setDate(news.get("date"));
        favNews.setMonth(news.get("month"));
        favNews.setYear(news.get("year"));
        favNews.setImage1(news.get("image1"));
        favNews.setImageDescription1(news.get("imageDescription1"));
        favNews.setSubtitle1(news.get("subtitle1"));
        favNews.setArticle1(news.get("article1"));
        favNews.setImage2(news.get("image2"));
        favNews.setImageDescription2(news.get("imageDescription2"));
        favNews.setSubtitle2(news.get("subtitle2"));
        favNews.setArticle2(news.get("article2"));
        favNews.setNewsType(news.get("newsType"));

        return favNews;
    }

    public static void addFav(String url, final String userName, int position){//add favorite to database
        final Firebase ref = new Firebase(url);
        String newsId = "news"+Integer.toString(position+1);
        ref.child(newsId).addListenerForSingleValueEvent(new ValueEventListener() {//get data from database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final HashMap<String, String> news = (HashMap<String, String>) dataSnapshot.getValue();
                final Firebase userRef = new Firebase("https://project6000fusers.firebaseio.com/users/"+userName);
                final Firebase favRef = userRef.child("favorites");
                final Firebase newFavRef = favRef.push();//user push to generate unique key value

                favRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        News favNews = Utility.setFavNews(news);
                        newFavRef.setValue(favNews);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {}
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public static void deleteFav(final String userName, final String newsType, final int position){
        final Firebase userRef = new Firebase("https://project6000fusers.firebaseio.com/users/"+userName);
        final Firebase favRef = userRef.child("favorites");
        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                    for (DataSnapshot favSnapshot: dataSnapshot.getChildren()) {
                        News favNews = favSnapshot.getValue(News.class);
                        if(favNews.getNewsType().equals(newsType)){
                            if(Integer.parseInt(favNews.getId())-1==position){
                                favRef.child(favSnapshot.getKey()).removeValue();
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

}
