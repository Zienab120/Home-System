package com.example.pervasive_projcect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pervasive_projcect.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity {

    Button logout,backtomain;
    TextView Username,Email;
    ImageView imageView;

    DatabaseReference CurrentUser;
    FirebaseDatabase database;

    ActivityProfileBinding binding;
    public static final String SHARED_PREFS = "sharedPrefs";


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        binding.imageView.

        setContentView(R.layout.activity_profile);
        logout=findViewById(R.id.logout_profile);
        backtomain=findViewById(R.id.go_to_main);
        Username=findViewById(R.id.username);
        Email=findViewById(R.id.email);
        imageView=findViewById(R.id.profileimage);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String username=getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");



            DBHelper db=new DBHelper(this);
            Boolean ur=db.getURIFromSql(email);
            if(ur==false){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.profile));

            }else {
                Bitmap bt=db.getImageFromSql(email);
                imageView.setImageBitmap(bt);
            }

         //   imageView.setImageDrawable(getResources().getDrawable(R.drawable.flek));






//        db.checkImg()




//        DocumentReference documentReference = fStore.collection("Users").document(userID);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapShot, @Nullable FirebaseFirestoreException error) {
//                username.setText(documentSnapShot.getString("username"));
//                emailTV.setText(documentSnapShot.getString("emailTV"));
////                imageView.setText(documentSnapShot.getString("UserName"));
//            }
//        });


            Username.setText(username);
            Email.setText(email);



       // imageView.setImageDrawable(getResources().getDrawable(R.drawable.cat));





        backtomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name","false");
                editor.apply();


                Intent intent=new Intent(ProfileActivity.this,Login.class);

                intent.putExtra("PREF","false");
                startActivity(intent);

            }
        });
    }
}