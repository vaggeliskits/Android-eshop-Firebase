package com.example.vaggelis.unipipmsplishopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Locale;

public class UserSettings extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextfistname, editTextlastname;
    private Button buttonsave;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;

    static UsersAdapter adapter = null;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        databaseReference = FirebaseDatabase.getInstance().getReference();
        editTextfistname = (EditText) findViewById(R.id.editTextfirstname);
        editTextlastname = (EditText) findViewById(R.id.editTextlastname);
        buttonsave = (Button) findViewById(R.id.buttonsave);
        final ListView listview = (ListView) findViewById(R.id.listview);
        buttonsave.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        FirebaseUser fbuser = firebaseAuth.getCurrentUser();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference();
        // get user settings by id
        databaseReference.child("foo").child(fbuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TEST","key: "+dataSnapshot.getKey());
                Log.d("TEST","value: "+dataSnapshot.getValue());

                User _user = dataSnapshot.getValue(User.class);

                // use sharedpreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("color",_user.getColor());
                editor.putString("language",_user.getLanguage());
                editor.putString("fname",_user.getFirstname());
                editor.putString("lname",_user.getLastname());
                editor.commit();



                String _fname = preferences.getString("fname","");
                String _lname = preferences.getString("lname","");
                editTextfistname.setText(_fname);
                editTextlastname.setText(_lname);
                // check and set the color
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


                // set spinner position
                Spinner colorspin = (Spinner) findViewById(R.id.colorspin);
                Spinner languagespin = (Spinner) findViewById(R.id.languagespin);
                int selectioncolor;
                int selectionlanguage;
                if (color.equalsIgnoreCase("white")
                        ||color.equalsIgnoreCase("Λευκό")
                        ||color.equalsIgnoreCase("Blanco"))
                {
                    selectioncolor = 0;
                }
                else if (color.equalsIgnoreCase("blue")
                        ||color.equalsIgnoreCase("Μλέ")
                        ||color.equalsIgnoreCase("Azul"))
                {
                    selectioncolor = 1;
                }
                else {
                    selectioncolor = 2;
                }
                //*******************************************************************************************//
                String languagetoload;
                //set spinner position
                if (language.equalsIgnoreCase("Greek")
                        ||language.equalsIgnoreCase("Ελληνικά")
                        ||language.equalsIgnoreCase("Griego"))
                {
                    selectionlanguage = 0;
                    languagetoload = "de";
                }
                else if (language.equalsIgnoreCase("English")
                        ||language.equalsIgnoreCase("Αγγλικά")
                        ||language.equalsIgnoreCase("Ingles"))
                {
                    selectionlanguage = 1;
                    languagetoload = "de";
                }
                else {
                    selectionlanguage = 2;
                    languagetoload = "de";
                }
                colorspin.setSelection(selectioncolor);
                languagespin.setSelection(selectionlanguage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void saveUserInformation() {
        Spinner colorspin = (Spinner) findViewById(R.id.colorspin);
        Spinner languagespin = (Spinner)findViewById(R.id.languagespin);
        String fname = editTextfistname.getText().toString().trim();
        String lname = editTextlastname.getText().toString().trim();
        String ulanguage = languagespin.getSelectedItem().toString();
        String ucolor = colorspin.getSelectedItem().toString();


        Userinformation userinformation = new Userinformation(fname, lname,ucolor,ulanguage);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        // save the user settings in foo/user_id
        databaseReference.child("foo").child(user.getUid()).setValue(userinformation);

        Toast.makeText(this, "Informations Saved...", Toast.LENGTH_SHORT).show();

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



        startActivity(new Intent(this,ProfileActivity.class));
    }
// set language
    private void updateconfig(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
        recreate();
    }



    @Override
    public void onClick(View view) {

        if (view == buttonsave) {
            saveUserInformation();
        }

    }


}
