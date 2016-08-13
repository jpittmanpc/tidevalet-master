package com.tidevalet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.tidevalet.SessionManager;
import com.tidevalet.thread.wp_thread;

/**
 * Created by admin on 8/6/2016.
 */
public class wp_service extends Service {

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        SessionManager session = new SessionManager(this);
        String user = session.getUsername();
        String pass = session.getPassword();
        wp_thread thread = new wp_thread(user, pass, this);
        thread.start();
    }

}
