package com.cust.sipnsnack.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddManagers extends AppCompatActivity {

    TextInputEditText managerUsernameET, managerNameET, managerPasswordET, managerPhoneNoET;
    String managerUsername, managerName, managerPassword, managerPhoneNo;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference();
    Button addManagerBTN;
    ImageView infoIcon;
    Boolean dbCheck = false;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_managers);

        managerUsernameET = findViewById(R.id.manager_UsernameET);
        managerNameET = findViewById(R.id.manager_NameET);
        managerPasswordET = findViewById(R.id.manager_PasswordET);
        managerPhoneNoET = findViewById(R.id. manager_phoneNoET);
        backBtn = findViewById(R.id.backBTN);

        infoIcon = findViewById(R.id.infoiconIVManagers);

        addManagerBTN = findViewById(R.id.addManagersButton);

        addManagerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managerUsername = managerUsernameET.getText().toString().toLowerCase();
                managerName = managerNameET.getText().toString();
                managerPassword = managerPasswordET.getText().toString();
                managerPhoneNo = managerPhoneNoET.getText().toString();

                addManager(managerUsername, managerName, managerPassword, managerPhoneNo);
            }
        });

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), ManageManagers.class);
                startActivity(it);
                finish();
            }
        });
    }

    public void addManager(String Username, String Name, String Password, String PhoneNo) {

        if (Username.equals("")) {
            managerUsernameET.setError("User Name is Required");
        } else if (Name.equals("")) {
            managerNameET.setError("Manager Name is Required");
        } else if (Password.equals("")) {
            managerPasswordET.setError("Password is Required");
        } else if (PhoneNo.equals("")) {
            managerPhoneNoET.setError("Phone No is Required");
        } else if (Password.length() < 6) {
            managerPasswordET.setError("Minimum Length should be 6");
        } else {
            FirebaseDatabase.getInstance().getReference().child("Users").child("Managers")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String tempUserName = managerUsername;
                                tempUserName = tempUserName.toLowerCase();
                                String dbUserName = snapshot.child("Username").getValue().toString().toLowerCase();
                                if (dbUserName.equals(tempUserName)) {
                                    dbCheck = true;
                                    managerUsernameET.setError("Username Already Exists");
                                    managerUsernameET.requestFocus();

                                    break;
                                } else {

                                    dbCheck = false;

                                }
                            }

                            if (!dbCheck) {
                                mDatabaseRef = mDatabase.getReference().child("Users").child("Managers").child(Username);
                                mDatabaseRef.child("Username").setValue(Username);
                                mDatabaseRef.child("Name").setValue(Name);
                                mDatabaseRef.child("Password").setValue(Password);
                                mDatabaseRef.child("PhoneNo").setValue(PhoneNo);
                                mDatabaseRef.child("Role").setValue("Manager");

                                managerUsernameET.setText("");
                                managerNameET.setText("");
                                managerPasswordET.setText("");
                                managerPhoneNoET.setText("");

                                Toast.makeText(AddManagers.this, "Manager Added Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    public void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_add_manager, null);
        Button okBTN = view.findViewById(R.id.okBTN);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), ManageManagers.class);
        startActivity(it);
        finish();
    }
}