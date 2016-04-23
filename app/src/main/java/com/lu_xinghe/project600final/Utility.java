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

    public static News setFavNews(HashMap<String, String> news, int favCount){//set and return a favorite news
        News favNews = new News();
        favNews.setId("fav" + Integer.toString(favCount+1));
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

        return favNews;
    }


}
