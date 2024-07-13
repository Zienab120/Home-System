package com.example.pervasive_projcect;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Confirmation extends AppCompatActivity {
    Button Confirm;
    EditText SetPassword, ConfirmPassword;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smart-home-csys2024-default-rtdb.firebaseio.com/");

//    EditText editionEmail;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Confirm = findViewById(R.id.ConfirmButton);
        SetPassword = findViewById(R.id.set_text);
        ConfirmPassword = findViewById(R.id.confirm_text);
//        editionEmail = findViewById(R.id.editionText);


        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String confirmPass, SetPass;
                confirmPass = ConfirmPassword.getText().toString().trim();
                SetPass = SetPassword.getText().toString().trim();

                if (confirmPass.isEmpty())
                    ConfirmPassword.setError("Password field is empty");

                if (SetPass.isEmpty())
                    SetPassword.setError("Password field is empty");

                else if (!(confirmPass.equals(SetPass)))
                {
                    ConfirmPassword.setError("Passwords are not identical");
                    SetPassword.setError("Passwords are not identical");
//                    Toast.makeText(Confirmation.this, "Please make sure of password.", Toast.LENGTH_SHORT).show();
                }
                else if (confirmPass.length() < 5 || SetPass.length() < 5)
                    Toast.makeText(Confirmation.this, "Password is too short.", Toast.LENGTH_SHORT).show();

                else {
                    HashMap User = new HashMap();
                    User.put("password", confirmPass.toString());
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//                    ForgetPassword forgetPassword = new ForgetPassword();
//                    email = forgetPassword.finalEmail;
//                    String email = editionEmail.getText().toString().trim();
                    String email = getIntent().getStringExtra("ForgetEmail");


                    databaseReference.child(email.replaceAll("@gmail.com","")).updateChildren(User).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Confirmation.this, "Successfully Change Password.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Confirmation.this, Login.class);
                                startActivity(intent);
                                finish();

                            } else {

                                Toast.makeText(Confirmation.this, "Failed to change password.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }
            }


        });
    }

}