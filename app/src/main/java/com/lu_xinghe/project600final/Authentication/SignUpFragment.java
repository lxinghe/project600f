package com.lu_xinghe.project600final.Authentication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.api.Auth;
import com.lu_xinghe.project600final.R;

import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView signIn;
    EditText userNameText, emailText, passwordText;
    String userUrl = "https://project6000fusers.firebaseio.com/users";
    String userName, email, password;
    Button createAccount;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnSignInClickListener mListener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
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
        final View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        signIn = (TextView)view.findViewById(R.id.link_login);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnSignInClickListener();
            }
        });

        createAccount = (Button)view.findViewById(R.id.btn_signup);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(view);
            }
        });

        return view;
    }

    private void createAccount(View view){
        userNameText = (EditText)view.findViewById(R.id.input_user_name);
        userName = userNameText.getText().toString();
        emailText = (EditText)view.findViewById(R.id.input_email);
        email = emailText.getText().toString();
        passwordText = (EditText)view.findViewById(R.id.input_password);
        password = passwordText.getText().toString();
        final Firebase ref = new Firebase(userUrl);
        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                HashMap<String, String> userInfo = new HashMap();
                //AuthData authData = ref.getAuth();
                userInfo.put("userName", userName);
                userInfo.put("email", email);
                userInfo.put("about","");
                userInfo.put("major","");
                userInfo.put("mood","");
                //userInfo.put("profileImageURL",(String)authData.getProviderData().get("profileImageURL"));
                Log.e("userName", userInfo.get("userName"));
                ref.child(result.get("uid").toString()).child("info").setValue(userInfo);
                Toast.makeText(getContext(), "Your account is created successfully.", Toast.LENGTH_LONG).show();
                mListener.OnSignInClickListener();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                Toast.makeText(getContext(), firebaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignInClickListener) {
            mListener = (OnSignInClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSignInClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSignInClickListener {
        // TODO: Update argument type and name
        void OnSignInClickListener();
    }
}
