package com.tidevalet.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.SessionManager;
import com.tidevalet.fragments.LoginActivityFragment;
import com.tidevalet.fragments.PropertyChosen;
import com.tidevalet.fragments.PropertyList;
import com.tidevalet.helpers.Attributes;
import com.tidevalet.helpers.Properties;
import com.tidevalet.interfaces.MainListener;
import com.tidevalet.service.wp_service;
import com.tidevalet.thread.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MainListener {

    private static final String TAG = "MainActivity";
    private static SessionManager sM = new SessionManager(App.getInstance());
    final FragmentManager fm = getSupportFragmentManager();
    ImageView propertyImage;
    TextView propertyName;
    Button newViolation;
    Button viewViolation;
    Button changeProperty;

    private static Context mContext;
    public static final Attributes attributes = new Attributes();
    private static ArrayAdapter<Object> listAdapter;
    private static adapter propAdapter = new adapter(App.getInstance());
    private static Map<String, Object> propertyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        sM.setDefUrl(App.getSiteUrl());
        InitializeUI();

    }


    private void InitializeUI() {
        if (sM.isLoggedIn()) { propUI();  }
        if (!sM.isLoggedIn()) { loginUI(); }
    }

    private void loginUI() {
        LoginActivityFragment LAF = new LoginActivityFragment();
        fm.beginTransaction().replace(R.id.main_fragment, LAF).commit();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }



    private void propUI() {
        if (sM.propertySelected() == 0) {
            startListView();
        }
        if (sM.propertySelected() != 0) {
            startPropView();
        }
    }
    private void startPropView() {
        Fragment propertyChosenFragment = new PropertyChosen();
        fm.beginTransaction().replace(R.id.main_fragment, propertyChosenFragment).commit();
        fm.popBackStack();

    }
    @Override
    public void propertyView(View v) {
        propAdapter.open();
        Properties property = propAdapter.getPropertyById(sM.propertySelected());
        propAdapter.close();
        propertyImage = (ImageView) v.findViewById(R.id.propertyImg);
        ImageLoader imgLoader = ImageLoader.getInstance();
        Log.d("TAG", sM.propertySelected() + "");
        attributes.setPropertyId(sM.propertySelected());
        imgLoader.displayImage(property.getImage(), propertyImage);
        propertyName = (TextView) v.findViewById(R.id.propertyName);
        try {
            propertyName.setText(property.getName());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        newViolation = (Button) v.findViewById(R.id.newViolation);
        newViolation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("New Violation", "Click (Start Intent)");
                Intent i = new Intent(App.getAppContext(), ViolationActivity.class);
                startActivity(i);
                finish();
            }
        });
        viewViolation = (Button) v.findViewById(R.id.viewViolation);
        viewViolation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("View Violations", "Click");
            }
        });
        changeProperty = (Button) v.findViewById(R.id.changeProperty);
        changeProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sM.setPropertySelected(0);
                startListView();
            }
        });
    }

    private void startListView() {
        Fragment listPropertyFragment = new PropertyList();
        fm.beginTransaction().replace(R.id.main_fragment, listPropertyFragment).commit();
        fm.popBackStack();

    }
    @Override
    public void propertyList(View v) {
        propAdapter.open();
        propertyList = propAdapter.getAllProperties();
        propAdapter.close();
        Log.d(TAG, sM.noProperties() + " ");
        if (propertyList.keySet().isEmpty()) {
            sM.setNoProperties(true);
        }
        Intent service = new Intent(this, wp_service.class);
        startService(service);
        propAdapter.open();
        propertyList = propAdapter.getAllProperties();
        propAdapter.close();

        @SuppressWarnings("unchecked")
        ArrayList<Object> listViewProperties = new ArrayList<Object>(propertyList.keySet());
        if (listViewProperties.isEmpty()) {
            sendBroadcast();
            setView(v);
            ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
        else { setView(v); ListAdapterStart(); }
    }
    private View listViewHolder;
    private void setView(View v) {
        this.listViewHolder = v;
    }
    private View getView() {
        return listViewHolder;
    }
    private void ListAdapterStart() {
        propAdapter.open();
        propertyList = propAdapter.getAllProperties();
        propAdapter.close();

        @SuppressWarnings("unchecked")
        ArrayList<Object> listViewProperties = new ArrayList<Object>(propertyList.keySet());
        if (listViewProperties.isEmpty()) {
            listViewProperties.add("There is no properties.");
        }
        View v = getView();
        v.findViewById(R.id.progressBar).setVisibility(View.GONE);
        listAdapter = new ArrayAdapter<Object>(App.getInstance(), R.layout.listitems, R.id.text1, listViewProperties);
        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                propAdapter.open();
                @SuppressWarnings("unchecked") HashMap<String, Object> map = (HashMap<String, Object>) propertyList.get(listAdapter.getItem(pos));
                long ids = (long) Integer.valueOf(map.get("property_id").toString());
                Properties property = propAdapter.getPropertyById(ids);
                if (!property.isContractor()) {
                    Snackbar snackbar = Snackbar
                            .make(view, "You are not a contractor for this property.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                sM.setPropertySelected(property.getId());
                propAdapter.close();
                startPropView();
                Log.d(TAG, propertyList.get(listAdapter.getItem(pos).toString()).toString());
                Log.d("MainAct", pos + " pos " + " id: " + id + "name: " + listAdapter.getItem(pos).toString() + " ID:");
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void clicked(View v) {

    }
    @Override
    public void showDialog(int i, String value) {
        ProgressDialog progressDialog = new ProgressDialog(App.getInstance());
        switch (i) {
            case 0:
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Logging In");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage(value);
                progressDialog.show();
                break;
            case 1:
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    break;
                }
        }
    }

    @Override
    public void authenticated() {
        startListView();
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            propAdapter.open();
            propertyList = propAdapter.getAllProperties();
            propAdapter.close();
            ListAdapterStart();
        }
    };
    private void sendBroadcast() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("updateListView"));
    }
}