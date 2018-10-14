package io.lmh.e.a3cs_akings.Model;

import java.io.Serializable;

/**
 * Created by E on 7/10/2018.
 */

public class Post implements Serializable{
    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public Post(String post_id, String acc_id, String acc_name, String post_body, String date, String comments, String likes, String isphoto, String liked) {
        this.post_id = post_id;
        this.acc_id = acc_id;
        this.acc_name = acc_name;
        this.post_body = post_body;
        this.date = date;
        this.comments = comments;
        this.likes = likes;
        this.isphoto = isphoto;
        this.liked = liked;
    }

    private String post_id,acc_id,acc_name,post_body,date,likes,comments,isphoto,liked;

    public String getAcc_name() {
        return acc_name;
    }

    public void setAcc_name(String acc_name) {
        this.acc_name = acc_name;
    }

    public String getIsphoto() {
        return isphoto;
    }

    public void setIsphoto(String isphoto) {
        this.isphoto = isphoto;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(String acc_id) {
        this.acc_id = acc_id;
    }

    public String getPost_body() {
        return post_body;
    }

    public void setPost_body(String post_body) {
        this.post_body = post_body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    public Post() {
    }
}
