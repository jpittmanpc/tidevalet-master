package com.tidevalet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tidevalet.R;
import com.tidevalet.interfaces.MainListener;
import com.tidevalet.interfaces.ViolationListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link Violation1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Violation3 extends Fragment implements Step {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ViolationListener vL;
public Violation3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Violation3.
     */
    // TODO: Rename and change types and number of parameters
    public static Violation3 newInstance(String param1, String param2) {
        Violation3 fragment = new Violation3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.viol3, container, false);
        sendview(v);
        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViolationListener) {
            vL = (ViolationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        vL = null;
    }
    public void sendview(View v) {
        vL.sendview(v, 3);
    }

    @Override
    public int getName() {
        return 2;
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(VerificationError error) {

    }
}
