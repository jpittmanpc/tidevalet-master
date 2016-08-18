package com.tidevalet.helpers;

import com.tidevalet.App;
import com.tidevalet.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Justin on 4/22/16.
 */
public class Properties {
    private long id;
    private String name;
    private String term_id, image, address, contractors;
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
    public boolean isContractor() {
        JSONArray list = new JSONArray();
        try {
            list = new JSONArray(contractors);
        }
        catch (JSONException e) { e.printStackTrace(); }
        SessionManager sm = new SessionManager(App.getInstance());
        return list.toString().contains(sm.getUserId().toString());
    }
    public void setContractors(String contractors) {
        this.contractors = contractors;
    }
}