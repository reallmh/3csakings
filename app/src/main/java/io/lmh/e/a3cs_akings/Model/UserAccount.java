package io.lmh.e.a3cs_akings.Model;

/**
 * Created by E on 5/18/2018.
 */

public class UserAccount {
    String us_id;
    String us_name;
    String us_password;
    String us_phno;
    String us_followed;

    public UserAccount(String us_id, String us_name) {
        this.us_id = us_id;
        this.us_name = us_name;
    }

    public UserAccount(String us_id, String us_name, String us_password, String us_phno) {
        this.us_id = us_id;
        this.us_name = us_name;
        this.us_password = us_password;
        this.us_phno = us_phno;
    }

    public UserAccount() {
    }

    public String getUs_id() {
        return us_id;
    }

    public void setUs_id(String us_id) {
        this.us_id = us_id;
    }

    public String getUs_name() {
        return us_name;
    }

    public void setUs_name(String us_name) {
        this.us_name = us_name;
    }

    public UserAccount(String us_id, String us_name, String us_followed) {
        this.us_id = us_id;
        this.us_name = us_name;
        this.us_followed = us_followed;
    }

    public String getUs_password() {
        return us_password;
    }

    public void setUs_password(String us_password) {
        this.us_password = us_password;
    }

    public String getUs_phno() {
        return us_phno;
    }

    public void setUs_phno(String us_phno) {
        this.us_phno = us_phno;
    }

    public String getUs_followed() {
        return us_followed;
    }

    public void setUs_followed(String us_followed) {
        this.us_followed = us_followed;
    }
}
