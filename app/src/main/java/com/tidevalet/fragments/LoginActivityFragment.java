package com.tidevalet.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.SessionManager;
import com.tidevalet.interfaces.MainListener;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {
    public TextView logintext;
    EditText username, password;
    CheckBox remember;
    ProgressDialog progress;
    View root;
    private MainListener mL;
    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_login, container, false);
        final TextView errorMsg = (TextView) root.findViewById(R.id.logintext);
        final ScrollView scrollview = ((ScrollView) root.findViewById(R.id.scroller));
        final View fragScrollView = root.findViewById(R.id.scroller);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                fragScrollView.getWindowVisibleDisplayFrame(r);

                int heightDiff = fragScrollView.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 100) {
                    scrollview.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.scrollTo(0, scrollview.getBottom());
                        }
                    });
                }
            }
        });
        username = (EditText) root.findViewById(R.id.input_email);
        password = (EditText) root.findViewById(R.id.input_password);
        logintext = (TextView) root.findViewById(R.id.logintext);
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!errorMsg.getText().equals("")) { errorMsg.setText(""); }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!errorMsg.getText().equals("")) { errorMsg.setText(""); }
            }
        });
        remember = (CheckBox) root.findViewById(R.id.rememberCheckbox);
        remember.setChecked(true);
        Button loginButton = (Button) root.findViewById(R.id.loginButton);
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Log In");
        progress.setMessage("Authenticating...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogInfo(v);
                CheckLogIn cL = new CheckLogIn();
                cL.execute(username.getText().toString(), password.getText().toString());
                InputMethodManager imm = (InputMethodManager) App.getAppContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                progress.show();

            }
        });

        return root;
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
        //MainListener mainListener = (MainListener) getActivity();
        private String resp;
        private boolean ERROR = false;

        @Override
        protected void onPostExecute(String values) {
            if (ERROR) {
                InteractWithAct(1, "done");
                if (progress.isShowing()) { progress.dismiss(); }
                logintext = (TextView) root.findViewById(R.id.logintext);
                resp = resp.split(":")[1];
                resp = resp.replaceAll("\\[.*?\\] ?", "");
                logintext.setText(resp);
                logintext.setTextColor(Color.GREEN);
                return;
            }
            else {
                session.setDefUsr(username.getText().toString());
                session.setDefPwd(password.getText().toString());
                session.setLoggedIn(true);
                InteractWithAct(1, "done");
            }
            super.onPostExecute(values);
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
            Object[] parameters = {1, params[0], params[1], true};
            Object result = null;
            try {
                result = client.call("android.auth", parameters);
                if (result != null) {
                    InteractWithAct(1, "Cancelled");
                    HashMap<String, String> hash = (HashMap<String, String>) result;
                    String id = String.valueOf(hash.get("ID"));
                    String role = hash.get("ROLE");
                    session.setUserId(id);
                    session.setRole(role);
                    mL.authenticated();

                }
            } catch (XMLRPCException e) {
                ERROR = true;
                resp = e.getMessage();
                InteractWithAct(1, "error");
                e.printStackTrace();
            }
            return resp;
        }
    }
}