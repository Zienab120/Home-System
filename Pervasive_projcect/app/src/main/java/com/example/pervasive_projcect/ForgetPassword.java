package com.example.pervasive_projcect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;



import android.content.Context.BindServiceFlags;

public class ForgetPassword extends AppCompatActivity {

    Button btnReset, btnBack;
    EditText edtEmail;
    ProgressBar progressBar;
    String strEmail="";
    String strEmail2="";

    String finalEmail="";

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrEmail() {
        return strEmail;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        btnBack = findViewById(R.id.btnForgotPasswordBack);
        btnReset = findViewById(R.id.btnReset);
        edtEmail = findViewById(R.id.edtForgotPasswordEmail);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = edtEmail.getText().toString().trim();
                strEmail2 = edtEmail.getText().toString();
                if (!TextUtils.isEmpty(strEmail)) {

                    Intent intent = new Intent(ForgetPassword.this, Confirmation.class);
                    intent.putExtra("ForgetEmail", strEmail);
                    startActivity(intent);
                    finish();
//                    Confirmation confirmation = new Confirmation();
//                    confirmation.setEmail(strEmail2);
                } else {
                    edtEmail.setError("Email field can't be empty");
//                    Confirmation confirmation = new Confirmation(strEmail);
                }

//                finalEmail = strEmail2;
//                EmailReturn();
            }
        });


        //Back Button Code
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public String EmailReturn()
    {return strEmail;}

}