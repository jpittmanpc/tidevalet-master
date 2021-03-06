package com.tidevalet.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private static final String FRAG1 = "Violation1";
    private static final String FRAG2 = "Violation2";
    private static final String FRAG3 = "Violation3";
    String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long postId = getIntent().getLongExtra("id",-1);
        Log.d("ViolActi", postId + "");
        SessionManager session = new SessionManager(this);
        if (postId != -1) {
            postId = session.getpostId();
        }
        adapter adapter = new adapter(this);
        String subtitletext;
        subtitletext = "New Violation - ";
        setContentView(R.layout.violation_slider);
        stepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        stepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager()));
        stepperLayout.setListener(this);
        adapter.open();
        property = adapter.getPropertyById(session.propertySelected());
        post = adapter.getPostById(postId);
        adapter.close();
        getSupportActionBar().setSubtitle(subtitletext + property.getName());
        initializeFragments(postId);
    }

    private void initializeFragments(long postId) {
        FragmentManager fM = getSupportFragmentManager();
        if (Violation1v == null) {
            Violation1.newInstance(postId);
            Log.d("init","viol1-null");
        }
        if (Violation2v == null) {
            Violation2.newInstance(postId);
        }
        if (Violation3v == null) {
            Violation3.newInstance(postId);
        }
        else {
            Violation1 Violation1v = (Violation1) fM.findFragmentByTag(FRAG1);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "content:" + image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;
    private void dispatchTakePictureIntent() {
        isStoragePermissionGranted();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.tidevalet",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) { return true; }
            else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
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
    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(App.getAppContext().getFilesDir(), "TIDE");
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
                try {
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                File file = new File(imageUri.getPath());
                InputStream ims = new FileInputStream(file);
                Bitmap tempImage = BitmapFactory.decodeStream(ims);
                    switch (uriList.size()) {
                        case 0:
                            img1.setImageBitmap(ThumbnailUtils.extractThumbnail(tempImage, img1.getWidth(), img1.getHeight()));
                            break;
                        case 1:
                            img2.setImageBitmap(ThumbnailUtils.extractThumbnail(tempImage, img2.getWidth(), img2.getHeight()));
                            break;
                        case 2:
                            img3.setImageBitmap(ThumbnailUtils.extractThumbnail(tempImage, img3.getWidth(), img3.getHeight()));
                            break;
                        case 3:
                            img4.setImageBitmap(ThumbnailUtils.extractThumbnail(tempImage, img4.getWidth(), img4.getHeight()));
                            break;
                        default:
                            img1.setImageBitmap(ThumbnailUtils.extractThumbnail(tempImage, img1.getWidth(), img1.getHeight()));
                            break;
                    }
                    saveScaledPhotoToFile(tempImage, file.getPath());
                } catch (FileNotFoundException e) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void saveScaledPhotoToFile(Bitmap photoBm, String oldFile) throws IOException {
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
        File f = createImageFile();
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
        int pu = 0;
        try {
            pu = post.getPU();
        } catch (NullPointerException e) {
        }
        if (post.getId() != -1) {
        }
        switch (id) {
            case 1:
                Violation1v = v;
                break;
            case 2:
                Violation2v = v;
                if (post.getPU() == 0) {
                    ((RadioButton) v.findViewById(R.id.notpickedup)).setChecked(false);
                }
                if (post.getPU() == 1) {
                    ((RadioButton) v.findViewById(R.id.pickedup)).setChecked(true);
                }

                switch (pu) {
                    case 0:
                        ((RadioButton) v.findViewById(R.id.notpickedup)).setChecked(true);
                        ((RadioButton) v.findViewById(R.id.pickedup)).setChecked(false);
                        post.setPU(0);
                        break;
                    case 1:
                        ((RadioButton) v.findViewById(R.id.notpickedup)).setChecked(false);
                        post.setPU(1);
                        break;
                    default:
                        break;
                }

                break;

            case 3:
                Violation3v = v;
                if (post.getId() != -1) {
                    EditText comments = (EditText) v.getRootView().findViewById(R.id.contractorComments);
                    try {
                        comments.setText(post.getContractorComments());
                    } catch (NullPointerException e) {
                        Log.d("violation3", "No contractor comments yet.");
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void sendComments(String s) {
        post.setContractorComments(s);
    }

    @Override
    public void checkType(List<CheckBox> cbList) {
       // View left = Violation2v.findViewById(R.id.left);
        // View right = Violation2v.findViewById(R.id.right);
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
        EditText comments = (EditText) Violation3v.findViewById(R.id.contractorComments);
        post.setContractorComments(comments.getText().toString());
        SessionManager session = new SessionManager(this);
        long ID = session.getpostId();
        Log.d("ID",ID + "....");
        if (uriList.size() == 0 && ID != -1) {
            String[] imgpath = post.getLocalImagePath().split(",");
            for (int i=0;i<imgpath.length;i++) {
                uriList.add(imgpath[i]);
                Log.d("uriList", "added" + imgpath[i]);
            }
        }
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
        switch (newStepPosition) {
            case 0:
                if (uriList.size() > 0) {
                    Bitmap image = null;
                    ImageLoader imgLoader = ImageLoader.getInstance();
                    ImageSize size = new ImageSize(80, 80);
                   switch (uriList.size()) {
                       case 0: break;
                       case 1:
                           ImageView img = (ImageView) Violation1v.findViewById(R.id.img1);
                           String filePath = uriList.get(0).replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ","");
                           if (filePath.startsWith("http://")) {
                               imgLoader.displayImage(filePath, img, size);
                           }
                           else {
                               Uri imageUri = Uri.parse(filePath);
                               File file = new File(imageUri.getPath());
                               InputStream ims = null;
                               try {
                                   ims = new FileInputStream(file);
                               } catch (FileNotFoundException e) {
                                   e.printStackTrace();
                               }
                               Bitmap tempImage = BitmapFactory.decodeStream(ims);
                               img.setImageBitmap(ThumbnailUtils.extractThumbnail(tempImage, 80, 80));
                           }
                           break;
                       default:
                           List<ImageView> list = new ArrayList<>();
                           list.add((ImageView) Violation1v.findViewById(R.id.img1));
                           list.add((ImageView) Violation1v.findViewById(R.id.img2));
                           list.add((ImageView) Violation1v.findViewById(R.id.img3));
                           list.add((ImageView) Violation1v.findViewById(R.id.img4));
                           for (int i=0; i < uriList.size(); i++) {
                               filePath = uriList.get(i).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
                               if (filePath.startsWith("http://")) {
                                   imgLoader.displayImage(filePath, list.get(i), size);
                               }
                               else {
                                   Uri imageUri = Uri.parse(filePath);
                                   File file = new File(imageUri.getPath());
                                   InputStream ims = null;
                                   try {
                                       ims = new FileInputStream(file);
                                   } catch (FileNotFoundException e) {
                                       e.printStackTrace();
                                   }
                                   Bitmap tempImage = BitmapFactory.decodeStream(ims);
                                   list.get(i).setImageBitmap(ThumbnailUtils.extractThumbnail(tempImage, 80, 80));
                               }
                           }
                           break;
                    }

                }
        }
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
        String firstCharacter = stringBuilder.length() != 0 ? String.valueOf(stringBuilder.charAt(0)) : "";
        if (firstCharacter.equals("/")) {
            post.setLocalImagePath(stringBuilder.toString());
        }
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mma", Locale.getDefault());
        post.setTimestamp(dateFormat.format(new Date()) + " " + timeFormat.format(new Date()));
        if (post.getLocalImagePath() == null) { Log.d("imagepath", "null"); return; }
        if (post.getBldg().equals("") || post.getUnit().equals("") || post.getLocalImagePath() == null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing()){
                        new AlertDialog.Builder(ViolationActivity.this)
                                .setTitle("Not enough information.")
                                .setMessage("You must have BLDG/UNIT and Image.")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }
                }
            });
        }
        else {
            adapter dbAdapter = new adapter(this);
            dbAdapter.open();
            try {
                long id = post.getId();
                if (id != -1) { dbAdapter.updatePost(post); }
                else { post = dbAdapter.addPost(post); }
            }
            catch (NullPointerException e) {
                post = dbAdapter.addPost(post);
            }
            dbAdapter.close();
            if (!upload) {
                Toast.makeText(App.getAppContext(),"Saving..",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }
        if (upload) {
            Intent service = new Intent(this, ulservice.class);
            service.putExtra("id", post.getId());
            service.putExtra("type", "newPost");
            startService(service);
            setResult(RESULT_OK);
            LocalBroadcastManager.getInstance(App.getAppContext()).registerReceiver(MainActivity.broadcastReceiver, new IntentFilter("sendSnackBar"));
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_violation, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                upload=false;
                startSubmit();
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