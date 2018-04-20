package com.example.vaggelis.unipipmsplishopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class WatchActivity extends AppCompatActivity {

    SharedPreferences preferences;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Product> products;
    static ProductAdapter adapter = null;

    public WatchActivity() {
        products = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);


        TextView textViewwatchlist = (TextView) findViewById(R.id.watchviewEmail);
        Button buttonwatchlist = (Button) findViewById(R.id.watchgotobasket);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //set background color
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
        getWindow().getDecorView().setBackgroundColor(Color.parseColor(""+color+""));
        //******************************************************************************************//
        // set language
        Userinformation userinformation = new Userinformation(null, null,null,language);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("color",userinformation.getColor());
        editor.putString("language",userinformation.getLanguage());
        editor.putString("fname",userinformation.getFirstname());
        editor.putString("lname",userinformation.getLastname());
        editor.commit();
        String lag = "";
        if (userinformation.getLanguage().equalsIgnoreCase("Ελληνικά")
                || userinformation.getLanguage().equalsIgnoreCase("Griego")
                || userinformation.getLanguage().equalsIgnoreCase("Greek"))
        {
            lag = "el";
        }
        else if (userinformation.getLanguage().equalsIgnoreCase("Ισπανικά")
                || userinformation.getLanguage().equalsIgnoreCase("Espanol")
                || userinformation.getLanguage().equalsIgnoreCase("Spanish"))
        {
            lag = "es";
        }
        else {
            lag = "en";
        }
        updateconfig(lag);
//*****************************************************************************************//

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null)
        {
            finish();
            //start loginActivity
            startActivity(new Intent(this,LoginActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //init listview and addaper
        final ListView listView = (ListView) findViewById(R.id.listviewwatch);
        products = new ArrayList<>();
        adapter =new ProductAdapter(this,products);
        listView.setAdapter(adapter);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference();
        //get items from the watchlist
        databaseReference.child("watchlist").child(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("TEST","key: "+dataSnapshot.getKey());
                Log.d("TEST","value: "+dataSnapshot.getValue());

                Product _product = dataSnapshot.getValue(Product.class);
                products.add(_product);
                adapter.notifyDataSetChanged();
                listView.invalidate();


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

            }
        });

        FirebaseUser fbuser = firebaseAuth.getCurrentUser();
        //get color and language
        databaseReference.child("foo").child(fbuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User _user = dataSnapshot.getValue(User.class);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("color",_user.getColor());
                editor.putString("language",_user.getLanguage());
                editor.commit();
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
                getWindow().getDecorView().setBackgroundColor(Color.parseColor(color));

                //*******************************************************************************************//
                String lag = "";
                if (_user.getLanguage().equalsIgnoreCase("Ελληνικά")
                        || _user.getLanguage().equalsIgnoreCase("Griego")
                        || _user.getLanguage().equalsIgnoreCase("Greek"))
                {
                    lag = "el";
                }
                else if (_user.getLanguage().equalsIgnoreCase("Ισπανικά")
                        || _user.getLanguage().equalsIgnoreCase("Espanol")
                        || _user.getLanguage().equalsIgnoreCase("Spanish"))
                {
                    lag = "es";
                }
                else {
                    lag = "en";
                }
                updateconfig(lag);
//*******************************************************************************************//

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void gotobasket(View view)
    {
        //start basket activity
        startActivity(new Intent(this,Basket.class));

    }
    //set language
    private void updateconfig(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
    }
}
