package com.tidevalet.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.SessionManager;
import com.tidevalet.activities.MainActivity;
import com.tidevalet.helpers.Post;
import com.tidevalet.interfaces.MainListener;
import com.tidevalet.service.ulservice;
import com.tidevalet.thread.adapter;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViolationExpand#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViolationExpand extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM2 = "id";
    private MainListener mListener;
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
    public static ViolationExpand newInstance(long posted) {
        adapter dbAdapter = new adapter(App.getAppContext());
        SessionManager session = new SessionManager(App.getAppContext());
        dbAdapter.open();
        post = dbAdapter.getPostById(posted);
        dbAdapter.close();
        Log.d("TAG", post.getId() + "posted" + posted + "");
        ViolationExpand fragment = new ViolationExpand();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM2, posted);
        fragment.setArguments(args);
        session.resetpostId();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getLong(ARG_PARAM2);
        }
        adapter dbAdapter = new adapter(App.getAppContext());
        SessionManager session = new SessionManager(App.getAppContext());
        dbAdapter.open();
        post = dbAdapter.getPostById(mParam2);
        Log.d("onCreate,ViolExp",mParam2 + " " + post.getBldg());
        dbAdapter.close();
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
        final adapter dbAdapter = new adapter(App.getAppContext());
        Log.d("mParam2", post.getLocalImagePath() + "" + post.getBldg() + "");
        /*dbAdapter.open();
        post = dbAdapter.getPostById(mParam2);
        dbAdapter.close();*/
        TextView location = (TextView)view.findViewById(R.id.bldgunit);
        location.setText("Location: " + post.getBldg() + "/" + post.getUnit());
        TextView date = (TextView)view.findViewById(R.id.datetext);
        date.setText(post.getTimestamp());
        final TextView comments = (TextView)view.findViewById(R.id.contcomments);
        comments.setText(post.getContractorComments());
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
        String[] imgs = null;
        try {
            imgs = post.getLocalImagePath().split(",");
        }
        catch (NullPointerException e) {
            try{ imgs = post.getImagePath().split(","); }
            catch (NullPointerException f) { f.printStackTrace(); }
        }
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
        ImageSize size = new ImageSize(140,140);

        for (int i=0;i<imageButtons.size();i++) {
            ImageButton iz = imageButtons.get(i);
            String filepath2 = "";
            if (imgs.length <= i) {
                iz.setVisibility(View.INVISIBLE);
            }
            else {
                final String filepath = Uri.parse(imgs[i]).toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
                Log.d("TAG", filepath + "");
                try {
                    if (filepath.charAt(0) == '/') {
                        filepath2 = "file://" + filepath;
                        imgLoader.displayImage(filepath2.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", ""), iz, size);
                    } else {
                        imgLoader.displayImage(filepath, iz, size);
                    }
                } catch (Exception e) {
                    iz.setVisibility(View.INVISIBLE);
                }
                iz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setTheUri(v, filepath);
                    }
                });
            }
        }
        Button edit = (Button) view.findViewById(R.id.updateViolation);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String current = "";
                try {
                    current = post.getContractorComments();
                } catch(NullPointerException e) { e.printStackTrace(); }
                String newComments = comments.getText().toString();
                Log.d("contractorComments", "curr:" + current + " new: " + newComments);
                dbAdapter.open();
                post.setContractorComments(newComments);
                dbAdapter.updatePost(post);
                dbAdapter.close();
                editViolation(post);
            }
        });
        return view;
    }
     public void setTheUri(View v, String filepath) {
        Log.d("ViolationExpand","set");
      mListener.setUri(v, filepath);
    }
    private void editViolation(Post post) {
        adapter dbAdapter = new adapter(this.getContext());
        dbAdapter.open();
        dbAdapter.updatePost(post);
        dbAdapter.close();
        Intent service = new Intent(this.getContext(), ulservice.class);
        service.putExtra(ARG_PARAM2, post.getId());
        service.putExtra("type","editPost");
        this.getContext().startService(service);
        getActivity().setResult(RESULT_OK);
        LocalBroadcastManager.getInstance(App.getAppContext()).registerReceiver(MainActivity.broadcastReceiver, new IntentFilter("sendSnackBar"));
        Log.d(getTag(), "Edit click");
        Toast.makeText(getActivity(), "Submitting..", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View v) {
    }
}
