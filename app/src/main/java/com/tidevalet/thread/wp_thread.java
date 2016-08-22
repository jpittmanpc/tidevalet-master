package com.tidevalet.thread;

import android.content.Context;
import android.util.Log;

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

    public wp_thread(String user, String pass, Context context) {
        this.context = context;
        this.user = user;
        this.pass = pass;
    }
    @Override
    public void run() {
        adapter dbAdapter = new adapter(context);
        dbAdapter.open();
        try {
            String url = WebUtils.callWp("wp.getTerms", context);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            dbAdapter.close();
        }

    }
}
