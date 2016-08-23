package com.tidevalet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tidevalet.App;
import com.tidevalet.R;
import com.tidevalet.activities.MainActivity;
import com.tidevalet.helpers.Post;
import com.tidevalet.SessionManager;
import com.tidevalet.interfaces.MainListener;
import com.tidevalet.thread.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link MainListener}
 * interface.
 */
public class ViewViolations extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MainListener mListener;
    private List<Post> posts = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ViewViolations() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ViewViolations newInstance(int columnCount) {
        ViewViolations fragment = new ViewViolations();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter dBadapter = new adapter(App.getAppContext());
        SessionManager sm = new SessionManager(App.getAppContext());
        try {
            dBadapter.open();
            posts = dBadapter.getPostsByPropertyId(sm.propertySelected());
            dBadapter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!posts.isEmpty()) {
            View view = inflater.inflate(R.layout.fragment_view_violations, container, false);
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            recyclerView.setHasFixedSize(false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation((LinearLayoutManager.VERTICAL));
            recyclerView.setLayoutManager(layoutManager);
            //PostViewAdapter postViewAdapter = new PostViewAdapter(PostViewAdapter.posts, mListener);
            recyclerView.setAdapter(new PostViewAdapter(posts, mListener));
            recyclerView.invalidate();
            return view;
        }
        else {
            View v = inflater.inflate(R.layout.noproperties, container, false);
            Button goBack = (Button) v.findViewById(R.id.backbutton);
            goBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                    v.invalidate();
                }
            });
            return v;
        }
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
    public void onDetach() {
        super.onDetach();
         mListener = null;
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
     */
    /*public interface MainListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }*/
}
