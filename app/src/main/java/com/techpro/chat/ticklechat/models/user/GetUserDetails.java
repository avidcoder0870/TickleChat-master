package com.techpro.chat.ticklechat.models.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aryans05 on 04/08/16.
 */

public class GetUserDetails {
    @SerializedName("message")
    private String message;

    private GetUserDetailsBody body;

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GetUserDetailsBody getBody() {
        return body;
    }

    public void setBody(GetUserDetailsBody body) {
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
        return "GetUserDetails [message = " + message + ", body = " + body + ", status = " + status + "]";
    }
}
