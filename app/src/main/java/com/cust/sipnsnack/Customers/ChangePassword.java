package com.cust.sipnsnack.Customers;

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

import com.cust.sipnsnack.LoginActivity;
import com.example.sipnsnack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {

    TextInputEditText oldPwd, newPwd, reEnterPwd;
    Button changeBtn;
    String old, newP, reEnter, username, pass, dbEmail, dbPass;
    SharedPreferences spr;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();
    LoadingDialog loadingDialog;
    ImageView infoIconIV, backBtn;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        loadingDialog = new LoadingDialog(ChangePassword.this);

        oldPwd = findViewById(R.id.changePwd_OldPwd);
        newPwd = findViewById(R.id.changePwd_NewPwd);
        reEnterPwd = findViewById(R.id.changePwd_ReNewPwd);
        infoIconIV = findViewById(R.id.infoiconIV);
        backBtn = findViewById(R.id.backBTN);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        username = spr.getString("Username", "");

        changeBtn = findViewById(R.id.changePwdBtn);

        readFromDB(username);

        infoIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passwordChangeDialog();

            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePWD();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), CustomerView.class);
                startActivity(it);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), CustomerView.class);
        startActivity(it);
        finish();
    }

    public void readFromDB(String userName) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
                .child(userName).addListenerForSingleValueEvent(new ValueEventListener() {

            DataSnapshot dataSnapshot;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                this.dataSnapshot = snapshot;

                pass = dataSnapshot.child("Password").getValue().toString();
                dbEmail = dataSnapshot.child("Email").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void changePwd(String username) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(dbEmail, pass);


        assert user != null;
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                                .child("Users").child("Customers").child(username);

                                        mDatabaseRef.child("Password").setValue(newP);

                                        loadingDialog.dismissDialog();
                                        operationSuccessful();

                                    } else {
                                        Toast.makeText(ChangePassword.this, "Some Error Occurred.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ChangePassword.this, "Action Failed ...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void changePWD() {
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


    public void operationSuccessful() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.operation_success_dialog, null);
        Button enterBTN = view.findViewById(R.id.enterBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        enterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                Toast.makeText(ChangePassword.this, "Password Updated Successfully\nLogin again to Continue ...", Toast.LENGTH_SHORT).show();
                logout();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    public void logout() {

        SharedPreferences.Editor editor = spr.edit();
        editor.putString("Status", "");
        editor.putString("User", "");
        editor.putString("LoggedIn", "");
        editor.putString("Username", "");
        editor.apply();

    }
}