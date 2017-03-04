package com.techpro.chat.ticklechat.models;

/**
 * Created by sagars on 10/19/16.
 */
public class Messages {
    public int _id;
    public String message = "";
    public int requested_by = 0;
    public int approved = 0;
    public String modified_at= "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRequested_by() {
        return requested_by;
    }

    public void setRequested_by(int requested_by) {
        this.requested_by = requested_by;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    // constructor
    public Messages(int id, String message,int requested_by, int approved, String modified_at) {
        this._id = id;
        this.message = message;
        this.requested_by = requested_by;
        this.approved = approved;
        this.modified_at = modified_at;
    }

    public Messages() {

    }
}
