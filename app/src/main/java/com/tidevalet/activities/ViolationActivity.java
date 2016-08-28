package com.tidevalet.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.adapter.AbstractStepAdapter;
import com.tidevalet.App;
import com.tidevalet.BitmapText;
import com.tidevalet.R;
import com.tidevalet.SessionManager;
import com.tidevalet.fragments.Violation1;
import com.tidevalet.fragments.Violation2;
import com.tidevalet.fragments.Violation3;
import com.tidevalet.helpers.Post;
import com.tidevalet.helpers.Properties;
import com.tidevalet.interfaces.ViolationListener;
import com.tidevalet.service.ulservice;
import com.tidevalet.thread.adapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViolationActivity extends AppCompatActivity implements ViolationListener, StepperLayout.StepperListener {
    private Uri filePath;
    private ImageView img1, img2, img3, img4;
    private Properties property;
    private Post post;
    private List<String> uriList = new ArrayList<>();
    private View Violation1v;
    private View Violation2v;
    private View Violation3v;
    private boolean upload = true;
    private StepperLayout stepperLayout;
    private long iE = -1;
    private String subtitletext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        iE = intent.getLongExtra("id", -1);
        post = new Post();
        SessionManager session = new SessionManager(this);
        adapter adapter = new adapter(this);
        if (iE != -1) {
            adapter.open();
            post = adapter.getPostById(iE);
            adapter.close();
            subtitletext = "Edit Violation " + post.getBldg() + "/" + post.getUnit() + " ";
        }
        else {
            subtitletext = "New Violation ";
        }
        setContentView(R.layout.violation_slider);
        stepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        stepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager()));
        stepperLayout.setListener(this);
        adapter.open();
        property = adapter.getPropertyById(session.propertySelected());
        adapter.close();
        getSupportActionBar().setSubtitle(subtitletext + property.getName());
    }

    private void dispatchTakePictureIntent() {
        if (isStoragePermissionGranted()) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            filePath = getOutputMediaFileUri(1);
            Log.d("filePath", filePath.toString());
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) { return true; }
            else {  ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false; }
        } else { return true;  }
    }
    static String TAG = "TAG";

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "TIDE");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            } else {
                mediaStorageDir.mkdir();
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
        Log.d("TAG", "OnActResult" + requestCode + " resultcode: " + resultCode);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                Log.d("ONACTIVITYRESULT", "Got pic");
                Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                saveScaledPhotoToFile(image, filePath.getPath());
                switch (uriList.size()) {
                    case 1:
                        img1.setImageBitmap(ThumbnailUtils.extractThumbnail(image, img1.getWidth(), img1.getHeight()));
                        break;
                    case 2:
                        img2.setImageBitmap(ThumbnailUtils.extractThumbnail(image, img2.getWidth(), img2.getHeight()));
                        break;
                    case 3:
                        img3.setImageBitmap(ThumbnailUtils.extractThumbnail(image, img3.getWidth(), img3.getHeight()));
                        break;
                    case 4:
                        img4.setImageBitmap(ThumbnailUtils.extractThumbnail(image, img4.getWidth(), img4.getHeight()));
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void saveScaledPhotoToFile(Bitmap photoBm, String oldFile) {
        int bmOriginalWidth = photoBm.getWidth();
        int bmOriginalHeight = photoBm.getHeight();
        double originalWidthToHeightRatio =  1.0 * bmOriginalWidth / bmOriginalHeight;
        double originalHeightToWidthRatio =  1.0 * bmOriginalHeight / bmOriginalWidth;
        //choose a maximum height
        int maxHeight = 640;
        //choose a max width
        int maxWidth = 480;
        //call the method to get the scaled bitmap
        photoBm = getScaledBitmap(photoBm, bmOriginalWidth, bmOriginalHeight,
                originalWidthToHeightRatio, originalHeightToWidthRatio,
                maxHeight, maxWidth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mma", Locale.getDefault());
        String text = dateFormat.format(new Date()) + " " + timeFormat.format(new Date());
        BitmapText.drawTextBitmap(photoBm, text);
        /**********THE REST OF THIS IS FROM Prabu's answer*******/
        //create a byte array output stream to hold the photo's bytes
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //compress the photo's bytes into the byte array output stream
        photoBm.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        photoBm.recycle();
        //construct a File object to save the scaled file to
        File f = getOutputMediaFile(1);
        //create an FileOutputStream on the created file
        try {
            FileOutputStream fo = new FileOutputStream(f);
            //write the photo's bytes to the file
            fo.write(bytes.toByteArray());
            //finish by closing the FileOutputStream
            fo.close();
            fo.flush();
            uriList.add(f.getPath());
            boolean deleted = new File(oldFile).delete();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    private static Bitmap getScaledBitmap(Bitmap bm, int bmOriginalWidth, int bmOriginalHeight, double originalWidthToHeightRatio, double originalHeightToWidthRatio, int maxHeight, int maxWidth) {
        if(bmOriginalWidth > maxWidth || bmOriginalHeight > maxHeight) {
            Log.v(TAG, "RESIZING bitmap FROM %sx%s " + bmOriginalWidth + bmOriginalHeight);

            if(bmOriginalWidth > bmOriginalHeight) {
                bm = scaleDeminsFromWidth(bm, maxWidth, bmOriginalHeight, originalHeightToWidthRatio);
            } else if (bmOriginalHeight > bmOriginalWidth){
                bm = scaleDeminsFromHeight(bm, maxHeight, bmOriginalHeight, originalWidthToHeightRatio);
            }

            Log.v(TAG, "RESIZED bitmap TO %sx%s " + bm.getWidth() + bm.getHeight());
        }
        return bm;
    }

    private static Bitmap scaleDeminsFromHeight(Bitmap bm, int maxHeight, int bmOriginalHeight, double originalWidthToHeightRatio) {
        int newHeight = (int) Math.max(maxHeight, bmOriginalHeight * .55);
        int newWidth = (int) (newHeight * originalWidthToHeightRatio);
        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return bm;
    }

    private static Bitmap scaleDeminsFromWidth(Bitmap bm, int maxWidth, int bmOriginalWidth, double originalHeightToWidthRatio) {
        //scale the width
        int newWidth = (int) Math.max(maxWidth, bmOriginalWidth * .75);
        int newHeight = (int) (newWidth * originalHeightToWidthRatio);
        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return bm;
    }

    @Override
    public void onBackPressed() {
        if (iE != -1) { super.onBackPressed(); return; }
        if (stepperLayout.getCurrentStepPosition() == 0) {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
        }
        else {
            // Otherwise, select the previous step.
            stepperLayout.setCurrentStepPosition((stepperLayout.getCurrentStepPosition()) - 1);
        }
    }
    @Override
    public void violationTypes(String list, int PICKEDUP) {
        post.setViolationType(list);
        post.setPU(PICKEDUP);
    }

    @Override
    public void sendview(View v, int id) {
        switch (id) {
            case 1: Violation1v = v;
                if (iE != -1) {
                    ((TextView) v.findViewById(R.id.dateTxt)).setText(post.getTimestamp());
                    ((EditText) v.findViewById(R.id.bldg)).setText(post.getBldg());
                    ((EditText) v.findViewById(R.id.unit)).setText(post.getUnit());
                }
                break;
            case 2: Violation2v = v;
                if (iE != -1) {
                    switch (post.getPU()) {
                        case 0:
                            ((RadioButton) v.findViewById(R.id.notpickedup)).setChecked(true);
                            ((RadioButton) v.findViewById(R.id.pickedup)).setChecked(false);
                            break;
                        case 1:
                            ((RadioButton) v.findViewById(R.id.notpickedup)).setChecked(false);
                            break;
                        default:
                            break;
                    }
                }
                break;
            case 3: Violation3v = v;
                ((EditText)v.findViewById(R.id.comments)).setText(post.getContractorComments());
                break;
            default: break;
        }
    }

    @Override
    public void sendComments(String s) {
        post.setContractorComments(s);
    }
    @Override
    public void clicked(View v) {
        dispatchTakePictureIntent();
        img1 = (ImageView) v.getRootView().findViewById(R.id.img1);
        img2 = (ImageView) v.getRootView().findViewById(R.id.img2);
        img3 = (ImageView) v.getRootView().findViewById(R.id.img3);
        img4 = (ImageView) v.getRootView().findViewById(R.id.img4);
    }
    @Override
    public void onCompleted(View completeButton) {
        EditText comments = (EditText) Violation3v.findViewById(R.id.comments);
        post.setContractorComments(comments.getText().toString());
        if (post.getViolationType() == null) {
            stepperLayout.setCurrentStepPosition(1);
            TextView tv1 = (TextView) Violation2v.findViewById(R.id.errorTextView2);
            tv1.setText(R.string.errorForNoViolationType);
            tv1.setTextColor(Color.RED);
        }
        if (uriList.size() == 0) {
            stepperLayout.setCurrentStepPosition(0);
            TextView tv0 = (TextView) Violation1v.findViewById(R.id.errorTextView1);
            tv0.setText(R.string.errorForNoPic);
        }
        else { startSubmit(); }
    }
    @Override
    public void onError(VerificationError verificationError) {
    }
    @Override
    public void onStepSelected(int newStepPosition) {
    }
    private void startSubmit() {
        post.setIsPosted(0);
        StringBuilder stringBuilder = new StringBuilder();
        String prefix = "";
        for (int i = 0; i<uriList.size();i++) {
            stringBuilder.append(prefix);
            prefix = ",";
            stringBuilder.append(uriList.get(i));
        }
        post.setLocalImagePath(stringBuilder.toString());
        EditText bldgV = (EditText) Violation1v.findViewById(R.id.bldg);
        EditText unitV = (EditText) Violation1v.findViewById(R.id.unit);
        String unit = "";
        String bldg = "";
        bldg = bldgV.getText().toString();
        unit = unitV.getText().toString();
        Log.d("BLDG", bldg + unit + " ");
        post.setBldg(bldg);
        post.setUnit(unit);
        post.setPropertyId(new SessionManager(this).propertySelected());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MM-dd h:mma");
        post.setTimestamp(dateFormat.format(new Date()));
        adapter dbAdapter = new adapter(this);
        if (iE == -1) {
            dbAdapter.open();
            post = dbAdapter.addPost(post);
            dbAdapter.close();
        }
        else {
            dbAdapter.open();
            dbAdapter.updatePost(post);
            dbAdapter.close();
        }
        if (upload) {
            Intent service = new Intent(this, ulservice.class);
            service.putExtra("id", post.getId());
            startService(service);
            setResult(RESULT_OK);
            LocalBroadcastManager.getInstance(App.getAppContext()).registerReceiver(MainActivity.broadcastReceiver, new IntentFilter("sendSnackBar"));
            finish();
        }
        //start submit, then call new intent to go back to the property screen
        if (iE == -1) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else { super.onBackPressed(); }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SessionManager byebye = new SessionManager(App.getInstance());
                byebye.resetUser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public static class StepperAdapter extends AbstractStepAdapter {
        private static final String CURRENT_STEP_POSITION_KEY = "position";
        public StepperAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment createStep(int position) {
            Bundle b = new Bundle();
            switch (position) {
                case 0:
                    final Violation1 step = new Violation1();
                    b.putInt(CURRENT_STEP_POSITION_KEY, position);
                    step.setArguments(b);
                    return step;
                case 1:
                    final Violation2 step2 = new Violation2();
                    b.putInt(CURRENT_STEP_POSITION_KEY, position);
                    step2.setArguments(b);
                    return step2;
                case 2:
                    final Violation3 step3 = new Violation3();
                    b.putInt(CURRENT_STEP_POSITION_KEY, position);
                    step3.setArguments(b);
                    return step3;
            }
            return null;
        }
        @Override
        public int getCount() {
            return 3;
        }
    }
}