package com.fmbg.moobuni.Model;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private String messagephotourl;
    private String messagetype;
    private String messagetime;

    public Chat(String sender, String receiver, String message, String messagephotourl, String messagetype, String messagetime) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.messagephotourl = messagephotourl;
        this.messagetype = messagetype;
        this.messagetime = messagetime;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessagephotourl() {
        return messagephotourl;
    }

    public void setMessagephotourl(String messagephotourl) {
        this.messagephotourl = messagephotourl;
    }

    public String getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    public String getMessagetime() {
        return messagetime;
    }

    public void setMessagetime(String messagetime) {
        this.messagetime = messagetime;
    }

}