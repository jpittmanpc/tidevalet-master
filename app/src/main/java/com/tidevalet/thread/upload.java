
package com.tidevalet.thread;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.helpers.Attributes;
import com.tidevalet.helpers.Post;
import com.tidevalet.helpers.Properties;

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
            Properties property = dbAdapter.getPropertyById(post.getViolationId());
            Attributes service = dbAdapter.getViolationByPropertyId(post.getViolationId(),
                    Attributes.TYPE_PRIMARYBLOGGER);
            dbAdapter.close();
            //if (service.getIsEnabled() == Attributes.SERVICE_ENABLED) {
                dbAdapter.open();
                String url = WebUtils.uploadPostToWordpress(property, post.getLocalImagePath(), ""
                        + post.getViolationType(), post.getBldg(), post.getUnit(), service, context);
                post.setIsPosted(1);
                post.setReturnedString(url);
                dbAdapter.updatePost(post);
                dbAdapter.close();
            //} else {
                post.setReturnedString("");
          //  }
            //dbAdapter.open();
          //  service = dbAdapter.getViolationByPropertyId(post.getViolationId(),
            //        Attributes.TYPE_XPARENA);
            //dbAdapter.close();
        //    if (service.getIsEnabled() == Attributes.SERVICE_ENABLED) {
                //WebUtils.uploadPostToXPArena(service, pupil, post.getReturnedString(), "500");
          //  }
        } catch (Exception e) {
            e.printStackTrace();
            NotificationManager nManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, App.class);
            intent.putExtra("id", postId);
            PendingIntent pendingIntent = PendingIntent
                    .getService(context, (int) postId, intent, 0);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification
                    .setSmallIcon(R.drawable.ms_circle)
                    .setContentTitle("Error Uploading")
                    .setContentText("Try again?");
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent((int) (R.string.app_name + postId), PendingIntent.FLAG_UPDATE_CURRENT);
            taskStackBuilder.addNextIntent(intent);
            notification.setContentIntent(resultPendingIntent);
            nManager.notify((int) (R.string.app_name + postId), notification.build());
        } finally {
            dbAdapter.close();
        }
    }
}
