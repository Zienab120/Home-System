package com.example.pervasive_projcect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;

public class EntryAttack extends AppCompatActivity {
    TextView attack;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_attack);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attack=findViewById(R.id.entryAttackTextView);

        email=getIntent().getStringExtra("email");
        //Intent intent=getIntent();
        //boolean connection=intent.getBooleanExtra("connection",false);
        //if(connection) {
        String google = "@gmail.com";
        String emailforfirebase = email.replaceAll(google, "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CurrentValues").child(emailforfirebase).child("attackAlert");
        DatabaseReference myEventRef = database.getReference("Users'Events").child(emailforfirebase);
        ValueEventListener Listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isAttacked = dataSnapshot.getValue(Boolean.class);
                String eventID=myEventRef.push().getKey();
                Event event;
                if(isAttacked) {
                    event=new Event(eventID,"EntryAttack","We Are In Attack!",(new Timestamp(System.currentTimeMillis()).toString()));
                    attack.setText("We Are In Attack!");
                }
                else {
                    event=new Event(eventID,"EntryAttack","We Are Safe:)",(new Timestamp(System.currentTimeMillis()).toString()));

                    attack.setText("We Are Safe:)");
                }
                myEventRef.child(eventID).setValue(event);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        myRef.addValueEventListener(Listener);
        // }



    }
}