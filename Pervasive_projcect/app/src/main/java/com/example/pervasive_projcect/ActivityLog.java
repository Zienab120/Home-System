package com.example.pervasive_projcect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ActivityLog extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Event> logList=new ArrayList<>();
    String email;
    logAdapter logAdapter;
    DBHelper dbHelper;
    Event event=new Event();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        boolean connection=intent.getBooleanExtra("internet",false);
        email=getIntent().getStringExtra("email");
        String google = "@gmail.com";
        String emailforfirebase = email.replaceAll(google, "");
        if(connection) {


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myEventRef = database.getReference("Users'Events").child(emailforfirebase);
            myEventRef.keepSynced(true);

            myEventRef.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data : dataSnapshot.getChildren())
                    {
                        event=new Event();
                        event.ID=data.child("id").getValue(String.class);
                        event.activityName=data.child("activityName").getValue(String.class);
                        event.event=data.child("event").getValue(String.class);
                        event.timeStamp=data.child("timeStamp").getValue(String.class);
                        dbHelper=new DBHelper((getApplicationContext()));
                        dbHelper.insertLog(event.ID,email,event.activityName,event.event,event.timeStamp);
                        displayItems(event);
                        //logList.add(event);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }
        else{
            dbHelper=new DBHelper((getApplicationContext()));
            Cursor cursor=dbHelper.fetchAllLogs(email);
            if (cursor.moveToFirst())
            {
                do {
                    String a = cursor.getString(0);
                    String b = cursor.getString(1);
                    String c = cursor.getString(2);
                    String d = cursor.getString(3);

                    Event e = new Event(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                    displayItems(e);
                    //logList.add(e);
                }while (cursor.moveToNext());
            }

        }


    }



    private void displayItems(Event event) {
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        if(logList.size()==0) {
            logList.add(event);
            logAdapter = new logAdapter(this, logList);
            recyclerView.setAdapter(logAdapter);
        }
        else{
            logList.add(event);
            logAdapter.notifyItemInserted(logList.size()-1);
        }
    }
}