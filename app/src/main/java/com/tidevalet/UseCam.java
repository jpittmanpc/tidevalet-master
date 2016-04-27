/*package com.tidevalet;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Justin on 4/27/16.
 *
public class UseCam extends Activity {
    private static final String TAG = "Picture";
    Camera camera;
    Preview preview;
    Button buttonClick;

    /** Called when the activity is first created. *
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        preview = new Preview(this);
        ((FrameLayout) findViewById(R.id.preview)).addView(preview);

        buttonClick = (Button) findViewById(R.id.buttonClick);
        buttonClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                preview.camera.takePicture(shutterCallback, rawCallback,
                        jpegCallback);
            }
        });

        preview.camera.autoFocus(new Camera.AutoFocusCallback() {

            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
                }
            }
        });

        Log.d(TAG, "onCreate'd");
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.d(TAG, "onShutter'd");
        }
    };

    /** Handles data for raw picture *
    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "onPictureTaken - raw");
        }
    };

    /** Handles data for jpeg picture *
    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream outStream = null;
            try {
                // write to local sandbox file system
                // outStream =
                // CameraDemo.this.openFileOutput(String.format("%d.jpg",
                // System.currentTimeMillis()), 0);
                // Or write to sdcard
                outStream = new FileOutputStream(String.format(
                        "/sdcard/%d.jpg", System.currentTimeMillis()));
                outStream.write(data);
                outStream.close();
                Log.d(TAG, "onPictureTaken - wrote b	ytes: " + data.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

}
*/