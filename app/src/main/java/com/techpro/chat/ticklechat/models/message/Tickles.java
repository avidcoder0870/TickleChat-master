package com.techpro.chat.ticklechat.models.message;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sagar on 10/23/2016.
 */

public class Tickles implements Serializable{
    private String message;
    private String status;
    private MessageList body;

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public MessageList getBody() {
        return body;
    }
    public void setBody(MessageList body) {this.body = body;}


    @Override
    public String toString() {
        return "UserModel [message = " + message + ", body = " + body + ", status = " + status + "]";
    }


    public class MessageList implements Serializable{

        private ArrayList<ChatMessagesTicklesList> tickles;

        public ArrayList<ChatMessagesTicklesList> getTickles() {
            return tickles;
        }

        public void setTickles(ArrayList<ChatMessagesTicklesList> tickles) {
            this.tickles = tickles;
        }

        public class ChatMessagesTicklesList implements Serializable {

            private String id;
            private String message;
            private String requested_by;
            private String approved;
            private String requested_at;
            private String name;
            private String profile_image;
            private String userid;

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

            public String getRequested_by() {
                return requested_by;
            }

            public void setRequested_by(String requested_by) {
                this.requested_by = requested_by;
            }

            public String getApproved() {
                return approved;
            }

            public void setApproved(String approved) {
                this.approved = approved;
            }

            public String getRequested_at() {
                return requested_at;
            }

            public void setRequested_at(String requested_at) {
                this.requested_at = requested_at;
            }

            public String getName() {return name;}

            public void setName(String name) {this.name = name;}

            public String getProfile_image() { return profile_image; }

            public void setProfile_image(String profile_image) { this.profile_image = profile_image; }

            public String getUserid() {return userid; }

            public void setUserid(String userid) {this.userid = userid;}
        }
        // Getters setters
    }
}

