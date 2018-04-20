package com.example.vaggelis.unipipmsplishopping;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.NotificationManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.Manifest;




import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener,LocationListener{

    LocationManager lm;
    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;

    private Toolbar toolbar;

    static ProductAdapter adapter = null;

    SharedPreferences preferences;
    private ArrayList<Product> products;

    public ProfileActivity() {
        products = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // init location
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        golocation(null);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String color = preferences.getString("color","");
        String language = preferences.getString("language","");
        // set the background color
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

        // use sharedpreferences to user settings
        Userinformation userinformation = new Userinformation(null, null,null,language);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("color",userinformation.getColor());
        editor.putString("language",userinformation.getLanguage());
        editor.putString("fname",userinformation.getFirstname());
        editor.putString("lname",userinformation.getLastname());
        editor.commit();
        String lag = "";
        //set the language
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
        toolbar  = (Toolbar) findViewById(R.id.toolbarSet);
        setSupportActionBar(toolbar);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null)
        {
            //start loginActivity
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        textViewUserEmail.setText("Welcome "+user.getEmail());

        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(this);
        //init listview and arraylist
        final ListView listView = (ListView) findViewById(R.id.listviewprofile);
        products = new ArrayList<>();

        // init adapter
        adapter =new ProductAdapter(this,products);
        listView.setAdapter(adapter);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference();
        // get all the product
        databaseReference.child("product").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("TEST","key: "+dataSnapshot.getKey());
                Log.d("TEST","value: "+dataSnapshot.getValue());


                Product _product = dataSnapshot.getValue(Product.class);
                // set product to arraylist
                products.add(_product);
                //addapter draw the UI
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
        // get user settinge by user id
        //get color by user
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
                //get language by user
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
        //*************************************************************************//


    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout)
        {
            recreate();
            firebaseAuth.signOut();
            finish();
            //start login activity
            startActivity(new Intent(this,LoginActivity.class));

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout)
        {
//            logout
            firebaseAuth.signOut();
            finish();
            //start login activity
            startActivity(new Intent(this,LoginActivity.class));
        }
        if  (id == R.id.setting)
        {
            //start usersettinge activity
            startActivity(new Intent(this,UserSettings.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //create option menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void gotobasket(View view)
    {
        //start basket activity
        startActivity(new Intent(this,Basket.class));

    }
    //set the language
    private void updateconfig(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
        //recreate();
    }
//create notification with lacation
    public void notify_product(String text)
    {
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.eshop);
        mBuilder.setContentTitle("Buy this Item");
        mBuilder.setContentText(text);
        // on click notification
        //start watchlist activity
        Intent intent=new Intent(this,WatchActivity.class);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,12,intent
                ,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager=
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(123,mBuilder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==8){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                golocation(null);
            }
        }
    }
// use the location
    public void golocation(View view)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},8);
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
//set the dafault location
        float[] results = new float[1];
        double unipilat = 37.941544;
        double unipilong = 23.652592;
//Rimini and lykovriseos location Store in Lykovrisi//
        double Store1lat = 38.071684 ,Store1long = 23.783596;

        Location.distanceBetween(location.getLatitude(),location.getLongitude(),
                Store1lat,Store1long,results);
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference();
        if (results[0] <100)//if user < 100 meter from the store
        {
            notify_product("Watch this List");
            // create the watchlist
            databaseReference.child("product").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Product _product = dataSnapshot.getValue(Product.class);
                    if (_product.getTitle().equalsIgnoreCase("Google Pixel"))
                    {
                        databaseReference.child("watchlist").child(user.getUid()).child(_product.getTitle()).setValue(_product);
                    }
                    if (_product.getTitle().equalsIgnoreCase("HTC 10"))
                    {
                        databaseReference.child("watchlist").child(user.getUid()).child(_product.getTitle()).setValue(_product);
                    }

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
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
