package com.techpro.chat.ticklechat.models.message;

import java.io.Serializable;

/**
 * Created by Sagar on 10/30/2016.
 */

public class SendMessage implements Serializable{
    private String message;
    private String status;
    private ChatMessagesList body;

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public ChatMessagesList getBody() {
        return body;
    }
    public void setBody(ChatMessagesList body) {this.body = body;}


    @Override
    public String toString() {
        return "UserModel [message = " + message + ", body = " + body + ", status = " + status + "]";
    }


    public class ChatMessagesList  implements Serializable{

        private String id;
        private String message;
        private String from_id;
        private String to_id;
        private String read;
        private String sent;
        private String tickle_id;

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

        public String getSent() {
            return sent;
        }

        public void setSent(String sent) {
            this.sent = sent;
        }

        public String getTickle_id() {
            return tickle_id;
        }

        public void setTickle_id(String tickle_id) {
            this.tickle_id = tickle_id;
        }
    }
}
