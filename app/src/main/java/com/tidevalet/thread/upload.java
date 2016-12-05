
package com.tidevalet.thread;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tidevalet.R;
import com.tidevalet.helpers.Post;

import org.xmlrpc.android.WebUtils;

import java.util.HashMap;

public class upload extends Thread {
    private long postId;
    private Context context;
    private String type;
    public upload(long postId, Context context, String type) {
        this.postId = postId;
        this.context = context;
        this.type = type;
    }

    @Override
    public void run() {
        adapter dbAdapter = new adapter(context);
        dbAdapter.open();
        Post post = null;
        try {
            NotificationManager nManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.cancel((int) (R.string.app_name + postId));
            post = dbAdapter.getPostById(postId);
            if (type.equals("editPost")) { String rez =
                    WebUtils.editPost(post.getContractorComments(), postId, context);
            Log.d("TAG", rez.toString()); }
            else {
                HashMap<String, String> pair = WebUtils.uploadPostToWordpress(
                        post.getLocalImagePath(),
                        "" + post.getViolationType(),
                        post.getBldg(),
                        post.getUnit(),
                        post.getContractorComments(),
                        post.getPU(),
                        type,
                        context, post.getId());
                post.setIsPosted(1);
                post.setReturnedString(pair.get("url"));
                post.setImagePath(pair.get("images"));
                post.setViolationId(Integer.valueOf(pair.get("violation_id")));
                dbAdapter.updatePost(post);
            }
        } catch (Exception e){
                if (post != null) {
                    post.setIsPosted(0);
                    post.setReturnedString("");
                    dbAdapter.updatePost(post);
                }
                e.printStackTrace();
                NotificationManager nManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(context, upload.class);
                intent.putExtra("id", postId);
                intent.putExtra("type", type);
                PendingIntent pendingIntent = PendingIntent
                        .getService(context, (int) (R.string.app_name + postId), intent, 0);
                NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.logotide_logo)
                        .setContentTitle("Tide Valet")
                        .setContentText("Error uploading post " + post.getBldg() + "/" + post.getUnit());
                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                taskStackBuilder.addNextIntent(intent);
                PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent((int) (R.string.app_name + postId), PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(resultPendingIntent);
                nManager.notify((int) (R.string.app_name + postId), notification.build());
        } finally {
            dbAdapter.close();
        }
    }
}
