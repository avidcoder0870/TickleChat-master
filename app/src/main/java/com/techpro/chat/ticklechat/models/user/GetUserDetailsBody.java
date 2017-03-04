package com.techpro.chat.ticklechat.models.user;

/**
 * Created by aryans05 on 04/08/16.
 */

public class GetUserDetailsBody {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "GetUserDetailsBody [user = " + user + "]";
    }
}
