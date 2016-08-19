package com.tidevalet.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.interfaces.ViolationListener;

import java.util.ArrayList;
import java.util.List;


public class Violation2 extends Fragment implements View.OnClickListener, Step {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public ViolationListener vL;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CheckBox cb1;

//    private MainListener vL;
    private String[] checkBoxListLeft = { "Not Tied", "Over Weight", "Sharp Objects", "Pet Waste", "Hazardous", "CardBoard" };
    private String[] checkBoxListRight = {"Not Bagged", "Oversized Trash", "Leaking Trash", "Outside Hours", "Recycling", "Empty Bin" };
    private List<CheckBox> cbList = new ArrayList<CheckBox>();
    private StringBuilder violationTypes = new StringBuilder();
    private TextView errorTextView;
    public Violation2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Violation2.
     */
    // TODO: Rename and change types and number of parameters
    public static Violation2 newInstance(String param1, String param2) {
        Violation2 fragment = new Violation2();
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
        View v = inflater.inflate(R.layout.fragment_violation2, container, false);
        sendview(v);
        errorTextView = (TextView) v.findViewById(R.id.errorTextView2);
        for (String aCheckBoxListLeft : checkBoxListLeft) {
            LinearLayout leftside = (LinearLayout) v.findViewById(R.id.left);
            CheckBox cb = new CheckBox(App.getInstance());
            Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(cb.getContext(), R.drawable.abc_btn_check_material));
            cb.setButtonDrawable(drawable);
            cb.setText(aCheckBoxListLeft);
            cb.setOnClickListener(this);
            cb.setTextColor(getResources().getColor(R.color.TideBlue));
            leftside.addView(cb);
            cbList.add(cb);
        }
        for (String aCheckBoxListRight : checkBoxListRight) {
            LinearLayout rightside = (LinearLayout) v.findViewById(R.id.right);
            CheckBox cb = new CheckBox(App.getInstance());
            Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(cb.getContext(), R.drawable.abc_btn_check_material));
            cb.setButtonDrawable(drawable);
            cb.setText(aCheckBoxListRight);
            cb.setOnClickListener(this);
            cb.setTextSize(15);
            cb.setTextColor(getResources().getColor(R.color.TideBlue));
            rightside.addView(cb);
            cbList.add(cb);
        }
       return v;
    }
    @Override
    public void onClick(View v) {
        errorTextView.setText("");
        violationTypes = new StringBuilder();
        for (int i = 0; i < cbList.size(); i++) {
            if (cbList.get(i).isChecked()) {
                String type = cbList.get(i).getText().toString();
                violationTypes.append(type).append(", ");
            }
        }
        if (violationTypes.length() == 0) {
            errorTextView.setText(R.string.errorForNoViolationType);
            errorTextView.setTextColor(Color.RED);
        }
        else {
            violationTypes.setLength(violationTypes.length() - 2); //removing the last ", "
            giveViolationTypes(violationTypes.toString()); }
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
    public void giveViolationTypes(String list) {
        if (vL != null) {
            vL.violationTypes(list);
        }
    }
    public void sendview(View v) {
        vL.sendview(v, 2);
    }

    @Override
    public int getName() {
        return 1;
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
