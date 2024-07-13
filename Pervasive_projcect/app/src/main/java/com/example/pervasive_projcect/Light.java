package com.example.pervasive_projcect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;

public class Light extends AppCompatActivity {
    Switch aSwitch;
    TextView light;
    Boolean checked;
    String email;
    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        aSwitch=findViewById(R.id.light_on_off);
        light=findViewById(R.id.light);

        email=getIntent().getStringExtra("email");


    }
    public void onSwitchClick(View view){
        String google = "@gmail.com";
        String emailforfirebase = email.replaceAll(google, "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CurrentValues").child(emailforfirebase).child("Light");
        DatabaseReference myEventRef = database.getReference("Users'Events").child(emailforfirebase);
        String eventID=myEventRef.push().getKey();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    light.setText("ON");
                    checked=true;
                    event=new Event(eventID,"Light","Light ON",(new Timestamp(System.currentTimeMillis()).toString()));

                }
                else{
                    light.setText("OFF");
                    checked=false;
                    event=new Event(eventID,"Light","Light OFF",(new Timestamp(System.currentTimeMillis()).toString()));
                }

            }
        });

        myRef.setValue(checked);
        myEventRef.child(eventID).setValue(event);

    }
}