package com.tidevalet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by admin on 11/2/2015.
 */
public class ImagePreview extends Activity implements View.OnClickListener {
    TextView continueBtn, cancelBtn;
    ImageView imageView, imageView2, imageView3;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int SELECT_PICTURE = 200;
    private static final String IMAGE_DIRECTORY_NAME = "TideValet";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    Uri fileAttachment;
    private static ImageView getImageView;
    int imageId;
    private String selectedImagePath;
    ArrayList<String> uriList = new ArrayList<String>();
    String latitude, longitude, address, date, time, addDetail;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview);
        imageView =  (ImageView) findViewById(R.id.image1);
        imageView2 = (ImageView) findViewById(R.id.image2);
        imageView3 = (ImageView) findViewById(R.id.image3);
        continueBtn = (TextView) findViewById(R.id.continueBtn);
        cancelBtn = (TextView) findViewById(R.id.cancelBtn);
        imageView.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        continueBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        latitude = user.get(SessionManager.LATITUDE);
        longitude = user.get(SessionManager.LONGITUDE);
        address = user.get(SessionManager.ADDRESS);
        date = user.get(SessionManager.DATE);
        time = user.get(SessionManager.TIME);
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support \'Camera\'",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera or access
            finish();
        }
    }
    public boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }
    private void CaptureImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ImagePreview.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Failed to create: " + IMAGE_DIRECTORY_NAME);
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        previewCaptureImage();
                        fileAttachment = Uri.parse("file://" + fileUri.getPath());
                        uriList.add("file://" + fileUri.getPath());
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(),
                                "Cancelled Image Capture", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }
                break;
        }
        switch (requestCode) {
            case SELECT_PICTURE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Uri selectedImageUri = data.getData();
                        selectedImagePath = getSelectGalleryPath(selectedImageUri);
                        uriList.add("file://" + selectedImagePath);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        int scaleFactor = Math.min(options.outWidth/getImageView.getWidth(), options.outHeight/getImageView.getHeight());
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = scaleFactor;
                        options.inPurgeable = true;
                        final Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
                        getImageView.setImageBitmap(bitmap);
                        break;
                }
                break;
        }

    }

    public String getSelectGalleryPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void previewCaptureImage() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            int targetW = getImageView.getWidth();
            int targetH = getImageView.getHeight();
            int photoW = options.outWidth;
            int photoH = options.outHeight;
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            options.inSampleSize = scaleFactor;
            options.inPurgeable = true;
            //setup date
            Date now = new Date();
            String template = "M/dd/yy      h:mm:ssa";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(template, Locale.getDefault());
            String td = simpleDateFormat.format(now);
            td = td.replace("AM","a"); td=td.replace("PM","p");
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options).copy(Bitmap.Config.ARGB_8888, true);
            //draw text on image
            BitmapText.drawTextBitmap(bitmap, td);
            //save it again
            FileOutputStream output = new FileOutputStream(fileUri.getPath());
            //compression
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, output);
            output.flush();
            output.close();
            Log.i("SETTING IMAGE", "setting..." + getImageView.getId());
            getImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image1:
                imageId = R.id.image1;
                getImageView = (ImageView) v;
                getImageView.findViewById(imageId);
                CaptureImage();
                break;

            case R.id.image2:
                imageId = R.id.image2;
                getImageView = (ImageView) v;
                getImageView.findViewById(imageId);
                CaptureImage();
                break;

            case R.id.image3:
                imageId = R.id.image3;
                getImageView = (ImageView) v;
                getImageView.findViewById(imageId);
                CaptureImage();
                break;

//            case R.id.image4:
//                imageId = R.id.image4;
//                getImageView = (ImageView)v;
//                getImageView.loginFragment(imageId);
//                CaptureImage();
//                break;
//
//            case R.id.image5:
//                imageId = R.id.image5;
//                getImageView = (ImageView)v;
//                getImageView.loginFragment(imageId);
//                CaptureImage();
//                break;

            case R.id.continueBtn:
                Intent intent = new Intent(ImagePreview.this, Preview.class);
                intent.putExtra("uri_list", uriList);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("address", address);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                startActivity(intent);
                break;
            case R.id.cancelBtn:
                Intent intent2 = new Intent(ImagePreview.this, Report.class);
                startActivity(intent2);
                break;

            default:
                break;
        }

    }


}
