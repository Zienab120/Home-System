package com.example.pervasive_projcect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;

import io.grpc.inprocess.AnonymousInProcessSocketAddress;


public class Login extends AppCompatActivity implements NetworkChangeReceiver.NetworkChangeListener {
    public boolean remebermeChecked = false;

    Boolean connected= true;
    AlertDialog dialog;
//    DatabaseReference CurrentUser;
    FirebaseDatabase database;
    NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver(this);
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finalproject-eef58-default-rtdb.firebaseio.com/");


    TextInputEditText editTextEmail, editTextPassword;
    Button txtForgetPassword;
    TextView loginButton, GoToregister;
    CheckBox remember;
    //    ProgressBar progressBar;
    FirebaseAuth mAuth;
    GoogleSignInClient signInClient;
    Button signInButton;
    int RC_SIGN_IN = 10;
    DBHelper DB;

    public static final String SHARED_PREFS = "sharedPrefs";
    Registration registration = new Registration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        preference = getSharedPreferences(PREF_NAME,MODE_PRIVATE);

        checkBox();

        editTextEmail = findViewById(R.id.mail);
        editTextPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_btn);
        txtForgetPassword = findViewById(R.id.forget);
        GoToregister = findViewById(R.id.go_to_register);
        remember = findViewById(R.id.remember_me);

        signInButton = findViewById(R.id.signIN_btn);

        mAuth = FirebaseAuth.getInstance();



        GoToregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
            }
        });
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    remebermeChecked = true;
                } else {
                    remebermeChecked = false;
                }
            }
        });


        txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgetPassword.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void LoginWithFirebase() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DB = new DBHelper(Login.this);

                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)){
                    editTextEmail.setError("Email field can't be empty");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    editTextPassword.setError("Password field can't be empty");
                }

                else {
//                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(email.replaceAll("@gmail.com",""))) {
                                final String getPassword = snapshot.child(email.replaceAll("@gmail.com","")).child("password").getValue(String.class);
                                if (getPassword.equals(password)) {
                                    Toast.makeText(Login.this, "Successfully Login.", Toast.LENGTH_SHORT).show();
//                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.putString("name", "true");
//                                    editor.apply();

//                                    String google = "@gmail.com";
//                                    String emailWithoutgmail_com = email.replaceAll(google, "");



                                    databaseReference.child("Users").child(email.replaceAll("@gmail.com","")).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if(task.isSuccessful()) {
                                                if (task.getResult().exists()) {
                                                    DataSnapshot dataSnapshot = task.getResult();
                                                    String u_name = String.valueOf(dataSnapshot.child("username").getValue());
                                                    onLoginSuccess(email,u_name);
//                                                    DatabaseReference CurrentUser = database.getReference("CurrentUser");
                                                    databaseReference.child("CurrentUser").setValue(email.replaceAll("@gmail.com","")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(Login.this, "Current User", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
//                                                    DatabaseReference myRef = database.getReference("CurrentValues").child(emailforfirebase).child("Light");
                                                    databaseReference.child("CurrentValues").child(email.replaceAll("@gmail.com","")).child("Light").setValue("");
                                                    databaseReference.child("CurrentValues").child(email.replaceAll("@gmail.com","")).child("HomePassword").setValue("");
                                                    databaseReference.child("CurrentValues").child(email.replaceAll("@gmail.com","")).child("Temperature").setValue("");
                                                    databaseReference.child("CurrentValues").child(email.replaceAll("@gmail.com","")).child("attackAlert").setValue("");
                                                    databaseReference.child("CurrentValues").child(email.replaceAll("@gmail.com","")).child("welcomeMessage").setValue("");

                                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                                    intent.putExtra("UniqueUser", u_name);
                                                    intent.putExtra("EmailExtraF",email);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            }
                                            else Toast.makeText(Login.this,"Error",Toast.LENGTH_SHORT).show();

                                        }
                                    });



                                }
                                else if (!getPassword.equals(password)){
                                    Toast.makeText(Login.this, "Password is wrong.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(Login.this, "There is no Email.", Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//                    LoginWithSQL();
                }

            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        signInClient = GoogleSignIn.getClient(Login.this, googleSignInOptions);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });

    }

    private void LoginWithSQL() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB = new DBHelper(Login.this);
                String userNameSQL="NULL";
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                if (email.equals("") || password.equals("")) {
                    Toast.makeText(Login.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean checkuserpass = DB.checkusernamepassword(email, password);
                    if (checkuserpass == true) {
                        Toast.makeText(Login.this, "Sign in successfull", Toast.LENGTH_SHORT).show();

                        userNameSQL = DB.fetchUserName(email);

                        onLoginSuccess(email,userNameSQL);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("UserNameExtra",userNameSQL);
                        intent.putExtra("EmailExtra",email);

                        startActivity(intent);

                    } else {
                        Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }

    private void SignIn() {
        Intent signIntent = signInClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut();
        signInClient.signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (Exception e) {
//                Toast.makeText(this, "HEHEHE", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getUserName() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            return firebaseUser.getDisplayName();
        }
        return "NULL";
    }

    private String getProfilePicture() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null && firebaseUser.getPhotoUrl() != null) {
            return firebaseUser.getPhotoUrl().toString();
        }
        return "NULL";
    }

    private String getEmail() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            return firebaseUser.getEmail().toString();
        }
        return "NULL";
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//
                    String google = "@gmail.com";
                    String emailWithoutgmail_com = getEmail().replaceAll(google, "");
                    User user = new User(getUserName().toLowerCase(), getUserName(), getEmail(), "password", "20.3.2003", getProfilePicture());

//                    registration.addUserToFirebase(getUserName().toLowerCase(), getUserName(), getEmail(), "password", "20.3.2003",getEmail().replaceAll("@gmail.com",""), getProfilePicture());
                    databaseReference.child(emailWithoutgmail_com).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(Login.this, "Added", Toast.LENGTH_SHORT).show();
                            onLoginSuccess(getEmail(),getUserName());
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra("userNameExtra",getUserName());
                            intent.putExtra("email",getEmail());
                            startActivity(intent);
                        }
                    });


                } else {
                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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
////        Cursor res = db.get();
//        if (res.getCount() == 0) {
//            Toast.makeText(Login.this, "No Entry Exits", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (isConnected) {

            LoginWithFirebase();

            connected = true;
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
                Toast.makeText(Login.this, "Check your Connectivity!", Toast.LENGTH_SHORT).show();
            }
////                            addUserToFirebase(res.getString(1),res.getString(2),res.getString(0),res.getString(3),res.getString(4),res.getString(0).replaceAll("@gmail.com",""));
////                                addUserToFirebase(res.getString(1),res.getString(2),res.getString(0),res.getString(3),res.getString(4),res.getString(0).replaceAll("@gmail.com",""));



        } else {

            if (dialog == null || !dialog.isShowing()) {
                connected = false;
                Toast.makeText(Login.this, "Check your Connectivity!", Toast.LENGTH_SHORT).show();

//                ShowDialog();
            }
            LoginWithSQL();
        }


    }


    //--------------------------------- Dialog Show --------------------------------
    private void ShowDialog() {

        dialog = new AlertDialog.Builder(Login.this)
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
    }

    private void checkBox() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String check = sharedPreferences.getString("name", "");
        String PREF_LOGOUT = getIntent().getStringExtra("PREF");
        if (check.equals("true")) {
            Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);
            finish();
        }


    }
    // In LoginActivity.java
    public void onLoginSuccess(String email,String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_email", email);
        editor.putString("user_name", username);
        editor.apply();

    }



}