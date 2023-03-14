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
import android.widget.Toast;

import com.cust.sipnsnack.Customers.CustomerView;
import com.example.sipnsnack.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerPasswordChange extends AppCompatActivity {

    TextInputEditText oldPwd, newPwd, reEnterPwd;
    Button changeBtn;
    String old, newP, reEnter, username, pass;
    SharedPreferences spr;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();
    LoadingDialog loadingDialog;
    ImageView infoIconIV, backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_password_change);

        loadingDialog = new LoadingDialog(ManagerPasswordChange.this);

        oldPwd = findViewById(R.id.changePwd_OldPwd);
        newPwd = findViewById(R.id.changePwd_NewPwd);
        reEnterPwd = findViewById(R.id.changePwd_ReNewPwd);
        infoIconIV = findViewById(R.id.infoiconIV);
        backIV = findViewById(R.id.backBTN);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        username = spr.getString("Username", "");

        changeBtn = findViewById(R.id.changePwdBtn);

        readFromDB(username);

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                old = oldPwd.getText().toString();
                newP = newPwd.getText().toString();
                reEnter = reEnterPwd.getText().toString();

                if (old.equals("")) {
                    loadingDialog.dismissDialog();
                    oldPwd.setError("Old Password is not Provided.");
                } else if (newP.equals("")){
                    loadingDialog.dismissDialog();
                    newPwd.setError("New Password must not Empty.");
                } else if (reEnter.equals("")) {
                    loadingDialog.dismissDialog();
                    reEnterPwd.setError("Re-enter Password must not Empty.");
                } else {
                    if (!old.equals(pass)) {
                        loadingDialog.dismissDialog();
                        oldPwd.setError("Old Password is not Matching !");
                    } else {
                        if (newP.length() < 6) {
                            loadingDialog.dismissDialog();
                            newPwd.setError("Password min length should be 6 !");
                        } else if (!newP.equals(reEnter)) {
                            loadingDialog.dismissDialog();
                            reEnterPwd.setError("New Passwords are not Matching...");
                        } else {
                            changePwd(username);
                        }
                    }
                }
            }
        });

        infoIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passwordChangeDialog();

            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), DashBoard.class);
        startActivity(it);
        finish();
    }


    public void readFromDB(String userName) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Managers")
                .child(userName).addListenerForSingleValueEvent(new ValueEventListener() {

            DataSnapshot dataSnapshot;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                this.dataSnapshot = snapshot;

                pass = dataSnapshot.child("Password").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void changePwd(String username) {
        mDatabase.getInstance().getReference().child("Users").child("Managers")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        this.dataSnapshot = snapshot;
                        mRef = mDatabase.getReference().child("Users").child("Managers").child(username);
                        mRef.child("Password").setValue(newP);

                        loadingDialog.dismissDialog();
                        Toast.makeText(ManagerPasswordChange.this, "Password Updated Successfully ... ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), CustomerView.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void passwordChangeDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_password_change, null);
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
}