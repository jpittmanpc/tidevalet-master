package com.tidevalet;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

/**
 * Created by Justin on 4/27/16.
 */
public class TakePic extends Activity {

    private ImageView image;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_pic);
        initializeUIElements();

//		WebUtils.uploadPostToWordpress();
//		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//		startActivityForResult(intent, 0);
    }

    private void initializeUIElements() {
        image = (ImageView) findViewById(R.id.ImageView01);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(data.getDataString());
        Cursor cursor = managedQuery(Uri.parse(data.getDataString()), null,
                null, null, null);
        cursor.moveToNext();
        // Retrieve the path and the mime type
        String path = cursor.getString(cursor
                .getColumnIndex(MediaStore.MediaColumns.DATA));
        String mimeType = cursor.getString(cursor
                .getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
        Drawable drawable = Drawable.createFromPath(path);
        image.setImageDrawable(drawable);
    }

}
