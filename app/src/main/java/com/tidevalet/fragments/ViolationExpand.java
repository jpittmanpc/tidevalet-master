package com.tidevalet.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.activities.ViolationActivity;
import com.tidevalet.helpers.Post;
import com.tidevalet.interfaces.MainListener;
import com.tidevalet.thread.adapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViolationExpand#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViolationExpand extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param1";
    private MainListener mListener;
    private String mParam1;
    private long mParam2;
    private static Post post = null;
    private RadioButton yes;
    private RadioButton no;


    public ViolationExpand() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ViolationExpand.
     */
    // TODO: Rename and change types and number of parameters
    public static ViolationExpand newInstance(String param1, Post posted) {
        post = posted;
        ViolationExpand fragment = new ViolationExpand();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM2, post.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getLong(ARG_PARAM2);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainListener) {
            mListener = (MainListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_violation, container, false);
        adapter dbAdapter = new adapter(App.getAppContext());
        dbAdapter.open();
        post = dbAdapter.getPostById(mParam2);
        dbAdapter.close();
        TextView location = (TextView)view.findViewById(R.id.bldgunit);
        location.setText("Location: " + post.getBldg() + "/" + post.getUnit());
        TextView comments = (TextView)view.findViewById(R.id.contcomments);
        comments.setText("Comments: " + post.getContractorComments());
        TextView violationType = (TextView)view.findViewById(R.id.violation_Type);
        violationType.setText(violationType.getText() + post.getViolationType());
        if (post.getPU() == 0) {
            yes = (RadioButton) view.findViewById(R.id.pickedup_yes);
            yes.setChecked(true);
            //yes.setOnClickListener(this);
        }
        if (post.getPU() == 1) {
            no = (RadioButton) view.findViewById(R.id.pickedup_no);
            no.setChecked(true);
            //no.setOnClickListener(this);
        }

       // TextView isPosted = (TextView)view.findViewById(R.id.expand_posted);
        //isPosted.setText("Posted: " + (post.getIsPosted() == 0 ? "No" : "Yes"));
        String[] imgs = post.getLocalImagePath().split(",");
        ArrayList<ImageButton> imageButtons = new ArrayList<ImageButton>();
        imageButtons.add((ImageButton)view.findViewById(R.id.ximg1));
        imageButtons.add((ImageButton)view.findViewById(R.id.ximg2));
        imageButtons.add((ImageButton)view.findViewById(R.id.ximg3));
        imageButtons.add((ImageButton)view.findViewById(R.id.ximg4));
        ImageLoader imgLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(
                        R.drawable.placeholder)
                .showImageForEmptyUri(
                        R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .displayer(new FadeInBitmapDisplayer(500))
                .cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageSize size = new ImageSize(80,80);

        for (int i=0;i<imgs.length;i++) {
            ImageButton iz = imageButtons.get(i);
            String filepath2 = "";
            final String filepath = Uri.parse(imgs[i]).toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ", "");
            Log.d("TAG",filepath + "");
            try {
                if (filepath.charAt(0) == '/') {
                    filepath2 = "file://" + filepath;
                    imgLoader.displayImage(filepath2.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", ""), iz, size);
                }
                else { imgLoader.displayImage(filepath, iz, size); }
            }
            catch(Exception e) { }
            iz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTheUri(v, filepath);
                }
            });
        }
        //LinearLayout click = (LinearLayout)view.findViewById(R.id.expand_close_click);
        //click.setOnClickListener(this);
      //  ImageButton close = (ImageButton)view.findViewById(R.id.expand_close_button);
        //close.setOnClickListener(new View.OnClickListener() {
            //@Override
           // public void onClick(View v) {
            //    getActivity().onBackPressed();
        // }
       // });
    //    ImageButton edit = (ImageButton)view.findViewById(R.id.expand_edit);
        //edit.setOnClickListener(this);
        return view;
    }
     public void setTheUri(View v, String filepath) {
        Log.d("ViolationExpand","set");
      mListener.setUri(v, filepath);
    }
    private void editViolation(long id) {
        Intent i = new Intent(getActivity(), ViolationActivity.class);
        i.putExtra("id", id);
        Log.d("Viol", id + "");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().startActivity(i);
        getActivity().finish();
        Log.d(getTag(), "Edit click");
    }
    @Override
    public void onClick(View v) {
    }
}
