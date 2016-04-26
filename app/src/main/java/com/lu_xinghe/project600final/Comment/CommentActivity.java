package com.lu_xinghe.project600final.Comment;

/**
 * Created by Lu,Xinghe 04/24/2016
 */

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.newsDetails.NewsDetailsActivity;

import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {

    Bundle extra;
    private String userName, url, newsType, realUserName;
    private int position, count;
    Fragment mContent;
    Toolbar mToolBar;
    ActionBar mActionBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Firebase.setAndroidContext(this);
        getInfoFromIntent();

        if (savedInstanceState != null) {//load details fragment
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mContent)
                    .commit();
        }
        else
            checkIfHasCommentAndLoadFragment(savedInstanceState);

        FloatingActionButton commentFab = (FloatingActionButton) findViewById(R.id.fab_comment);
        commentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment();
            }
        });

        monitorAuthentication();
    }

    private void getInfoFromIntent(){
        extra = getIntent().getExtras();
        userName = extra.getString("userName");
        url = extra.getString("url");
        position = extra.getInt("position");
        count = extra.getInt("count");
        newsType = extra.getString("newsType");
        mToolBar = (Toolbar)findViewById(R.id.toolbar_comment);
        setSupportActionBar(mToolBar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Comment");//set label
    }

    private void checkIfHasCommentAndLoadFragment(final Bundle savedInstanceState){
        final Firebase ref = new Firebase(url+"/news"+Integer.toString(position+1)+"/comment");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    loadBlankFavFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, mContent)
                            .commit();
                } else {
                    loadCommentRecycleViewFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, mContent)
                            .commit();
                    ref.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void loadCommentRecycleViewFragment(){
        mContent = CommentRecycleViewFragment.newInstance(url+"/news"+Integer.toString(position+1)+"/comment");
    }

    private void loadBlankFavFragment(){
        mContent = BlankCommentFragment.newInstance();
    }

    private void comment(){//comment dialog pop up
        LayoutInflater layoutInflater = LayoutInflater.from(this);// get comment_prompts.xml view
        View promptView = layoutInflater.inflate(R.layout.comment_prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);// set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(promptView);

        final EditText input = (EditText) promptView.findViewById(R.id.userInput);
        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and push it to cloud
                        String date = (DateFormat.format("MM-dd-yyyy hh:mm:ss", new java.util.Date()).toString());
                        final Firebase userRef = new Firebase(url+"/news"+Integer.toString(position+1));
                        final Firebase commentRef = userRef.child("comment");
                        final Firebase newCommentRef = commentRef.push();
                        Comment comment = new Comment();
                        comment.setDate(date);
                        comment.setUserName(realUserName);
                        //comment.setUserName(userName);
                        comment.setComment(input.getText().toString());
                        newCommentRef.setValue(comment);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);

                /*Intent intent = new Intent(this.getApplicationContext(), NewsDetailsActivity.class);
                //intent.putExtra("newsId", news.getId());
                intent.putExtra("url", url);
                intent.putExtra("count", count);
                intent.putExtra("position", position);
                intent.putExtra("newsType", newsType);
                intent.putExtra("userName", userName);
                // Log.d("url", url);
                startActivity(intent);*/
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        outState.putInt("position", position);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("position");
    }

    private void monitorAuthentication(){
        final Firebase ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    userName = authData.getUid();
                    ref.child(authData.getUid().toString()).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String, String> info = (HashMap<String, String>) dataSnapshot.getValue();
                            realUserName = info.get("userName");
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    ref.removeAuthStateListener(this);
                } else {
                }
            }
        });
    }
}
