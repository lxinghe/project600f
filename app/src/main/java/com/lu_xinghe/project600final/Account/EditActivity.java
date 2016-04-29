package com.lu_xinghe.project600final.Account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.lu_xinghe.project600final.Authentication.AuthenticationActivity;
import com.lu_xinghe.project600final.Comment.Comment;
import com.lu_xinghe.project600final.R;
import com.lu_xinghe.project600final.newsPage.NewsPageActivity;

public class EditActivity extends AppCompatActivity
{
    Firebase ref;
    String oldEmail, oldPassword, newUserName, newAbout, newMajor, newStatus, newEmail,newPassword, uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Firebase.setAndroidContext(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        Button applyChanges = (Button)findViewById(R.id.btn_apply);
        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        TextView cancel = (TextView)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void applyChanges(){//comment dialog pop up
        LayoutInflater layoutInflater = LayoutInflater.from(this);// get comment_prompts.xml view
        View promptView = layoutInflater.inflate(R.layout.authentication_prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);// set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(promptView);

        final EditText emailInput = (EditText) promptView.findViewById(R.id.email_prompt);
        final EditText passwordInput = (EditText) promptView.findViewById(R.id.password_prompt);
        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and push it to cloud
                        AuthData authData = ref.getAuth();
                        if (authData != null) {
                            uid = authData.getUid();
                            oldEmail = (String) authData.getProviderData().get("email");
                            oldPassword = passwordInput.getText().toString();
                            if (emailInput.getText().toString().equals(oldEmail))
                                verifyPassword(oldPassword);
                            else
                                Toast.makeText(getApplicationContext(), "Verification failed: invalid Email address.", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Please log in first.", Toast.LENGTH_LONG).show();

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

    private void verifyPassword(String oldPassword){
        final Firebase ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        ref.authWithPassword(oldEmail, oldPassword.toString(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Toast.makeText(getApplicationContext(), "Authentication verified", Toast.LENGTH_LONG).show();
                Firebase userInfoRef = ref.child(uid).child("info");
                final EditText userNameInput = (EditText) findViewById(R.id.input_user_name);
                newUserName = userNameInput.getText().toString();
                final EditText aboutInput = (EditText) findViewById(R.id.input_about);
                newAbout = aboutInput.getText().toString();
                final EditText majorInput = (EditText) findViewById(R.id.input_major);
                newMajor = majorInput.getText().toString();
                final EditText statusInput = (EditText) findViewById(R.id.input_status);
                newStatus = statusInput.getText().toString();
                final EditText emailInput = (EditText) findViewById(R.id.input_email);
                newEmail = emailInput.getText().toString();
                final EditText passwordInput = (EditText) findViewById(R.id.input_password);
                newPassword = passwordInput.getText().toString();
                if (!newUserName.equals(""))
                    userInfoRef.child("userName").setValue(newUserName);
                if (!newAbout.equals(""))
                    userInfoRef.child("about").setValue(newAbout);
                if (!newMajor.equals(""))
                    userInfoRef.child("major").setValue(newMajor);
                if (!newStatus.equals(""))
                    userInfoRef.child("mood").setValue(newStatus);
                if (!newEmail.equals(""))
                    changeEmailAddress(newPassword);
                else if (!newPassword.equals(""))
                    changePassword(oldEmail);
                /*Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);*/
                //finish();
                onBackPressed();
                /*Intent local = new Intent();
                local.setAction("com.example.CUSTOM_INTENT");
                sendBroadcast(local);*/
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
                Toast.makeText(getApplicationContext(), "Verification failed: invalid password", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void changeEmailAddress(final String newPassword){
        //final Firebase ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        ref.changeEmail(oldEmail, oldPassword, newEmail, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // email changed
                Firebase userInfoRef = new Firebase("https://project6000fusers.firebaseio.com/users" + uid);
                userInfoRef.child("email").setValue(newPassword);
                AuthData authData = ref.getAuth();
                if (authData != null) {
                    String profileImageUrl = (String) authData.getProviderData().get("profileImageURL");
                    userInfoRef.child("profileImageURL").setValue(profileImageUrl);
                }
                if (!newPassword.equals("")) {
                    changePassword(newEmail);
                } else {
                    Toast.makeText(getApplicationContext(), "Authentication info modified, please re-login", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
            }
        });
    }

    private void changePassword(String email){
        final Firebase ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        ref.changePassword(email, oldPassword, newPassword, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password changed
                Log.e("Authentication", "updated!");
                Toast.makeText(getApplicationContext(), "Authentication info modified, please re-login", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
            }
        });
    }


}
