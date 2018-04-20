package com.example.vaggelis.unipipmsplishopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSigin;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null)
        {
//            start profile activity
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)  findViewById(R.id.editTextPassword);
        textViewSigin = (TextView) findViewById(R.id.textViewSignin);

        buttonRegister.setOnClickListener(this);
        textViewSigin.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();

    }
// check textview if is empty
    private void registerUser()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
//            email is empty
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password))
        {
//            password is empty
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User..");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
//           start profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            // init user settings from first time
                            String fname = "Firstname";
                            String lname = "Lastname";
                            String ucolor = "white";
                            String ulanguage = "English";

                            Userinformation userinformation = new Userinformation(fname,lname,ucolor,ulanguage);
                            FirebaseUser fuser = firebaseAuth.getCurrentUser();
                            //set user settings by user id
                            databaseReference.child("foo").child(fuser.getUid()).setValue(userinformation);
//                            ****************************************************************************//
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                    .getReference();
                            // get user settings by id to set the language
                            databaseReference.child("foo").child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User _user = dataSnapshot.getValue(User.class);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("language",_user.getLanguage());
                                    editor.commit();
                                    String language = preferences.getString("language","");
                                    String languagetoload = "en";
                                    if (language.equalsIgnoreCase("Greek")
                                            ||language.equalsIgnoreCase("Ελληνικά")
                                            ||language.equalsIgnoreCase("Griego"))
                                    {
                                        languagetoload = "el";
                                    }
                                    else if (language.equalsIgnoreCase("English")
                                            ||language.equalsIgnoreCase("Αγγλικά")
                                            ||language.equalsIgnoreCase("Ingles"))
                                    {
                                        languagetoload = "en";
                                    }
                                    else {
                                        languagetoload = "es";
                                    }
                                    updateconfig(languagetoload);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

//                            ****************************************************************************//

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Please try again",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister)
        {
            registerUser();
        }
        if (view == textViewSigin)
        {
//            will open loginActivity
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
    // updateconfig to set the language
    private void updateconfig(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
        recreate();
    }
}
