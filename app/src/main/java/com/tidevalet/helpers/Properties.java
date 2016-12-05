package com.tidevalet.helpers;

import android.util.Log;

import com.tidevalet.App;
import com.tidevalet.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Justin on 4/22/16.
 */
public class Properties {
    private long id;
    private String name;
    private String term_id, image, address, contractors, complexmgrs;
    public void setImage(String image) {
        this.image = image;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
      this.address = address;
    }
    public String getImage() {
        return image;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContractors() {
        return contractors;
    }
    public Boolean isContractor() {
        SessionManager sM = new SessionManager(App.getInstance());
        Boolean isContractor = false;
        String test = contractors.replaceAll("\"", "");
        test = test.substring(1,test.length()-1);
        Log.d("Contractor", sM.getUserId() + " id");
        for (String isCont : test.split(",")) {
            if (isCont.equals(sM.getUserId())) { isContractor = true; }
        }
        return isContractor;
    }
    public void setContractors(String contractors) {
        this.contractors = contractors;
    }
    public void setComplexMgrs(String complexMgrs) {
        this.complexmgrs = complexMgrs;
    }
    public String getComplexMgrs() {
        return complexmgrs;
    }
}