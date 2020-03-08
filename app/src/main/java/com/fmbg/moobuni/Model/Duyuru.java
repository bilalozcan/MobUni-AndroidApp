package com.fmbg.moobuni.Model;

public class Duyuru {
    String day;
    String date;
    String title;
    String url;

    public Duyuru(String day, String date, String title, String url) {
        this.day = day;
        this.date = date;
        this.title = title;
        this.url = url;
    }

    public Duyuru() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
