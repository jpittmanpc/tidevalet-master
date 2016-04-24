package com.tidevalet.helpers;

/**
 * Created by Justin on 4/22/16.
 */
public class Post {
    private long id;
    private long pupilId;
    private String localImagePath;
    private int isPosted;
    private String returnedString;
    private String timestamp;
    private String grade;
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
    public String getLocalImagePath() {
        return localImagePath;
    }
    public void setLocalImagePath(String localImagePath) {
        this.localImagePath = localImagePath;
    }
    public int getIsPosted() {
        return isPosted;
    }
    public void setIsPosted(int isPosted) {
        this.isPosted = isPosted;
    }
    public String getReturnedString() {
        return returnedString;
    }
    public void setReturnedString(String returnedString) {
        this.returnedString = returnedString;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
}
