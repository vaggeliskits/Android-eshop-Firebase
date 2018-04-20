package com.example.vaggelis.unipipmsplishopping;

/**
 * Created by vaggelis on 23/02/17.
 */
//Product class to manage
    // the product

public class Product {
    private String code;
    private String date;
    private String description;
    private String latitube;
    private String longitube;
    private String price;
    private String title;
    private String image;
    
    public Product(){
        
    }

    public Product(String code, String title, String price, String longitube, String latitube, String description, String date, String image) {
        this.code = code;
        this.title = title;
        this.price = price;
        this.longitube = longitube;
        this.latitube = latitube;
        this.description = description;
        this.date = date;
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitube() {
        return latitube;
    }

    public void setLatitube(String latitube) {
        this.latitube = latitube;
    }

    public String getLongitube() {
        return longitube;
    }

    public void setLongitube(String longitube) {
        this.longitube = longitube;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }
}


