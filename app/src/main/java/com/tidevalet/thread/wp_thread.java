package com.tidevalet.thread;

import android.content.Context;
import android.util.Log;

import com.tidevalet.App;
import com.tidevalet.SessionManager;
import com.tidevalet.service.wp_service;

import org.xmlrpc.android.WebUtils;

/**
 * Created by admin on 8/5/2016.
 */
public class wp_thread extends Thread {
    private Context context;
    private String user;
    private String pass;
    private Object result;
    private String TAG = "wp_thread";
    private int ID = 0;

    public wp_thread(String user, String pass, Context context, long ID) {
        this.context = context;
        this.user = user;
        this.pass = pass;
        this.ID = (int) ID;
    }
    @Override
    public void run() {
        adapter dbAdapter = new adapter(context);
        dbAdapter.open();
        try {
            if (ID == 0) {
            String url = WebUtils.callWp("wp.getTerms", context); }
            else { String ha = WebUtils.getPosts(ID); }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            dbAdapter.close();
        }

    }
}
