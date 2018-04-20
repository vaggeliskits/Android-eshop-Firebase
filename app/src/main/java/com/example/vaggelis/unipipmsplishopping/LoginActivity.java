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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null)
        {
//            Login and
            // go ProfileActivity
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        textViewSignup = (TextView) findViewById(R.id.textViewSignUp);
        progressDialog = new ProgressDialog(this);

        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }
//  if textview is emty set Toast
    private void userLogin()
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
        // progressDialog with message
        progressDialog.setMessage("Registering User..");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful())
                        {
//                            Start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

//     ******************************************************************************************************//
                            // check and set the language by user settings
                            FirebaseUser fbuser = firebaseAuth.getCurrentUser();
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                    .getReference();
                            databaseReference.child("foo").child(fbuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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

//******************************************************************************************************//
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Please try again",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });
    }
// choice to signin or signup
    @Override
    public void onClick(View view) {
        if (view == buttonSignIn)
        {
            userLogin();
        }
        if (view == textViewSignup)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
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
