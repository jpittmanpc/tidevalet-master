package com.tidevalet.helpers;

/**
 * Created by Justin on 4/22/16.
 */
public class Attributes {
    public static final int TYPE_PRIMARYBLOGGER = 0;
    public static final int SERVICE_DISABLED = 0;
    public static final int SERVICE_ENABLED = 1;
    public static final int USE_DEFAULT = 0;
    public static final int USE_CUSTOM = 1;
    private long id;
    private long propertyId;
    private long serviceId;
    private int isEnabled;
    private String nickname;
    private String url;
    private String username;
    private String password;
    private int useDefault;
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
    public long getServiceId() {
        return serviceId;
    }
    public void setViolationId(long serviceId) {
        this.serviceId = serviceId;
    }
    public int getIsEnabled() {
        return isEnabled;
    }
    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
