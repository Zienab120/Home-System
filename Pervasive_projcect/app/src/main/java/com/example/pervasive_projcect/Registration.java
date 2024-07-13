package com.example.pervasive_projcect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Registration extends AppCompatActivity implements NetworkChangeReceiver.NetworkChangeListener {
    AlertDialog dialog;
    Bitmap imageToStorSql;
    NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver(this);
    int counting=0;
    String U=" ";
    DBHelper db;
    String key;

    Boolean connected= true;
    public int chickprofile=0;
    ImageView picuterProfile;

    DatabaseReference myRef;
    FirebaseDatabase database;
    TextInputEditText editTextName,editTextUserName,editTextEmail,editTextPassword,editTextConfirmPassword,editTextBirthdate;
    TextView TextViewHaveAccount;
    Button buttonRegister;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        db=new DBHelper(this);
        picuterProfile=findViewById(R.id.profilePicture);
        TextViewHaveAccount=findViewById(R.id.haveAccount);
        editTextName=findViewById(R.id.Name);
        editTextUserName=findViewById(R.id.UserName);
        editTextEmail=findViewById(R.id.Email);
        editTextPassword=findViewById(R.id.Password);
        editTextConfirmPassword=findViewById(R.id.ConfirmPassword);
        editTextBirthdate=findViewById(R.id.Birthdate);
        buttonRegister=findViewById(R.id.btn_register);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("Users");

        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        picuterProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chickprofile=1;
                ImagePicker.with(Registration.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        editTextBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BirthdateCalender();
            }
        });

        TextViewHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Registration.this,Login.class);
                startActivity(intent);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailWithoutgmail_com=" ";
                String name,userName,email,password,confirmPassword,birthdate;
                name=String.valueOf(editTextName.getText());
                userName=String.valueOf(editTextUserName.getText());
                email=String.valueOf(editTextEmail.getText());
                password=String.valueOf(editTextPassword.getText());
                confirmPassword=String.valueOf(editTextConfirmPassword.getText());
                birthdate=String.valueOf(editTextBirthdate.getText());




                if(TextUtils.isEmpty(name)){
                    editTextName.setError("Name field can't be empty");

                    //Toast.makeText(Registration.this,"Enter Name",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(userName)){
                    editTextUserName.setError("UserName field can't be empty");

                    //Toast.makeText(Registration.this,"Enter UserName",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    editTextEmail.setError("Email field can't be empty");
                    //Toast.makeText(Registration.this,"Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }

                //int existing = 0;
                String google="@gmail.com";
                emailWithoutgmail_com=email.replaceAll(google,"");
                myRef.child(email.replaceAll(google,"")).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            if(task.getResult().exists()){
                                editTextEmail.setError("This email already has an account!");
                                // editTextEmail.getText().clear();
                            }
                        }

                    }

                });


                if(TextUtils.isEmpty(password)){
                    editTextPassword.setError("Password field can't be empty");
                    //Toast.makeText(Registration.this,"Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()<5){
                    editTextPassword.setError("Enter Strong Password");
                    return;
                }

                if(TextUtils.isEmpty(confirmPassword)){
                    editTextConfirmPassword.setError("Confirm Password field can't be empty");
                    //Toast.makeText(Registration.this,"Enter ConfirmPassword",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(password.equals(confirmPassword))){
                    editTextConfirmPassword.setError("Password and ConfirmPassword should be identical!");
                    //Toast.makeText(Registration.this,"Password and ConfirmPassword should be identical!",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(birthdate)){
                    editTextBirthdate.setError("Birthdate field can't be empty");
                    //Toast.makeText(Registration.this,"Enter Birthdate",Toast.LENGTH_SHORT).show();
                    return;
                }


                //addToFirebase OR sqlite:

                if(connected) {




                    addUserToFirebase(name, userName, email, password, birthdate, emailWithoutgmail_com,"");
                    if(chickprofile==1){
                        db.insert(email, name, userName, password, birthdate, U,imageToStorSql,uri.toString());
                    }else{
                        db.insert(email, name, userName, password, birthdate, " ",null,null);

                    }


                }
                else {
                    if(chickprofile==1) {
                        db.insert(email, name, userName, password, birthdate, U, imageToStorSql,uri.toString());
                    }else{
                        db.insert(email, name, userName, password, birthdate, " ", null,null);

                    }
                    Toast.makeText(Registration.this,"Check network connectivity!",Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);

                editTextName.getText().clear();
                editTextUserName.getText().clear();
                editTextEmail.getText().clear();
                editTextPassword.getText().clear();
                editTextConfirmPassword.getText().clear();
                editTextBirthdate.getText().clear();
            }

        });


        //delay:



//content();

    }
    public void content(){
        counting++;
        Toast.makeText(Registration.this,"Delaaaaaaaay!",Toast.LENGTH_SHORT).show();
        refresh(3000);

    }

    private void refresh(int delay) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                content();

            }
        };
        Handler handler=new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable,delay);
    }


    private void BirthdateCalender(){
        DatePickerDialog calender=new DatePickerDialog(this,R.style.Calender, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                editTextBirthdate.setText(String.valueOf(year)+"."+String.valueOf(month+1)+"."+String.valueOf(dayOfMonth));
            }
        }, 2024, 3, 12);
        calender.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri =data.getData();
        try {
            imageToStorSql= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        picuterProfile.setImageURI(uri);
        //picuterProfile.setImageBitmap(imageToStorSql);

    }
    private void UploadProfilePicture(String email) {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        FirebaseStorage.getInstance().getReference("images/"+ UUID.randomUUID().toString()).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                updateProfilePicture(email,task.getResult().toString());
                            }
                        }
                    });
                    Toast.makeText(Registration.this,"Image Uploaded !",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Registration.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress= 100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded "+(int)progress+"%");
            }
        });


    }

    private void updateProfilePicture(String email,String url) {
        U=url;

        FirebaseDatabase.getInstance().getReference("Users/"+key+"/profilePicture").setValue(url);
        Toast.makeText(Registration.this,url,Toast.LENGTH_SHORT).show();
        db.updateUrlInSql(email,url);
        //FirebaseDatabase.getInstance().getReference("Users/"+"U"+String.valueOf(user_count)+"/profilPicture").setValue(url);
    }
    public void addUserToFirebase( String name, String userName, String email, String password, String birthdate,String emailWithoutgmail_com ,String profilePecture) {


        //key=myRef.push().getKey();
        key = emailWithoutgmail_com;
        User user = new User(name, userName, email, password, birthdate, profilePecture);
        myRef.child(key).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Registration.this, "Added", Toast.LENGTH_SHORT).show();
                if (chickprofile == 1) {
                    UploadProfilePicture(email);
                }


            }
        });

    }
    //------------------------- Check internet ------------------
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


        if (isConnected) {

            connected = true;
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }

            ///////used:
            Cursor res=db.fetchAllUsers();
            if(res.getCount()==0){
                Toast.makeText(Registration.this, "No Entry Exits", Toast.LENGTH_SHORT).show();
                return;
            }

//            Registration reg=new Registration();
            if(res !=null && res.moveToFirst()){
                do {

                    if(res.getString(5)==""&&res.getString(7)!=null){
                        uri= Uri.parse(res.getString(7));
                        chickprofile=1;
                    }else {
                        chickprofile=0;
                    }

                    addUserToFirebase(String.valueOf(res.getString(1)),String.valueOf(res.getString(2)),String.valueOf(res.getString(0)),String.valueOf(res.getString(3)),res.getString(4),String.valueOf(res.getString(0)).replaceAll("@gmail.com",""),res.getString(5));
                    //db.updateUrlInSql(res.getString(0),U);
                }while (res.moveToNext());
            }


            //---------------------//
//            Cursor res=db.get();
//            if(res.getCount()==0){
//                Toast.makeText(Registration.this, "No Entry Exits", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if(res !=null && res.moveToFirst()){
//                do {
//
//                    chickprofile=0;
//                    addUserToFirebase(String.valueOf(res.getString(1)),String.valueOf(res.getString(2)),String.valueOf(res.getString(0)),String.valueOf(res.getString(3)),res.getString(4),String.valueOf(res.getString(0)).replaceAll("@gmail.com",""));
//
//                }while (res.moveToNext());
//            }

//            while (res.moveToNext()){
//                myRef.child(res.getString(0).replaceAll("@gmail.com","")).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                        if (task.isSuccessful()){
//                            if(task.getResult().exists()){
//
//                            }else{
//                                chickprofile=0;
//                                addUserToFirebase(res.getString(1),res.getString(2),res.getString(0).replaceAll("@gmail.com",""),res.getString(3),res.getString(4),res.getString(5));
//
//                            }
//                        }
//
//                    }
//
//                });
//
//            }



        } else {

            if (dialog == null || !dialog.isShowing()) {
                connected =false;
                Toast.makeText(Registration.this, "Check your Connectivity!", Toast.LENGTH_SHORT).show();

//                ShowDialog();
            }

        }
    }


    //--------------------------------- Dialog Show --------------------------------
    private void ShowDialog() {

        dialog = new AlertDialog.Builder(Registration.this)
                .setView(R.layout.no_internet_dialog)
                .setCancelable(false)
                .create();
        dialog.show();



        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

            }
        };
        Handler handler=new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable,5000);



//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable((android.R.color.transparent)));


    }




}