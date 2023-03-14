package com.cust.sipnsnack.Bikers;

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

public class AddBikers extends AppCompatActivity {

    TextInputEditText bikerUsernameET, bikerNameET, bikerPasswordET, bikerPhoneNoET, bikerAddressET;
    String bikerUsername, bikerName, bikerPassword, bikerPhoneNo, bikerAddress;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference();
    private DatabaseReference mDatabaseRef2 = mDatabase.getReference();
    Button addBikerBTN;
    ImageView infoIcon;
    Boolean dbCheck = false;
    LoadingDialog loadingDialog;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bikers);

        bikerUsernameET = findViewById(R.id.biker_UsernameET);
        bikerNameET = findViewById(R.id.biker_NameET);
        bikerPasswordET = findViewById(R.id.biker_PasswordET);
        bikerPhoneNoET = findViewById(R.id.biker_phoneNoET);
        bikerAddressET = findViewById(R.id.biker_AddressET);
        backBtn = findViewById(R.id.backBTN);
        loadingDialog = new LoadingDialog(AddBikers.this);

        infoIcon = findViewById(R.id.infoiconIVBikers);

        addBikerBTN = findViewById(R.id.addBikersButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ManageBikers.class));
                finish();
            }
        });

        addBikerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bikerUsername = bikerUsernameET.getText().toString().toLowerCase();
                bikerName = bikerNameET.getText().toString();
                bikerPassword = bikerPasswordET.getText().toString();
                bikerPhoneNo = bikerPhoneNoET.getText().toString();
                bikerAddress = bikerAddressET.getText().toString();

                addBiker(bikerUsername, bikerName, bikerPassword, bikerPhoneNo, bikerAddress);
            }
        });


        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    public void addBiker(String Username, String Name, String Password, String PhoneNo, String Address) {
        loadingDialog.startLoadingDialog();

        if (Username.equals("")) {

            bikerUsernameET.setError("User Name is Required");
            loadingDialog.dismissDialog();
            bikerUsernameET.requestFocus();

        } else if (Name.equals("")) {

            bikerNameET.setError("Biker Name is Required");
            loadingDialog.dismissDialog();
            bikerNameET.requestFocus();

        } else if (Password.equals("")) {

            bikerPasswordET.setError("Password is Required");
            loadingDialog.dismissDialog();
            bikerPasswordET.requestFocus();

        } else if (PhoneNo.equals("")) {

            bikerPhoneNoET.setError("Phone No is Required");
            loadingDialog.dismissDialog();
            bikerPhoneNoET.requestFocus();

        } else if (!PhoneNo.startsWith("03")) {

            bikerPhoneNoET.setError("Phone No must Start with 03 !");
            loadingDialog.dismissDialog();
            bikerPhoneNoET.requestFocus();

        } else if (bikerPhoneNo.length() < 11) {

            bikerPhoneNoET.setError("Phone No length must be 11 !");
            loadingDialog.dismissDialog();
            bikerPhoneNoET.requestFocus();

        } else if (Address.equals("")) {

            bikerAddressET.setError("Biker Address is Required");
            loadingDialog.dismissDialog();
            bikerAddressET.requestFocus();

        } else if (Password.length() < 6) {

            bikerPasswordET.setError("Minimum Length should be 6");
            loadingDialog.dismissDialog();
            bikerPasswordET.requestFocus();

        } else {

            FirebaseDatabase.getInstance().getReference().child("Users").child("Bikers")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String tempUserName = bikerUsername;
                                tempUserName = tempUserName.toLowerCase();
                                String dbUserName = snapshot.child("Username").getValue().toString().toLowerCase();
                                if (dbUserName.equals(tempUserName)) {
                                    dbCheck = true;
                                    bikerUsernameET.setError("Username Already Exists");
                                    bikerUsernameET.requestFocus();

                                    break;
                                } else {

                                    dbCheck = false;

                                }
                            }

                            if (!dbCheck) {
                                mDatabaseRef = mDatabase.getReference().child("Users").child("Bikers").child(Username);
                                mDatabaseRef.child("Username").setValue(Username);
                                mDatabaseRef.child("Name").setValue(Name);
                                mDatabaseRef.child("Password").setValue(Password);
                                mDatabaseRef.child("PhoneNo").setValue(PhoneNo);
                                mDatabaseRef.child("Address").setValue(Address);
                                mDatabaseRef.child("Role").setValue("Biker");
                                mDatabaseRef.child("AvailabilityStatus").setValue("Available");

                                mDatabaseRef2 = mDatabase.getReference().child("BikersStars").child(Username);
                                mDatabaseRef2.child("Name").setValue(Name);
                                mDatabaseRef2.child("FeedbackCount").setValue("1");
                                mDatabaseRef2.child("StarsSum").setValue("5");
                                mDatabaseRef2.child("TotalRating").setValue("5");


                                loadingDialog.dismissDialog();

                                bikerUsernameET.setText("");
                                bikerNameET.setText("");
                                bikerPasswordET.setText("");
                                bikerPhoneNoET.setText("");
                                bikerAddressET.setText("");

                                Toast.makeText(AddBikers.this, "Biker Added Successfully.", Toast.LENGTH_SHORT).show();
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
        View view = layoutInflater.inflate(R.layout.dialog_add_biker, null);
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
        Intent it = new Intent(getApplicationContext(), ManageBikers.class);
        startActivity(it);
        finish();
    }
}