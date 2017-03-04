package com.techpro.chat.ticklechat.models;

/**
 * Created by sagar on 06/08/16.
 */

public class CustomModel {

    private String message;

    private String body;

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserModel [message = " + message + ", body = " + body + ", status = " + status + "]";
    }
}
