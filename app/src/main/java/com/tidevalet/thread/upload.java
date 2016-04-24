
package com.tidevalet.thread;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.tidevalet.R;
import com.tidevalet.helpers.Post;
import com.tidevalet.helpers.Attributes;

import org.xmlrpc.android.WebUtils;

public class upload extends Thread {
    private long postId;
    public String TAG = "Testing";

    private Context context;

    public upload(long postId, Context context) {
        this.postId = postId;
        this.context = context;

    }

    @Override
    public void run() {
        adapter dbAdapter = new adapter(context);
        try {
            NotificationManager nManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.cancel((int) (R.string.app_name + postId));

            dbAdapter.open();
            Log.e(TAG, "Adapter opened");
            Post post = dbAdapter.getPostById(postId);
            Pupil pupil = dbAdapter.getPupilById(post.getPupilId());
            Attributes service = dbAdapter.getPupilServiceByPupilId(post.getPupilId(),
                    Attributes.TYPE_PRIMARYBLOGGER);
            dbAdapter.close();
            if (service.getIsEnabled() == Attributes.SERVICE_ENABLED) {
                dbAdapter.open();
                String url = WebUtils.uploadPostToWordpress(pupil, post.getLocalImagePath(), ""
                        + post.getGrade(), service, context);
                post.setIsPosted(1);
                post.setReturnedString(url);
                dbAdapter.updatePost(post);
                dbAdapter.close();
            } else {
                post.setReturnedString("");
            }
            dbAdapter.open();
            service = dbAdapter.getPupilServiceByPupilId(post.getPupilId(),
                    Attributes.TYPE_XPARENA);
            dbAdapter.close();
            if (service.getIsEnabled() == Attributes.SERVICE_ENABLED) {
                WebUtils.uploadPostToXPArena(service, pupil, post.getReturnedString(), "500");
            }
        } catch (Exception e) {
            e.printStackTrace();
            NotificationManager nManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.cancel(R.string.app_name);
            Intent intent = new Intent(context, ClassdroidService.class);
            intent.putExtra("id", postId);
            PendingIntent pendingIntent = PendingIntent
                    .getService(context, (int) postId, intent, 0);
            Notification notification = new Notification(R.drawable.icon, context
                    .getString(R.string.lab_classdroid_error), System.currentTimeMillis());
            notification.setLatestEventInfo(context, "failed",
                    "Try again?", pendingIntent);
            nManager.notify((int) (R.string.app_name + postId), notification);
        } finally {
            dbAdapter.close();
        }
    }
}
