package com.example.pervasive_projcect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;


public class Password extends AppCompatActivity {
    TextInputEditText editTextPassword,editTextConfirmPassword;
    Button buttonCreate;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email=getIntent().getStringExtra("email");

        editTextPassword=findViewById(R.id.Password);
        editTextConfirmPassword=findViewById(R.id.ConfirmPassword);
        buttonCreate=findViewById(R.id.btn_create);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent=getIntent();
                //boolean connection=intent.getBooleanExtra("connection",false);
                //if(connection) {
                String password, confirmPassword;
                password = String.valueOf(editTextPassword.getText());
                confirmPassword = String.valueOf(editTextConfirmPassword.getText());
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Password.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 5) {
                    Toast.makeText(Password.this, "Password is too short", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(Password.this, "Enter ConfirmPassword", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(password.equals(confirmPassword))) {
                    Toast.makeText(Password.this, "Password and ConfirmPassword should be identical!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String google = "@gmail.com";
                String emailforfirebase = email.replaceAll(google, "");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("CurrentValues").child(emailforfirebase).child("HomePassword");
                myRef.setValue(password);

                DatabaseReference myEventRef = database.getReference("Users'Events").child(emailforfirebase);
                String eventID=myEventRef.push().getKey();
                Event event=new Event(eventID,"Password","Home Password is changed",(new Timestamp(System.currentTimeMillis()).toString()));
                myEventRef.child(eventID).setValue(event);

                editTextPassword.setText("");
                editTextConfirmPassword.setText("");
                Toast.makeText(Password.this, "Password Created successfully", Toast.LENGTH_SHORT).show();
                //  intent = new Intent(Password.this, MainActivity.class);
                //   startActivity(intent);
                //  }
                //   else
                //     Toast.makeText(Password.this, "Failed to save changes. No Internet Connection!", Toast.LENGTH_SHORT).show();

            }
        });

    }
}