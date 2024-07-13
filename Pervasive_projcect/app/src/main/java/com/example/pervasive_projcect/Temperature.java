package com.example.pervasive_projcect;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;

public class Temperature extends AppCompatActivity {

    TextView TextViewTemperature;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        email=getIntent().getStringExtra("email");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextViewTemperature=findViewById(R.id.temperatureTextView);
        // Intent intent=getIntent();
        //boolean connection=intent.getBooleanExtra("connection",false);

        // if(connection) {
        String google = "@gmail.com";
        String emailforfirebase = email.replaceAll(google, "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CurrentValues").child(emailforfirebase).child("Temperature");
        DatabaseReference myEventRef = database.getReference("Users'Events").child(emailforfirebase);
        ValueEventListener Listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String temperature = dataSnapshot.getValue(String.class);
                String eventID=myEventRef.push().getKey();
                Event event=new Event(eventID,"Temperature","Temperature is:"+temperature,(new Timestamp(System.currentTimeMillis()).toString()));
                myEventRef.child(eventID).setValue(event);
                TextViewTemperature.setText(temperature);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        myRef.addValueEventListener(Listener);
        //}

    }

}
