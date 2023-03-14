package com.cust.sipnsnack.ManagerDashboard;

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
import android.widget.Toast;

import com.cust.sipnsnack.LoginActivity;
import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerProfile extends AppCompatActivity {

    TextView nameTV, usernameTV, phoneNoTV;
    Button editBtn, logoutBtn;
    String name, username, phoneNo, password;
    SharedPreferences spr;
    ImageView infoIcon, backBtn;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_profile);

        loadingDialog = new LoadingDialog(ManagerProfile.this);
        loadingDialog.startLoadingDialog();

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);

        nameTV = findViewById(R.id.nameTv);
        usernameTV = findViewById(R.id.usernameTv);
        phoneNoTV = findViewById(R.id.phoneNoTv);
        editBtn = findViewById(R.id.editProfileBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        infoIcon = findViewById(R.id.infoiconIV);
        backBtn = findViewById(R.id.backBTN);


        username = spr.getString("Username", "");

        readFromDB(username);

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managerProfileDialog();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), DashBoard.class);
                startActivity(it);
                finish();
            }
        });


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), ProfileEdit.class);
                it.putExtra("Name", name);
                it.putExtra("Username", username);
                it.putExtra("PhoneNo", phoneNo);
                startActivity(it);
                finish();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog();
            }
        });
    }


    public void readFromDB(String userName) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Managers")
                .child(userName).addListenerForSingleValueEvent(new ValueEventListener() {

            DataSnapshot dataSnapshot;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                this.dataSnapshot = snapshot;

                name = dataSnapshot.child("Name").getValue().toString();
                password = dataSnapshot.child("Password").getValue().toString();
                phoneNo = dataSnapshot.child("PhoneNo").getValue().toString();

                nameTV.setText(name);
                usernameTV.setText(userName);
                phoneNoTV.setText(phoneNo);

                loadingDialog.dismissDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void managerProfileDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_manager_profile_dialog, null);
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
        Intent it = new Intent(getApplicationContext(), DashBoard.class);
        startActivity(it);
        finish();
    }

    public void logoutDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_logout_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = spr.edit();
                editor.putString("User", "");
                editor.putString("Status", "");
                editor.putString("Username", "");
                editor.apply();


                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "Logout Successful ...", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        noBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}