package com.zeydalcan.instagram;

public class Comment {

    public String username;
    public String comment;
    String postId;

    public Comment(String username, String comment, String postId) {

        this.username = username;
        this.comment = comment;
        this.postId=postId;
    }


}
