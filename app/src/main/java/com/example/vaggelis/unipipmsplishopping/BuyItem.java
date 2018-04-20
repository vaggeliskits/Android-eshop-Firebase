package com.example.vaggelis.unipipmsplishopping;

/**
 * Created by vaggelis on 26/02/17.
 */

// buyitem class to help set
    //firstname ,lastname,product_code,timestap on buy click
public class BuyItem {
    private String firstname;
    private String lastname;
    private String code;
    private String time;

    public BuyItem(){

    }

    public BuyItem(String firstname,String lastname,String code, String time)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.code = code;
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
