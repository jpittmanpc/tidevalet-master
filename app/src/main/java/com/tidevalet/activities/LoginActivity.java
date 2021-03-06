/*package com.tidevalet.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.SessionManager;
import com.tidevalet.activities.MainActivity;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;


public class LoginActivity extends AppCompatActivity {
    TextView username, password, logintext;
    CheckBox remember;
    Button loginButton;
    ProgressDialog progress;
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = App.getAppContext();
        setContentView(R.layout.activity_login);
        SessionManager utils = new SessionManager(this);
        utils.setDefUrl(App.getSiteUrl());
        logintext = (TextView) findViewById(R.id.logintext);
        username = (TextView) findViewById(R.id.editUsername);
        password = (TextView) findViewById(R.id.editPassword);
        remember = (CheckBox) findViewById(R.id.rememberCheckbox);
        remember.setChecked(true);
        loginButton = (Button) findViewById(R.id.loginButton);
        progress = new ProgressDialog(this);
        progress.setTitle("Log In");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: check loginFragment
                logintext.setTextColor(Color.WHITE);
                CheckLogIn checkLogIn = new CheckLogIn();
                checkLogIn.onPreExecute("Authenticating..");

                checkLogIn.execute(username.getText().toString(), password.getText().toString());
            }
        });

    }
       private class CheckLogIn extends AsyncTask<String, Void, String> {
           SessionManager session = new SessionManager(mContext);

           private String resp;
           private boolean ERROR = false;
           @Override
           protected void onPostExecute(String values) {
               progress.dismiss();
               if (ERROR) { logintext.setText(resp); return; }
               if (remember.isChecked()) {
                   session.setDefUsr(username.getText().toString());
                   session.setDefPwd(password.getText().toString());
                   session.setLoggedIn(true);
               }
           }

           protected void onPreExecute(String value) {
               progress.setIndeterminate(true);
               progress.setMessage(value);
               progress.show();
           }
           protected void onCancelled(String value) {
               logintext.setText("Cancelled");
               progress.dismiss();
           }

           @Override
            protected String doInBackground(String[] params) {
               progress.setIndeterminate(true);
               progress.setMessage("Authenticating..");
               progress.show();

                XMLRPCClient client = new XMLRPCClient(session.getDefUrl() + "xmlrpc.php");
                Object[] paramaters = { 1, params[0], params[1], true };
                try {
                    Object result = client.call("android.auth", paramaters);
                    if (result != null) {
                        Intent intent = new Intent(App.getInstance(), MainActivity.class);
                        startActivity(intent);
                    }
                } catch (XMLRPCException e) {
                    ERROR = true;
                    resp = e.getMessage();
                    e.printStackTrace();
                }
                return resp;
            }
        }

}

                /*if (iE != -1) {
                    ((TextView) v.findViewById(R.id.dateTxt)).setText(post.getTimestamp());
                    ((EditText) v.findViewById(R.id.bldg)).setText(post.getBldg());
                    ((EditText) v.findViewById(R.id.unit)).setText(post.getUnit());
                    List<ImageView> x = new ArrayList<ImageView>();
                    x.add((ImageView) v.findViewById(R.id.img1));x.add((ImageView) v.findViewById(R.id.img2));x.add((ImageView) v.findViewById(R.id.img3));x.add((ImageView) v.findViewById(R.id.img4));
                    String[] imagePath = post.getLocalImagePath().split(",");
                    ImageLoader imgLoader = ImageLoader.getInstance();
                    ImageSize size = new ImageSize(60, 60);
                    int i = imagePath.length;
                    for (int j = 0; j < i; j++) {
                        uriList.add(imagePath[j]);
                        imgLoader.displayImage(imagePath[j].replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ",""), x.get(j), size);
                    }
                }*/
  /* if (iE != -1) {
                    String[] violType = post.getViolationType().split(",");
                    for (int i=0; i<violType.length;i++) {
                        ViewGroup left = (ViewGroup) v.findViewById(R.id.left);
                        ViewGroup right = (ViewGroup) v.findViewById(R.id.right);
                        for (int z = 0; z < left.getChildCount(); z++) {
                            if (left.getChildAt(i) instanceof CheckBox) {
                                CheckBox cb = (CheckBox) left.getChildAt(z);
                                if (Objects.equals(cb.getText(),violType[i])) {
                                    cb.setChecked(true);
                                }
                            }
                        }
                        for (int z = 0; z < right.getChildCount(); z++) {
                            if (left.getChildAt(i) instanceof CheckBox) {
                                CheckBox cb = (CheckBox) right.getChildAt(z);
                                Log.d("test", cb.getText() + " " + violType[i]);
                                if (Objects.equals(cb.getText(),violType[i])) {
                                    cb.setChecked(true);
                                }
                            }
                        }
                    }*/
               /* ((EditText)v.findViewById(R.id.contractorComments)).setText(post.getContractorComments());*/