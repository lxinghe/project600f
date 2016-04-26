package com.lu_xinghe.project600final.Authentication;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.lu_xinghe.project600final.R;

/**
 * Created by Lu,Xinghe on 4/25/2016
 */
public class LogInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText emailText,passwordText;
    private String email, password;
    Button logInButton;

    private OnCreateAccountClickListener mListener;

    public LogInFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance() {
        LogInFragment fragment = new LogInFragment();
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
        final View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        TextView createAccount = (TextView)view.findViewById(R.id.link_signup);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnCreateAccountClickListener();
            }
        });

        logInButton = (Button)view.findViewById(R.id.btn_login);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn(view);
            }
        });

        return view;
    }

    private void logIn(View view){
        emailText = (EditText)view.findViewById(R.id.input_email);
        email = emailText.getText().toString();
        passwordText = (EditText)view.findViewById(R.id.input_password);
        password = passwordText.getText().toString();

        final Firebase ref = new Firebase("https://project6000fusers.firebaseio.com/users");
        ref.unauth();
        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                Toast.makeText(getContext(), "Logged in successfully, enjoy!~", Toast.LENGTH_LONG).show();
                /*SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.uid), authData.getUid().toString());
                Log.e("uid: ", "" + authData.getUid());
                editor.commit();*/
                getActivity().onBackPressed();
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreateAccountClickListener) {
            mListener = (OnCreateAccountClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateAccountClickListener");
        }
    }


    public interface OnCreateAccountClickListener {
        // TODO: Update argument type and name
        void OnCreateAccountClickListener();
    }
}
