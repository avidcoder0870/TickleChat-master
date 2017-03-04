package com.techpro.chat.ticklechat.models.message;

import java.io.Serializable;

/**
 * Created by sagars on 11/23/16.
 */
public class NotificationMessage implements Serializable {

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getSentat() {
        return sentat;
    }

    public void setSentat(String sentat) {
        this.sentat = sentat;
    }

    public String getIsgroup() {
        return isgroup;
    }

    public void setIsgroup(String isgroup) {
        this.isgroup = isgroup;
    }

    private String from_name;
    private String id;
    private String message;
    private String from_id;
    private String to_id;
    private String read;
    private String sentat;
    private String tickle_id;
    private String isgroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }


    public String getTickle_id() {
        return tickle_id;
    }

    public void setTickle_id(String tickle_id) {
        this.tickle_id = tickle_id;
    }
}
