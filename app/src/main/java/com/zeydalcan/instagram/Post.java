package com.zeydalcan.instagram;

public class Post {
    String downloadUrl;
    String userEmail;
    String comment;
    String postId;

    public Post(String downloadUrl, String userEmail, String comment,String postId) {
        this.downloadUrl = downloadUrl;
        this.userEmail = userEmail;
        this.comment = comment;
        this.postId=postId;

    }
}
