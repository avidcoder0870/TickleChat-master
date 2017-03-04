package com.techpro.chat.ticklechat.models.user;

/**
 * Created by aryans05 on 06/08/16.
 */

public class UserModel {

    private String message;

    private UserDetailsModel body;

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDetailsModel getBody() {
        return body;
    }

    public void setBody(UserDetailsModel body) {
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
