package com.example.vaggelis.unipipmsplishopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Basket extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    static BasketAdapter adapterbasket = null;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String color = preferences.getString("color","");
        String language = preferences.getString("language","");

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null)
        {
            finish();
//            return to LoginActivity if is not Auth..
            startActivity(new Intent(this,LoginActivity.class));
        }
//      init listciew and arraylist
        final ListView listViewbasket = (ListView) findViewById(R.id.listviewbasket);
        final ArrayList<Product> basketproducts = new ArrayList<>();
//      init adapter(BasketAdapter)
        adapterbasket = new BasketAdapter(this,basketproducts);
        listViewbasket.setAdapter(adapterbasket);
        FirebaseUser addu = firebaseAuth.getCurrentUser();
//********************************************************************************************************//
        // Get all the product by user_id
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference();
        databaseReference.child("userbasket").child(addu.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product basketproduct = dataSnapshot.getValue(Product.class);
                basketproducts.add(basketproduct);
                adapterbasket.notifyDataSetChanged();
                listViewbasket.invalidate();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Product basketproduct = dataSnapshot.getValue(Product.class);
                basketproducts.add(basketproduct);
                adapterbasket.notifyDataSetChanged();
                listViewbasket.invalidate();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Product basketproduct = dataSnapshot.getValue(Product.class);
                basketproducts.add(basketproduct);
                adapterbasket.notifyDataSetChanged();
                listViewbasket.invalidate();
                refresh();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Product basketproduct = dataSnapshot.getValue(Product.class);
                basketproducts.add(basketproduct);
                adapterbasket.notifyDataSetChanged();
                listViewbasket.invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//********************************************************************************************************//
//        Get all the user settings by id(Color,language,firstname,lastname)
        FirebaseUser fbuser = firebaseAuth.getCurrentUser();
        databaseReference.child("foo").child(fbuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User _user = dataSnapshot.getValue(User.class);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("color",_user.getColor());
                editor.putString("language",_user.getLanguage());
                editor.commit();
                // check to set the backgroundcolor
                String color = preferences.getString("color","");
                String language = preferences.getString("language","");
                if (color.equalsIgnoreCase("White")
                        ||color.equalsIgnoreCase("Λευκό")
                        ||color.equalsIgnoreCase("Blanco"))
                {
                    color = "white";
                }
                else if (color.equalsIgnoreCase("Blue")
                        ||color.equalsIgnoreCase("Μλέ")
                        ||color.equalsIgnoreCase("Azul"))
                {
                    color = "blue";
                }
                else {
                    color = "gray";
                }
                // set the backgroundcolor
                getWindow().getDecorView().setBackgroundColor(Color.parseColor(color));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//    apprefresh() recreate the activity
    public static void apprefresh(AppCompatActivity appCompatActivity)
    {
        appCompatActivity.recreate();

    }

    public void refresh()
    {
        apprefresh(this);
    }

//    buy button on click
    public void buyitemsonclick(View view)
    {
        Toast.makeText(this,"Τransaction Complete",Toast.LENGTH_SHORT).show();
        //timestap
        Long tsLong = System.currentTimeMillis()/1000;
        final String ts = tsLong.toString();

        FirebaseUser addu = firebaseAuth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference();
//      get products by user id(basket products)
        databaseReference.child("userbasket").child(addu.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Product product = dataSnapshot.getValue(Product.class);
                User user = dataSnapshot.getValue(User.class);
                BuyItem buyItem = new BuyItem(user.getFirstname(),user.getLastname(),product.getCode(),ts);
                FirebaseUser addu = firebaseAuth.getCurrentUser();
                // set the products in buyitems/user_id/product_title
                databaseReference.child("buyitems").child(addu.getUid()).child(product.getTitle()).setValue(buyItem);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }//remove the products from user basket(by id)
        }); databaseReference.child("userbasket").child(addu.getUid()).setValue(null);
            recreate();

    }


}
