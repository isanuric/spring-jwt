package com.isanuric.bar.ldap;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;

/*
 * Project: bar
 * @author  ehsan.salmani@ic-consult.de on 14/11/2018.
 */
//@Entity
public class User {

    private String username;
    private String password;
    private String uid;


    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String uid) {
        this.username = username;
        this.password = password;
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
