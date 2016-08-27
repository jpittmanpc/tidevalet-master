package com.tidevalet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tidevalet.R;
import com.tidevalet.interfaces.ViolationListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViolationListener} interface
 * to handle interaction events.
 * Use the {@link Violation1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Violation1 extends Fragment implements View.OnClickListener, Step {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId";
    private int i = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ViolationListener vL;
    private TextView errorText;
    public Violation1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Violation1.
     */
    // TODO: Rename and change types and number of parameters
    public static Violation1 newInstance(String param1, String param2) {
        Violation1 fragment = new Violation1();
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
        View v = inflater.inflate(R.layout.viol1, container, false);
        EditText bldg = (EditText)v.findViewById(R.id.bldg);
        bldg.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        bldg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftKeyboard(v);
            }
        });
        errorText = (TextView) v.findViewById(R.id.errorTextView1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mma", Locale.getDefault());
        //TextView propViol = (TextView)v.findViewById(R.id.violProp);
        TextView dateTxt = (TextView)v.findViewById(R.id.dateTxt);
        TextView timeTxt = (TextView)v.findViewById(R.id.timeTxt);
        ImageButton takePic = (ImageButton)v.findViewById(R.id.imageButton);
        takePic.setOnClickListener(this);
        dateTxt.setText(dateFormat.format(new Date()));
        timeTxt.setText(timeFormat.format(new Date()));
        sendview(v);
        return v;


    }
    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    public void takePicture(View view) {
        if (vL != null) {
            vL.clicked(view);
        }
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViolationListener) {
            vL = (ViolationListener) context;
        } else {
            throw new ClassCastException(context.toString() + "must implement MainListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        vL = null;
    }

    public void sendview(View v) {
        vL.sendview(v, 1);
    }

    @Override
    public void onClick(View v) {
        errorText.setText("");
        takePicture(v);
    }

    @Override
    public int getName() {
        return 0;
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
