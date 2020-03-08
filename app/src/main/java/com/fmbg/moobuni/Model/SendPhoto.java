package com.fmbg.moobuni.Model;

public class SendPhoto {
    private String sender;
    private String receiver;
    private String imageurl;

    public SendPhoto() {
    }

    public SendPhoto(String sender, String receiver, String imageurl) {
        this.sender = sender;
        this.receiver = receiver;
        this.imageurl = imageurl;
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
