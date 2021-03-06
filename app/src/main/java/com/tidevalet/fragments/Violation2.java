package com.tidevalet.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.SessionManager;
import com.tidevalet.helpers.Post;
import com.tidevalet.interfaces.ViolationListener;
import com.tidevalet.thread.adapter;

import java.util.ArrayList;
import java.util.List;


public class Violation2 extends Fragment implements View.OnClickListener, Step {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public ViolationListener vL;
    private static final String POST_ID = "id";
    private RadioButton got, not;
    private String[] checkBoxListLeft = { "Not Tied", "Over Weight", "Sharp Objects", "Pet Waste", "Hazardous", "CardBoard" };
    private String[] checkBoxListRight = {"Not Bagged", "Oversized Trash", "Leaking Trash", "Outside Hours", "Recycling", "Trash without Bin" };
    private List<CheckBox> cbList = new ArrayList<CheckBox>();
    private StringBuilder violationTypes = new StringBuilder();
    private TextView errorTextView;
    private Post post = null;
    private RelativeLayout.LayoutParams params;
    static long postId = -1;
    static SessionManager sm;
    private int PICKEDUP = 1;
    public Violation2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Violation2.
     */
    // TODO: Rename and change types and number of parameters
    public static Violation2 newInstance(long postId) {
        sm = new SessionManager(App.getAppContext());
        Bundle args = new Bundle();
        args.putLong(POST_ID, postId);
        sm.setpostId(postId);
        Violation2 fragment = new Violation2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = new SessionManager(App.getAppContext());
        if (getArguments() != null) {
            postId = sm.getpostId();
            Log.d("Viol2",postId + "");
            if (postId != -1) {
                adapter dBadapter = new adapter(this.getActivity());
                dBadapter.open();
                this.post = dBadapter.getPostById(postId);
                Log.d("onCreate2", post.getBldg() + post.getUnit() + post.getViolationType() + "  .." + post.getTimestamp());
                dBadapter.close();
            }
        }
        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setLayoutDirection(Gravity.CENTER_HORIZONTAL);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.viol2, container, false);
        errorTextView = (TextView) v.findViewById(R.id.errorTextView2);
        got = (RadioButton) v.findViewById(R.id.pickedup);
        not = (RadioButton) v.findViewById(R.id.notpickedup);
        got.setOnClickListener(this);
        not.setOnClickListener(this);
        ViewGroup left = (ViewGroup) v.findViewById(R.id.leftOptions);
        ViewGroup right = (ViewGroup) v.findViewById(R.id.rightOptions);
        int x=0;
        for (int i = 0; i<checkBoxListLeft.length; i++) {
            if (left.getChildAt(i) instanceof CheckBox) {
                 CheckBox checkBox = (CheckBox) left.getChildAt(i);
                 checkBox.setOnClickListener(this);
                 checkBox.setText(checkBoxListLeft[x]);
                 cbList.add(checkBox);
                 try {
                     if (!post.getViolationType().isEmpty()) {
                         if (post.getViolationType().equals(checkBoxListLeft[x])) {
                             checkBox.setChecked(true);
                         }
                     }
                 }
                 catch (NullPointerException e) { }
                 }
                 x++;
            }
        x=0;
        for (int i = 0; i<checkBoxListRight.length; i++) {
            if (right.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) right.getChildAt(i);
                checkBox.setOnClickListener(this);
                checkBox.setText(checkBoxListRight[x]);
                cbList.add(checkBox);
                try {
                    if (!post.getViolationType().isEmpty()) {
                        if (post.getViolationType().equals(checkBoxListRight[x])) {
                            checkBox.setChecked(true);
                        }
                    }
                }
                catch (NullPointerException e) { }
                x++;
            }
        }
        sendview(v);
        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pickedup:
                not.setChecked(false);
                got.setChecked(true);
                break;
            case R.id.notpickedup:
                got.setChecked(false);
                not.setChecked(true);
                break;
            default:
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
                } else {
                    violationTypes.setLength(violationTypes.length() - 2); //removing the last ", "
                    giveViolationTypes(violationTypes.toString(), PICKEDUP());
                }
                break;
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViolationListener) {
            vL = (ViolationListener) context;
            vL.checkType(cbList);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainListener");
        }
    }
    public int PICKEDUP() {
        if (not.isChecked()) { PICKEDUP = 0; }
        else { PICKEDUP = 1; }
        return PICKEDUP;
    }
    @Override
    public void onDetach() {

        super.onDetach();
        vL = null;
    }
    public void giveViolationTypes(String list, int PICKEDUP) {
        if (vL != null) {
            vL.violationTypes(list, PICKEDUP);
        }
    }
    void setViolationTypes(List<CheckBox> cbList) {
        if (vL != null) {
            vL.checkType(cbList);
        }
    }
    public void sendview(View v) {
        vL.sendview(v, 2);
    }

    @Override
    public int getName() {
        String one = "1";
        return Integer.parseInt(one);
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
