package com.example.vaggelis.unipipmsplishopping;

/**
 * Created by vaggelis on 22/02/17.
 */

//User class to manage the users settings
public class User {
    private String firstname;
    private String lastname;
    private String color;
    private String language;

    public User() {
    }

    public User(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.color = color;
        this.language = language;
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getColor()
    {
        return color;
    }

    public String getLanguage()
    {
        return language;
    }

}