package com.tidevalet.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.interfaces.ViolationListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViolationListener} interface
 * to handle interaction events.
 * Use the {@link Violation2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Violation2 extends Fragment implements View.OnClickListener {
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
    private String[] checkBoxListRight = {"Not bagged", "Oversized Trash", "Leaking Trash", "Outside Hours", "Recycling", "Other" };
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
        for (int i=0; i < checkBoxListLeft.length; i++) {
            LinearLayout leftside = (LinearLayout) v.findViewById(R.id.left);
            CheckBox cb = new CheckBox(App.getInstance());
            Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(cb.getContext(), R.drawable.abc_btn_check_material));
            cb.setButtonDrawable(drawable);
            cb.setText(checkBoxListLeft[i]);
            cb.setOnClickListener(this);
            cb.setTextColor(getResources().getColor(R.color.TideBlue));
            leftside.addView(cb);
            cbList.add(cb);
        }
        for (int i=0; i<checkBoxListRight.length;i++) {
            LinearLayout rightside = (LinearLayout) v.findViewById(R.id.right);
            CheckBox cb = new CheckBox(App.getInstance());
            Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(cb.getContext(), R.drawable.abc_btn_check_material));
            cb.setButtonDrawable(drawable);
            cb.setText(checkBoxListRight[i]);
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
        for (int i = 0; i < cbList.size(); i++) {
            if (cbList.get(i).isChecked()) {
                violationTypes.append(cbList.get(i).getText().toString()+",");
            }
        }
        if (violationTypes.length() == 0) {
            errorTextView.setText("You must choose a violation type.");
            errorTextView.setTextColor(Color.RED);
        }
        else { giveViolationTypes(violationTypes.toString()); }
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *
    public interface MainListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
