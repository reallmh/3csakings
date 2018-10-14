package io.lmh.e.a3cs_akings.Model;

/**
 * Created by E on 7/21/2018.
 */

public class Comment {
    public String getAcc_id() {
        return acc_id;
    }

    public String getPost_id() {
        return post_id;
    }


    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public Comment(String id, String post_id, String acc_id, String acc_name, String comment_body, String date) {
        this.id = id;
        this.post_id = post_id;
        this.acc_id = acc_id;
        this.acc_name = acc_name;
        this.comment_body = comment_body;
        this.date = date;
    }

    public void setAcc_id(String acc_id) {
        this.acc_id = acc_id;

    }

    public String getAcc_name() {
        return acc_name;
    }

    public void setAcc_name(String acc_name) {
        this.acc_name = acc_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment_body() {
        return comment_body;
    }

    public void setComment_body(String comment_body) {
        this.comment_body = comment_body;
    }

    String id,post_id,acc_id,acc_name,comment_body,date;
}
