package com.techpro.chat.ticklechat.models;

/**
 * Created by sagars on 10/26/16.
 */
public class GetGroupDetails {

    private String message;

    private GroupDetails body;

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GroupDetails getBody() {
        return body;
    }

    public void setBody(GroupDetails body) {
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

    public class GroupDetails {

        public Group getGroup() {
            return group;
        }

        public void setGroup(Group group) {
            this.group = group;
        }

        private Group group;
    }
}