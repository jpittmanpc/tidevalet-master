package com.tidevalet.interfaces;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import java.util.List;

/**
 * Created by Justin on 8/15/2016.
 */
public interface ViolationListener {

    void clicked(View view);

    void violationTypes(String list, int PICKEDUP);

    void sendview(View v, int id);

    void sendComments(String s);

    void checkType(List<CheckBox> cbList);
}
