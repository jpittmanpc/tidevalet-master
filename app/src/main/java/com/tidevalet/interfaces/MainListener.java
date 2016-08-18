package com.tidevalet.interfaces;

import android.net.Uri;
import android.view.View;

/**
 * Created by Justin on 8/15/2016.
 */
public interface MainListener {
    void clicked(View v);
    void authenticated();
    void showDialog(int i, String value);
    void propertyList(View v);

    void onFragmentInteraction(Uri uri);

    void propertyView(View v);
}
