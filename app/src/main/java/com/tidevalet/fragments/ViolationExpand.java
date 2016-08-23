package com.tidevalet.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.helpers.Post;
import com.tidevalet.interfaces.MainListener;
import com.tidevalet.thread.adapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViolationExpand#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViolationExpand extends Fragment implements MainListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param1";
    private MainListener mListener;

    private String mParam1;
    private long mParam2;


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
    public static ViolationExpand newInstance(String param1, Post post) {
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
        mListener = this;

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
        Post post = dbAdapter.getPostById(mParam2);
        dbAdapter.close();
        TextView location = (TextView)view.findViewById(R.id.expand_location);
        location.setText("Location: " + post.getBldg() + "/" + post.getUnit());
        TextView comments = (TextView)view.findViewById(R.id.expand_comments);
        comments.setText("Comments: " + post.getContractorComments());
        TextView violationType = (TextView)view.findViewById(R.id.expand_violation);
        violationType.setText(post.getViolationType());
        TextView postLink = (TextView)view.findViewById(R.id.expand_link);
        postLink.setText(post.getReturnedString() + "");
        postLink.setMovementMethod(LinkMovementMethod.getInstance());
        ImageButton close = (ImageButton)view.findViewById(R.id.expand_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ImageButton edit = (ImageButton)view.findViewById(R.id.expand_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Edit", Snackbar.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void clicked(View v) {

    }

    @Override
    public void authenticated() {

    }

    @Override
    public void showDialog(int i, String value) {

    }

    @Override
    public void propertyList(View v) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Post item, View view) {

    }

    @Override
    public void propertyView(View v) {

    }

    @Override
    public void onClick(View v) {

    }
}
