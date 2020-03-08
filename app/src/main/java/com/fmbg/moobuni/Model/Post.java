package com.fmbg.moobuni.Model;

public class Post {

    private String postimage;
    private String postid;
    private String description;
    private String publisher;
    private String postuniversityname;

    public Post(String postimage, String postid, String description, String publisher) {
        this.postimage = postimage;
        this.postid = postid;
        this.description = description;
        this.publisher = publisher;
    }

    public Post() {
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPostuniversityname() {
        return postuniversityname;
    }

    public void setPostuniversityname(String postuniversityname) {
        this.postuniversityname = postuniversityname;
    }
}
