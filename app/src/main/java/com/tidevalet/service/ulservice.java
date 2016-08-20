package com.tidevalet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.tidevalet.thread.upload;

/**
 * Created by Justin on 4/22/16.
 */
public class ulservice extends Service {

        public IBinder onBind(Intent arg0) {
            return null;
        }

        @Override
        public void onStart(Intent intent, int startId) {
            super.onStart(intent, startId);
try {
    long postId = intent.getLongExtra("id", 0);
    upload thread = new upload(postId, this);
    thread.start();
    Toast.makeText(this, "Starting", Toast.LENGTH_LONG).show();
}
catch (NullPointerException e) { e.printStackTrace(); }
        }

    }

