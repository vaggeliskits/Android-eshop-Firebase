package com.example.vaggelis.unipipmsplishopping;

import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by vaggelis on 23/02/17.
 */

// Addtobasket class for help add or
//remove product from basket

public class Addtobasket {
    private String code;
    private String date;
    private String description;
    private String latitube;
    private String longitube;
    private String price;
    private String title;
    private String image;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    Addtobasket()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

// Create add() to add product to the basket
    public String add(String code, String title, String price, String longitube, String latitube, String description, String date, String image)
    {
        Product product = new Product( code,  title, price, longitube, latitube, description, date, image);
        //get the user auth
        FirebaseUser addu = firebaseAuth.getCurrentUser();
        // add product in userbasket/id_user/product_title
        databaseReference.child("userbasket").child(addu.getUid()).child(product.getTitle()).setValue(product);
        String la = " ";

        return la;
    }

// Creata move() to remove product to the basket
    public String move(String code, String title, String price, String longitube, String latitube, String description, String date, String image)
    {
        //get the user auth
        FirebaseUser addu = firebaseAuth.getCurrentUser();
        Product product = new Product( code,  title, price, longitube, latitube, description, date, image);
        // remove product in userbasket/id_user/product_title
        databaseReference.child("userbasket").child(addu.getUid()).child(product.getTitle()).setValue(null);
        System.out.println(product.getTitle());
        String la = " ";

        return la;

    }


}
