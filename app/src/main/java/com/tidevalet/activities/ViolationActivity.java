package com.tidevalet.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.AbstractStepAdapter;
import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.SessionManager;
import com.tidevalet.fragments.Violation1;
import com.tidevalet.fragments.Violation2;
import com.tidevalet.fragments.Violation3;
import com.tidevalet.helpers.Attributes;
import com.tidevalet.helpers.Post;
import com.tidevalet.helpers.Properties;
import com.tidevalet.interfaces.ViolationListener;
import com.tidevalet.service.ulservice;
import com.tidevalet.thread.adapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViolationActivity extends AppCompatActivity implements ViolationListener {
    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private List<String> violationTypes = new ArrayList<>();
    private List<ImageView> dots;
    Button back;
    Button next;
    private Uri filePath;
    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    Properties property;
    Attributes attributes = new Attributes();
    Post post;
    List<String> uriList = new ArrayList<>();
    private View Violation1v;
    private View Violation2v;
    private View Violation3v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        post = new Post();
        SessionManager session = new SessionManager(this);
        adapter adapter = new adapter(this);
        setContentView(R.layout.violation_slider);
        StepperLayout mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager()));
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        /*addDots();
        selectDot(0);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        back.setVisibility(View.INVISIBLE);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, post.getViolationType() + " ...types");
                if (mPager.getCurrentItem() == 2) {
                    EditText comments = (EditText) Violation3v.findViewById(R.id.comments);
                    post.setContractorComments(comments.getText().toString());
                    if (post.getViolationType() == null) {
                        mPager.setCurrentItem(1);
                        TextView tv1 = (TextView) Violation2v.findViewById(R.id.errorTextView2);
                        tv1.setText(R.string.errorForNoViolationType);
                        tv1.setTextColor(Color.RED);
                    }
                    if (uriList.size() == 0) {
                        mPager.setCurrentItem(0);
                        TextView tv0 = (TextView) Violation1v.findViewById(R.id.errorTextView1);
                        tv0.setText(R.string.errorForNoPic);
                    }
                    else {
                        Snackbar.make(v, "Submitting", Snackbar.LENGTH_LONG).show();
                        startSubmit();
                    }
                }
                else { mPager.setCurrentItem((mPager.getCurrentItem()) + 1); }
            }
        });*/
        adapter.open();
        property = adapter.getPropertyById(attributes.getPropertyId());
        adapter.close();
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
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    static String TAG = "TAG";
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
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
                Log.d("TAG", "no write access");
                return null;
            }
            else { mediaStorageDir.mkdir(); }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }
    private File getFileName() {
        File folder = getApplicationContext().getFilesDir();
        Log.d("TAG", folder.toString());
        if(!folder.exists()){ folder.mkdir(); Log.d("TAG", "folder created"); }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "tide_"+ timeStamp + "_";
        File image_file = null;
        try { image_file = File.createTempFile(imageFileName,".jpg",folder); }
        catch (IOException e) { e.printStackTrace(); }
        return image_file;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG", "OnActResult" + requestCode + " resultcode: " + resultCode);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                uriList.add(filePath.getPath());
                Log.d("ONACTIVITYRESULT", "Got pic");
                Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                switch (uriList.size()) {
                    case 1: img1.setImageBitmap(ThumbnailUtils.extractThumbnail(image, img1.getWidth(), img1.getHeight())); break;
                    case 2: img2.setImageBitmap(ThumbnailUtils.extractThumbnail(image, img2.getWidth(), img2.getHeight())); break;
                    case 3: img3.setImageBitmap(ThumbnailUtils.extractThumbnail(image, img3.getWidth(), img3.getHeight())); break;
                    case 4: img4.setImageBitmap(ThumbnailUtils.extractThumbnail(image, img4.getWidth(), img4.getHeight())); break;
                    default: break;
                }
            }
            catch (Exception e) { e.printStackTrace(); }

        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
    @Override
    public void violationTypes(String list) {
        post.setViolationType(list);
    }

    @Override
    public void sendview(View v, int id) {
        switch (id) {
            case 1: Violation1v = v; break;
            case 2: Violation2v = v; break;
            case 3: Violation3v = v; break;
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


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Violation1();
                case 1:
                    return new Violation2();
                case 2:
                    return new Violation3();
                default: break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

    private void startSubmit() {
        post.setIsPosted(0);
        post.setLocalImagePath(uriList);
        post.setPropertyId(attributes.getPropertyId());
        EditText bldgV = (EditText) Violation1v.findViewById(R.id.bldg);
        EditText unitV = (EditText) Violation1v.findViewById(R.id.unit);
        String unit = "";
        String bldg = "";
        bldg = bldgV.getText().toString();
        unit = unitV.getText().toString();
        Log.d("BLDG", bldg + unit + " ");
        post.setBldg(bldg);
        post.setUnit(unit);
        adapter dbAdapter = new adapter(this);
        dbAdapter.open();
        post = dbAdapter.addPost(post);
        dbAdapter.close();
        Intent service = new Intent(this,  ulservice.class);
        service.putExtra("id", post.getId());
        startService(service);
        setResult(RESULT_OK);
        finish();
        //start submit, then call new intent to go back to the property screen
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("snackbar", "The violation has been successfully submitted");
        startActivity(i);
        finish();

    }

    private void addDots() {
        dots = new ArrayList<>();
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.dots);

        for (int i = 0; i < NUM_PAGES; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(getResources().getDrawable(R.drawable.pager_dot_not_selected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            dotsLayout.addView(dot, params);
            dots.add(dot);
        }
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
                switch (position) {
                    case 0:
                        back.setVisibility(View.INVISIBLE);
                        break;
                    case 1: next.setText("Next");
                        back.setVisibility(View.VISIBLE);
                        break;
                    case 2: next.setText("Submit");
                        break;
                    default: break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void selectDot(int idx) {
        Resources res = getResources();
        for(int i = 0; i < NUM_PAGES; i++) {
            int drawableId = (i==idx)?(R.drawable.pager_dot_selected):(R.drawable.pager_dot_not_selected);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
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
            switch (position) {
                case 0:
                    final Violation1 step = new Violation1();
                    Bundle b = new Bundle();
                    b.putInt(CURRENT_STEP_POSITION_KEY, position);
                    step.setArguments(b);
                    return step;
                case 1:
                    final Violation2 step2 = new Violation2();
                    Bundle b2 = new Bundle();
                    b2.putInt(CURRENT_STEP_POSITION_KEY, position);
                    step2.setArguments(b2);
                    return step2;
                case 2:
                    final Violation3 step3 = new Violation3();
                    Bundle b3 = new Bundle();
                    b3.putInt(CURRENT_STEP_POSITION_KEY, position);
                    step3.setArguments(b3);
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