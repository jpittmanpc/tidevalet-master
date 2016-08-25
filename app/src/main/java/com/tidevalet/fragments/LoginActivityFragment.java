package com.tidevalet.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.SessionManager;
import com.tidevalet.interfaces.MainListener;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {
    TextView logintext;
    EditText username, password;
    CheckBox remember;
    ProgressDialog progress;

    private MainListener mL;
    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        username = (EditText) v.findViewById(R.id.input_email);
        password = (EditText) v.findViewById(R.id.input_password);
        CheckBox remember = (CheckBox) v.findViewById(R.id.rememberCheckbox);
        remember.setChecked(true);
        Button loginButton = (Button) v.findViewById(R.id.loginButton);
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Log In");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogInfo(v);
                CheckLogIn cL = new CheckLogIn();
                cL.execute(username.getText().toString(), password.getText().toString());
                progress.show();

            }
        });
        return v;
    }
    private void InteractWithAct(int i, String message) {
        switch(i) {
            case 0: progress.setTitle(message);
                if (!progress.isShowing()) { progress.show(); }
                break;
            case 1: if (progress.isShowing()) { progress.dismiss(); } break;

        }
    }

    private void checkLogInfo(View v) {
        if (mL != null) {
            mL.clicked(v);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainListener) {
            mL = (MainListener) context;
        } else {
            throw new ClassCastException(context.toString() + "must implement MainListener");
        }
    }
    @Override
    public void onDetach() {
        mL = null;
        super.onDetach();
    }
    private class CheckLogIn extends AsyncTask<String, Void, String> {
        SessionManager session = new SessionManager(App.getInstance());

       // MainListener mainListener = (MainListener) getActivity();

        private String resp;
        private boolean ERROR = false;

        @Override
        protected void onPostExecute(String values) {
            if (ERROR) { logintext.setText(resp); return; }
            session.setDefUsr(username.getText().toString());
            session.setDefPwd(password.getText().toString());
            session.setLoggedIn(true);
            InteractWithAct(1, "done");
        }
        protected void onPreExecute(String values) {
            super.onPreExecute();
            InteractWithAct(0, "Authenticating");
        }

        protected void onCancelled(String value) {
            super.onCancelled();
            logintext.setText("Cancelled");
            InteractWithAct(1, "Cancelled");
        }

        @Override
        protected String doInBackground(String[] params) {
            XMLRPCClient client = new XMLRPCClient(session.a() + "xmlrpc.php");
            Object[] paramaters = {1, params[0], params[1], true};
            Object result = null;
            try {
                result = client.call("android.auth", paramaters);
                if (result != null) {
                    InteractWithAct(1, "Cancelled");
                    mL.authenticated();
                    session.setUserId(result.toString());
                    //Intent intent = new Intent(App.getInstance(), MainActivity.class);
                    //startActivity(intent);
                }
            } catch (XMLRPCException e) {
                ERROR = true;
                result = " ";
                resp = e.getMessage();
                InteractWithAct(1, "error");
                e.printStackTrace();
            }
            Log.d("RESPONSE FROM SITE", result.toString() + " ");
            return resp;
        }
    }
}