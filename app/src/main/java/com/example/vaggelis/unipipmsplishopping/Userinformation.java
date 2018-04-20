package com.example.vaggelis.unipipmsplishopping;

/**
 * Created by vaggelis on 22/02/17.
 */
// Userinformation to manage
    //the user information
    //(firstname ,lastname,color,language)
public class Userinformation {

    public String firstname;
    public String lastname;
    public String color;
    public String language;

    public Userinformation(String firstname, String lastname , String color , String language) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.color = color;
        this.language = language;
    }

    public String getColor()
    {
        return color;
    }

    public String getLanguage()
    {
        return language;
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
}
