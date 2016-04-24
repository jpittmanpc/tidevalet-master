package com.tidevalet.helpers;

/**
 * Created by Justin on 4/22/16.
 */
public class Attributes {
    public static final int TYPE_PRIMARYBLOGGER = 0;
    public static final int TYPE_XPARENA = 1;
    public static final int SERVICE_DISABLED = 0;
    public static final int SERVICE_ENABLED = 1;
    public static final int USE_DEFAULT = 0;
    public static final int USE_CUSTOM = 1;
    private long id;
    private long pupilId;
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
    public long getPupilId() {
        return pupilId;
    }
    public void setPupilId(long pupilId) {
        this.pupilId = pupilId;
    }
    public long getServiceId() {
        return serviceId;
    }
    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }
    public int getIsEnabled() {
        return isEnabled;
    }
    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getUseDefault() {
        return useDefault;
    }
    public void setUseDefault(int useDefault) {
        this.useDefault = useDefault;
    }
}
