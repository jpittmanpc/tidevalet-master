package com.tidevalet.helpers;

/**
 * Created by Justin on 4/22/16.
 */
public class Attributes {
    public static final int TYPE_PRIMARYBLOGGER = 0;
    private long id, propertyId, violationId;
    private String url;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getPropertyId() {
        return propertyId;
    }
    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }
    public long getViolationId() {
        return violationId;
    }
    public void setViolationId(long serviceId) {
        this.violationId = serviceId;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
