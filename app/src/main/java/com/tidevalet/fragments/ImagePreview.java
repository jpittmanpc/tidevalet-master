package com.tidevalet.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tidevalet.R;

/**
 * Created by Justin on 9/6/2016.
 */
public class ImagePreview extends Fragment {
    ImageView imageView;
    boolean willFit;
    private String bitmap;
    public static ImagePreview newInstance() {
        ImagePreview fragment = new ImagePreview();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if (b != null){
            bitmap = b.getString("img_filepath", "image");
            Log.d("ImagePrev","...got");
        }
        else { Log.d("imagePrev","null parc"); }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_imagepreview, container, false);
        imageView = (ImageView) v.findViewById(R.id.imagepreview);
        Log.d("imagePrev","...now here");
        ImageLoader imgLoader = ImageLoader.getInstance();
        ImageSize size = new ImageSize(80,80);

          if (willFit) {
              willFit=false;
              Log.d("imagePrev","setting1");
              imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
              imageView.setAdjustViewBounds(true);
              imgLoader.displayImage(bitmap, imageView, size);

          }
          else {
              Log.d("imagePrev","setting2");
              willFit = true;
              imgLoader.displayImage(bitmap, imageView, size);
          }
        return v;
    }
}
