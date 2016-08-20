package com.tidevalet.interfaces;

import android.net.Uri;
import android.view.View;

import com.tidevalet.helpers.Post;

/**
 * Created by Justin on 8/15/2016.
 */
public interface MainListener {
    void clicked(View v);
    void authenticated();
    void showDialog(int i, String value);
    void propertyList(View v);

    void onFragmentInteraction(Uri uri);
    void onListFragmentInteraction(Post item);
    void propertyView(View v);
}
