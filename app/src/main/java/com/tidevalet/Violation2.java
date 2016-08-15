package com.tidevalet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViolationListener} interface
 * to handle interaction events.
 * Use the {@link Violation2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Violation2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public ViolationListener violationListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CheckBox cb1;

//    private OnFragmentInteractionListener violationListener;

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
        cb1 = (CheckBox)v.findViewById(R.id.cb1);
        return v;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViolationListener) {
            violationListener = (ViolationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        if (cb1.isChecked()) {
            violationTypes((String) cb1.getText());
        }
        super.onDetach();

        violationListener = null;
    }
    public void violationTypes(String string) {
        if (violationListener != null) {
            violationListener.violationTypes(string);
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
