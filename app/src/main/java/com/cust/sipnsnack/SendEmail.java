package com.cust.sipnsnack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SendEmail extends AppCompatActivity {

    TextView emailTV;
    Button verify;
    FirebaseAuth mAuth;

    ImageView back, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        mAuth = FirebaseAuth.getInstance();

        emailTV = findViewById(R.id.emailTV);
        verify = findViewById(R.id.verifyEmail);
        back = findViewById(R.id.backBTN);
        info = findViewById(R.id.infoiconIV);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog();
            }
        });

        emailTV.setText("Email: " + getIntent().getStringExtra("Email"));

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // SEND EMAIL CONFIRMATION LINK ...
                FirebaseUser myUser = mAuth.getCurrentUser();
                myUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        emailSentDialog();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SendEmail.this, "Error : "+ e.toString(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(SendEmail.this, "Email was not sent ...\nTry Again in a Moment.", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    public void emailSentDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.email_sent_dialog, null);
        Button enterBTN = view.findViewById(R.id.enterBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        enterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                Toast.makeText(SendEmail.this, "Confirmation Email Sent ...", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }


    public void infoDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_email_dialog, null);
        Button enterBTN = view.findViewById(R.id.okBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        enterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}