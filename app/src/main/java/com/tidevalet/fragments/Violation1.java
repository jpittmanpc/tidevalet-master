package com.tidevalet.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.SessionManager;
import com.tidevalet.helpers.Post;
import com.tidevalet.interfaces.ViolationListener;
import com.tidevalet.thread.adapter;

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
    private static final String POST_ID = "id";
    private static final String LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId";
    static long postId = -1;
    private int i = 0;
    private Post post = null;
    public ViolationListener vL;
    private TextView errorText;
    private ImageView img1,img2,img3,img4;
    private Bitmap img;
    static SessionManager sm;
    public Violation1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Violation1.
     */
    // TODO: Rename and change types and number of parameters
    public static Violation1 newInstance(long postId) {
        sm = new SessionManager(App.getAppContext());
        Violation1 fragment = new Violation1();
        Bundle args = new Bundle();
        args.putLong(POST_ID, postId);
        sm.setpostId(postId);
        fragment.setArguments(args);
        Log.d("viol1", postId + " newinstance");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = new SessionManager(App.getAppContext());
        if (getArguments() != null) {
            postId = sm.getpostId();
            Log.d("Viol1",postId + "");
            if (postId != -1) {
                adapter dBadapter = new adapter(this.getActivity());
                dBadapter.open();
                this.post = dBadapter.getPostById(postId);
                Log.d("onCreate", post.getBldg() + post.getUnit() + post.getViolationType() + "  .." + post.getTimestamp());
                dBadapter.close();
            }
        }
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.viol1, container, false);
        if (postId != -1) {
            Log.d("Viol1","post not null" + " bldg: " + post.getBldg());
            EditText bldg = (EditText) v.findViewById(R.id.bldg);
            bldg.setText(post.getBldg());
            EditText unit = (EditText) v.findViewById(R.id.unit);
            unit.setText(post.getUnit());
            TextView date = (TextView) v.findViewById(R.id.dateTxt);
            date.setText(post.getTimestamp().split(" ")[0]);
            TextView time = (TextView) v.findViewById(R.id.timeTxt);
            time.setText(post.getTimestamp().split(" ")[1]);
            String[] imagePath;
            try { imagePath = post.getImagePath().split(","); }
            catch(NullPointerException e) { imagePath = post.getLocalImagePath().split(","); }
            ImageLoader imgLoader = ImageLoader.getInstance();
            ImageSize size = new ImageSize(80,80);
            try {
                if (imagePath[0].charAt(0) == '/') {
                    imagePath[0] = "file://" + imagePath[0];
                }
                imgLoader.displayImage(imagePath[0].replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ",""), (ImageView) v.findViewById(R.id.img1), size);
            }
            catch (NullPointerException e) { e.printStackTrace(); }
        }
        else {
            EditText bldg = (EditText) v.findViewById(R.id.bldg);
            EditText unit = (EditText) v.findViewById(R.id.unit);
            bldg.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            bldg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSoftKeyboard(v);
                }
            });
            errorText = (TextView) v.findViewById(R.id.errorTextView1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mma", Locale.getDefault());
            TextView dateTxt = (TextView) v.findViewById(R.id.dateTxt);
            TextView timeTxt = (TextView) v.findViewById(R.id.timeTxt);
            ImageButton takePic = (ImageButton) v.findViewById(R.id.imageButton);
            takePic.setOnClickListener(this);
            dateTxt.setText(dateFormat.format(new Date()));
            timeTxt.setText(timeFormat.format(new Date()));
        }
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
    @Override
    public void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
    }
}
