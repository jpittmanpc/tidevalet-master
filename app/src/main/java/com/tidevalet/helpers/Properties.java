package com.tidevalet.helpers;

/**
 * Created by Justin on 4/22/16.
 */
public class Properties {
    private long id;
    private String name;
    private String term_id, image, address;
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
}