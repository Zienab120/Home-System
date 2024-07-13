package com.example.pervasive_projcect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;

public class Message extends AppCompatActivity {
    EditText m;
    String message;
    Button send;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email=getIntent().getStringExtra("email");

        m=findViewById(R.id.message_editText);

        send=findViewById(R.id.message_send_btn);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=getIntent();
                //  boolean connection=intent.getBooleanExtra("connection",false);
                // if(connection) {
                String google = "@gmail.com";
                String emailforfirebase = email.replaceAll(google, "");
                message = String.valueOf(m.getText());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("CurrentValues").child(emailforfirebase).child("welcomeMessage");
                myRef.setValue(message);

                DatabaseReference myEventRef = database.getReference("Users'Events").child(emailforfirebase);
                String eventID=myEventRef.push().getKey();
                Event event=new Event(eventID,"Message","Sending Message",(new Timestamp(System.currentTimeMillis()).toString()));
                myEventRef.child(eventID).setValue(event);

                m.setText("");
                Toast.makeText(Message.this, "message sent successfully", Toast.LENGTH_SHORT).show();
                // intent = new Intent(Message.this, MainActivity.class);
                // startActivity(intent);

                //}
                //  else
                // Toast.makeText(Message.this, "Failed to save changes. No Internet Connection!", Toast.LENGTH_SHORT).show();

            }
        });


    }
}