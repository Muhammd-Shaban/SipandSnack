package com.cust.sipnsnack.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminProfile extends AppCompatActivity {

    TextView nameTV, usernameTV, phoneNoTV;
    Button editBtn, logoutBtn;
    String name, username, password;
    SharedPreferences spr;
    ImageView infoIcon;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        loadingDialog = new LoadingDialog(AdminProfile.this);
        loadingDialog.startLoadingDialog();

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);

        nameTV = findViewById(R.id.nameTv);
        usernameTV = findViewById(R.id.usernameTv);
        phoneNoTV = findViewById(R.id.phoneNoTv);
        editBtn = findViewById(R.id.editProfileBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        infoIcon = findViewById(R.id.infoiconIV);

        username = spr.getString("Username", "");

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managerProfileDialog();
            }
        });

        readFromDB(username);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), EditAdminProfile.class);
                it.putExtra("Name", name);
                it.putExtra("Username", username);
                startActivity(it);
                finish();
            }
        });
    }


    public void readFromDB(String userName) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Admin")
                .addListenerForSingleValueEvent(new ValueEventListener() {

            DataSnapshot dataSnapshot;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                this.dataSnapshot = snapshot;

                name = dataSnapshot.child("Name").getValue().toString();
                password = dataSnapshot.child("Password").getValue().toString();

                nameTV.setText(name);
                usernameTV.setText(userName);

                loadingDialog.dismissDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void managerProfileDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_admin_profile_dialog, null);
        Button okBTN = view.findViewById(R.id.okBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), AdminView.class);
        startActivity(it);
        finish();
    }
}