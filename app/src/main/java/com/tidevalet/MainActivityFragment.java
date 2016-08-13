package com.tidevalet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SessionManager session = new SessionManager(App.getInstance());
        if (session.propertySelected() != 0) {
           return inflater.inflate(R.layout.property_selected, container, false);
        }
        else {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }
    }
}
