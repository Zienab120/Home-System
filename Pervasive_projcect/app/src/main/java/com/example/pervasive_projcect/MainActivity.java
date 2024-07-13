package com.example.pervasive_projcect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener,NetworkChangeReceiver.NetworkChangeListener{
    RecyclerView recyclerView;
    List<items> list;
    MyAdapter myAdapter;
    SearchView searchView;

    String email,username;
    boolean networkConnection=false;
    NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        String userNameSent1 = getIntent().getStringExtra("UserNameExtra");
        String email1=getIntent().getStringExtra("EmailExtra");

        String userNameSent2 = getIntent().getStringExtra("usernameExtra");
        String email2=getIntent().getStringExtra("email");

        String userNameSent3 = getIntent().getStringExtra("UniqueUser");
        String email3=getIntent().getStringExtra("EmailExtraF");

        if(userNameSent1!= null && email1!= null ){
            email=email1;
            username=userNameSent1;
        }
        else if(userNameSent2!= null && email2!= null){
                email=email2;
                username=userNameSent2;
        }
        else if(userNameSent3!= null && email3!= null){
                email=email3;
                username=userNameSent3;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("user_email", null);
        username=sharedPreferences.getString("user_name", null);


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String [] {Manifest.permission.POST_NOTIFICATIONS},101
                );

            }
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        searchView=findViewById(R.id.search_view);
        displayItems();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

    }

    private void filter(String newText) {
        List<items> filteredList=new ArrayList<>();
        for(items item:list){
            if(item.getTitle().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        myAdapter.filterList(filteredList);
    }

    private void displayItems() {
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        list=new ArrayList<>();
        list.add(new items("Temperature",R.drawable.tempreture));
        list.add(new items("Password",R.drawable.password));
        list.add(new items("Light",R.drawable.light));
        list.add(new items("Entry Attack",R.drawable.attack));
        list.add(new items("Message",R.drawable.message));

        myAdapter=new MyAdapter(this,list,this);
        recyclerView.setAdapter(myAdapter);

    }


    @Override
    public void onItemClicked(items item) {
        // Toast.makeText(this,item.getTitle(),Toast.LENGTH_SHORT).show();

        if(item.getTitle().equals("Light")){

            if(!networkConnection)
                Toast.makeText(MainActivity.this,"No Internet Connection!",Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(MainActivity.this, Light.class);
                 intent.putExtra("email",email);
                startActivity(intent);
            }
        }
        else if(item.getTitle().equals("Entry Attack")){
//            Password pass=new Password();
//            if(!(pass.DoorPassword.equals("654321"))){
//                makeNotification();
//            }else{
//                Intent intent=new Intent(MainActivity.this, EntryAttack.class);
//                startActivity(intent);
//            }
            // makeNotification();
            if(!networkConnection)
                Toast.makeText(MainActivity.this,"No Internet Connection!",Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(MainActivity.this, EntryAttack.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }

        }
        else if(item.getTitle().equals("Message")){
            if(!networkConnection)
                Toast.makeText(MainActivity.this,"No Internet Connection!",Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(MainActivity.this, Message.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        }
        else if(item.getTitle().equals("Password")){
            if(!networkConnection)
                Toast.makeText(MainActivity.this,"No Internet Connection!",Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(MainActivity.this, Password.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        }
        else if(item.getTitle().equals("Temperature")){
            if(!networkConnection)
                Toast.makeText(MainActivity.this,"No Internet Connection!",Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(MainActivity.this, Temperature.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeReceiver);
    }


    @Override
    public void onNetworkChanged(boolean isConnected) {
        if (isConnected)
            networkConnection=true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.activity_log){
            Intent intent=new Intent(this,ActivityLog.class);
            intent.putExtra("internet", networkConnection);
            intent.putExtra("email", email);
            startActivity(intent);
        }else if(item.getItemId()==R.id.profile){
            Intent intent=new Intent(this,ProfileActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("email",email);
            startActivity(intent);
        }else if(item.getItemId()==R.id.logout){
            Intent intent=new Intent(this,Login.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void makeNotification(){
        String channelID="CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),channelID);
        builder.setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Attack!")
                .setContentText("We are at Risk!")
                .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH);
        Intent intent=new Intent(getApplicationContext(),EntryAttack.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Attack","We are at Risk!");

        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT>= android.os.Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=notificationManager.getNotificationChannel(channelID);
            if(notificationChannel==null){
                int importance=NotificationManager.IMPORTANCE_HIGH;
                notificationChannel=new NotificationChannel(channelID,"some description",importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0,builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If you need to re-fetch the email when returning to MainActivity
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("user_email", null);
        username=sharedPreferences.getString("user_name", null);
    }
}

