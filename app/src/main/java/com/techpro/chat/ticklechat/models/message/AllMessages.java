package com.techpro.chat.ticklechat.models.message;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sagar on 10/23/2016.
 */

public class AllMessages implements Serializable{
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


    public class MessageList  implements Serializable{


        public ArrayList<ChatMessagesList> getMessages() {
            return messages;
        }

        public void setMessages(ArrayList<ChatMessagesList> messages) {
            this.messages = messages;
        }

        private ArrayList<ChatMessagesList> messages;
        private ArrayList<ChatMessagesTicklesList> tickles;

        public ArrayList<ChatMessagesTicklesList> getTickles() {
            return tickles;
        }

        public void setTickles(ArrayList<ChatMessagesTicklesList> tickles) {
            this.tickles = tickles;
        }

        public class ChatMessagesList  implements Serializable{

            private String id;
            private String from_id;
            private String to_id;
            private String sentat;
            private String message;
            private String read;
            private String tickle_id;
            private String isgroup;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getSentat() {
                return sentat;
            }

            public void setSentat(String sentat) {
                this.sentat = sentat;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
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

            public String getIsgroup() {
                return isgroup;
            }

            public void setIsgroup(String isgroup) {
                this.isgroup = isgroup;
            }
        }

        public class ChatMessagesTicklesList  implements Serializable{

            private String id;
            private String message;
            private String requested_by;
            private String approved;
            private String added_at;
            private String requested_at;
            private String modified_at;
            private String updated_at;

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

            public String getAdded_at() {
                return added_at;
            }

            public void setAdded_at(String added_at) {
                this.added_at = added_at;
            }

            public String getRequested_at() {
                return requested_at;
            }

            public void setRequested_at(String requested_at) {
                this.requested_at = requested_at;
            }

            public String getModified_at() {
                return modified_at;
            }

            public void setModified_at(String modified_at) {
                this.modified_at = modified_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }
        }
        // Getters setters
    }
}

