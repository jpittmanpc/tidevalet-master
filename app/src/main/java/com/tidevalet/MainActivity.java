package com.tidevalet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tidevalet.helpers.Properties;
import com.tidevalet.service.wp_service;
import com.tidevalet.thread.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    static String TAG = "MainActivity";
    ImageView propertyImage;
    TextView propertyName;
    Button newViolation;
    Button viewViolation;
    Button changeProperty;
    private static SessionManager sessionManager = new SessionManager(App.getInstance());
    private static ArrayAdapter listAdapter;
    private static adapter propAdapter = new adapter(App.getInstance());
    private static Map<String, Object> propertyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setContentView(R.layout.activity_main2);
        if (sessionManager.isLoggedIn()) {
            setResult(RESULT_OK);
            if (sessionManager.propertySelected() != 0) {
                startPropertyView();
                Bundle data = null;
                data = getIntent().getExtras();
                if (data != null) {
                    String snackbarContent = data.getString("snackbar");
                    Snackbar.make((CoordinatorLayout) findViewById(R.id.snackbarlocation), snackbarContent, Snackbar.LENGTH_LONG);
                }
            }
            else {
                Intent service = new Intent(this, wp_service.class);
                startService(service);
                propAdapter.open();
                propertyList = propAdapter.getAllProperties();
                propAdapter.close();
                ArrayList listViewProperties = new ArrayList(propertyList.keySet());
                listAdapter = new ArrayAdapter(App.getInstance(), R.layout.listitems,R.id.text1, listViewProperties);
                startListView();
            }
        }
        else { startLogin(); }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
    private void startLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
    private void startPropertyView() {
        LayoutInflater inflater = this.getLayoutInflater();
        ViewGroup container = (ViewGroup) findViewById(R.id.fragment);
        inflater.inflate(R.layout.property_selected, container, false);
        propAdapter.open();
        Properties property = propAdapter.getPropertyById(sessionManager.propertySelected());
        propAdapter.close();
        propertyImage = (ImageView) findViewById(R.id.propertyImg);
        ImageLoader imgLoader = ImageLoader.getInstance();
        imgLoader.displayImage(property.getImage(), propertyImage);
        propertyName = (TextView) findViewById(R.id.propertyName);
        try { propertyName.setText(property.getName()); }
        catch (NullPointerException e) { e.printStackTrace(); }
        newViolation = (Button) findViewById(R.id.newViolation);
        newViolation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("New Violation1", "Click (Start Intent)");
                Intent i = new Intent(App.getAppContext(), ViolationActivity.class);
                startActivity(i);
                finish();
            }
        });
        viewViolation = (Button) findViewById(R.id.viewViolation);
        viewViolation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("View Violations", "Click");
            }
        });
        changeProperty = (Button) findViewById(R.id.changeProperty);
        changeProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setPropertySelected(0);
                Intent listScreen = new Intent(App.getAppContext(), MainActivity.class);
                startActivity(listScreen);
                finish();
            }
        });
    }

    private void startListView() {
        LayoutInflater inflater = this.getLayoutInflater();
        ViewGroup container = (ViewGroup) findViewById(R.id.fragment);
        inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                propAdapter.open();
                HashMap<String, Object> map = (HashMap<String, Object>) propertyList.get(listAdapter.getItem(pos));
                long ids = (long) Integer.valueOf(map.get("property_id").toString());
                Properties property = propAdapter.getPropertyById(ids);
                sessionManager.setPropertySelected(property.getId());
                propAdapter.close();
                Intent propertyScreen = new Intent(App.getAppContext(), MainActivity.class);
                startActivity(propertyScreen);
                finish();
                Log.d(TAG, propertyList.get(listAdapter.getItem(pos).toString()).toString());
                Log.d("MainAct", pos + " pos " + " id: " + id + "name: " + listAdapter.getItem(pos).toString() + " ID:");
            }
        });
    }

}
