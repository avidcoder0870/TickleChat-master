package com.techpro.chat.ticklechat.models.message;


/**
 * Created by Sagar on 10/23/2016.
 */

public class MessageDetails {

    public String getMessages() {
        return messages;
    }

    public void setMessages(String message) {
        this.messages = message;
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

    private String from_id;

    private String to_id;

    private String sentat;

    private String messages;

    private String id;
    private String read;
    private String tickle_id;
    private String isgroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "UserDetails [message = " + messages + ", from_id = " + from_id + ", to_id = " + to_id + ", chatUserList = " + id + ", read = " +
                read + ", tickle_id = " + tickle_id + ", isgroup = " + isgroup + ", sentat = " + sentat + "]";
    }
}
