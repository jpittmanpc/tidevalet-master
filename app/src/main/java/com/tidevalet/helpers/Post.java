package com.tidevalet.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 4/22/16.
 */
public class Post {
    private long id, violationId, propertyId;
    private int isPosted;
    private String type, contractorComments, timestamp, returnedString, localImagePath;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getViolationId() {
        return violationId;
    }
    public void setPropertyId(long propertyId) { this.propertyId = propertyId; }
    public long getPropertyId() { return propertyId; }
    public void setViolationId(long violationId) {
        this.violationId = violationId;
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
    public String getViolationType() {
        return type;
    }
    public void setViolationType(String type) {
        this.type = type;
    }
    public String getContractorComments(String contractorComments) { return contractorComments; }
    public void setContractorComments(String contractorComments) {
        this.contractorComments = contractorComments;
    }
}
