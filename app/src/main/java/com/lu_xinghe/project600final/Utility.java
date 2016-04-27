package com.lu_xinghe.project600final;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lu_xinghe.project600final.Favorites.FavDetails.FavDetailsActivity;
import com.lu_xinghe.project600final.Favorites.FavDetails.News2;
import com.lu_xinghe.project600final.Favorites.FavoritesActivity;
import com.lu_xinghe.project600final.newsPage.News;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by Lu,Xinghe on 4/22/2016.
 * Purpose of this class:
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
                final Firebase userRef = new Firebase("https://project6000fusers.firebaseio.com/users/" + userName);
                final Firebase favRef = userRef.child("favorites");
                final Firebase newFavRef = favRef.push();//user push to generate unique key value

                favRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //News favNews = Utility.setFavNews(news);
                        news.put("select","false");
                        news.remove("comment");
                        newFavRef.setValue(news);
                        //newFavRef
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }

    public static void deleteFav(final String userName, final String newsType, final int position){//delete favorite from database
        final Firebase userRef = new Firebase("https://project6000fusers.firebaseio.com/users/"+userName);
        final Firebase favRef = userRef.child("favorites");
        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                    for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {//loop through all kids
                        News favNews = favSnapshot.getValue(News.class);
                        if (favNews.getNewsType().equals(newsType)) {
                            if (Integer.parseInt(favNews.getId()) - 1 == position) {
                                favRef.child(favSnapshot.getKey()).removeValue();
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }

    public static void deleteFav2(final String userName, final int position){//delete favorite from database
        final Firebase userRef = new Firebase("https://project6000fusers.firebaseio.com/users/"+userName);
        final Firebase favRef = userRef.child("favorites");
        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                if(dataSnapshot.exists()){
                    //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                    for (DataSnapshot favSnapshot: dataSnapshot.getChildren()) {//loop through all kids
                        if(counter==position){
                            favRef.child(favSnapshot.getKey()).removeValue();
                            break;
                        }
                        counter++;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {Log.e("The read failed: " ,firebaseError.getMessage());}
        });
    }

    public static String getUserId(){
        Firebase ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        AuthData authData = ref.getAuth();
        if (authData != null) {
            // user authenticated
            return authData.getUid();
        } else {
            // no user authenticated
            return "stranger";
        }
    }

    public static Bitmap downloadImageusingHTTPGetRequest(String urlString) {
        Bitmap image=null, line;

        try {
            URL url = new URL(urlString);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = httpConnection.getInputStream();
                image = getImagefromStream(stream);
            }
            httpConnection.disconnect();
        }  catch (UnknownHostException e1) {
            Log.d("MyDebugMsg", "UnknownHostexception in sendHttpGetRequest");
            e1.printStackTrace();
        } catch (Exception ex) {
            Log.d("MyDebugMsg", "Exception in sendHttpGetRequest");
            ex.printStackTrace();
        }
        return image;
    }

    private static Bitmap getImagefromStream(InputStream stream) {
        Bitmap bitmap = null;
        if(stream!=null) {
            bitmap = BitmapFactory.decodeStream(stream);
            try {
                stream.close();
            }catch (IOException e1) {
                Log.d("MyDebugMsg", "IOException in getImagefromStream()");
                e1.printStackTrace();
            }
        }
        return bitmap;
    }

    public static void changeSelect(Firebase userRef, String uid, final int position, final boolean isChecked){
        final Firebase fav = userRef.child(uid).child("favorites");
        fav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {//loop through all kids
                    //News favNews = favSnapshot.getValue(News.class);
                    String select;
                    if(isChecked==true)
                        select = "true";
                    else
                        select = "false";
                    if (counter == position) {
                        fav.child(favSnapshot.getKey()).child("select").setValue(select);
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

    public static void selectAll(Firebase userRef, String uid){
        final Firebase fav = userRef.child(uid).child("favorites");
        fav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {//loop through all kids
                    fav.child(favSnapshot.getKey()).child("select").setValue("true");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void unSelect(Firebase userRef, String uid){
        final Firebase fav = userRef.child(uid).child("favorites");
        fav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {//loop through all kids
                    News2 favNews = favSnapshot.getValue(News2.class);
                    if(favNews.getSelect().equals("true"))
                        fav.child(favSnapshot.getKey()).child("select").setValue("false");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void unSelectAll(Firebase userRef, String uid){
        final Firebase fav = userRef.child(uid).child("favorites");
        fav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {//loop through all kids
                    fav.child(favSnapshot.getKey()).child("select").setValue("false");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void deleteSelectedFav(Firebase userRef, String uid){
        final Firebase fav = userRef.child(uid).child("favorites");
        fav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {//loop through all kids
                    News2 favNews = favSnapshot.getValue(News2.class);
                    if(favNews.getSelect().equals("true"))
                        fav.child(favSnapshot.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
