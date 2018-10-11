package io.lmh.e.a3cs_akings.Model;

public class Notification {
    private String id;
    private String reactername;

    public String getId() {
        return id;
    }

    public Notification(String id, String reactername, String reacterid, String notibody, String type, String postid, String seen, String date) {
        this.id = id;
        this.reactername = reactername;
        this.reacterid = reacterid;
        this.notibody = notibody;
        this.type = type;
        this.postid = postid;
        this.seen = seen;
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReactername() {
        return reactername;
    }

    public void setReactername(String reactername) {
        this.reactername = reactername;
    }

    public String getReacterid() {
        return reacterid;
    }

    public void setReacterid(String reacterid) {
        this.reacterid = reacterid;
    }

    public String getNotibody() {
        return notibody;
    }

    public void setNotibody(String notibody) {
        this.notibody = notibody;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String reacterid;
    private String notibody;
    private String type;
    private String postid;
    private String seen;
    private String date;
}
