package com.cust.sipnsnack.Customers;

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

public class EditProfile extends AppCompatActivity {

    TextInputEditText customerNameET, customerUsernameET, customerPhoneNoET, customerAddressET;
    String nam, user, address, phone, pass;
    Button updateBtn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();
    String Name, Username, Address, Phone;
    LoadingDialog loadingDialog;
    ImageView infoIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        loadingDialog = new LoadingDialog(EditProfile.this);

        customerNameET = findViewById(R.id.updateProfile_NameET);
        customerUsernameET = findViewById(R.id.updateProfile_UsernameET);
        customerPhoneNoET = findViewById(R.id.updateProfile_PhoneNoET);
        customerAddressET = findViewById(R.id.updateProfile_AddressET);
        updateBtn = findViewById(R.id.profileUpdateButton);
        infoIcon = findViewById(R.id.infoiconIV);

        Intent intent = getIntent();
        nam = intent.getStringExtra("Name");
        user = intent.getStringExtra("Username");
        address = intent.getStringExtra("Address");
        phone = intent.getStringExtra("PhoneNo");
        pass = intent.getStringExtra("Password");

        customerNameET.setText(nam);
        customerUsernameET.setText(user);
        customerPhoneNoET.setText(phone);
        customerAddressET.setText(address);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileDialog();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), CustomerView.class);
        startActivity(it);
        finish();
    }

    public void updateProfile() {
        loadingDialog.startLoadingDialog();

        Name = customerNameET.getText().toString();
        Username = customerUsernameET.getText().toString();
        Phone = customerPhoneNoET.getText().toString();
        Address = customerAddressET.getText().toString();

        if (Name.equals("")) {
            loadingDialog.dismissDialog();
            customerNameET.setError("Customer Name is Required");
        } else if (Username.equals("")) {
            loadingDialog.dismissDialog();
            customerUsernameET.setError("Customer Username is Required");
        } else if (Phone.equals("")) {
            loadingDialog.dismissDialog();
            customerPhoneNoET.setError("Customer Phone No is Required");
        } else if (Address.equals("")) {
            loadingDialog.dismissDialog();
            customerAddressET.setError("Customer Address is Required");
        } else {
            if (Username.equals(user)) {
                mDatabase.getInstance().getReference().child("Users").child("Customers")
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            private DataSnapshot dataSnapshot;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                this.dataSnapshot = snapshot;
                                 mRef = mDatabase.getReference().child("Users").child("Customers").child(Username);
                                 mRef.child("Username").setValue(Username);
                                 mRef.child("Name").setValue(Name);
                                 mRef.child("PhoneNo").setValue(Phone);
                                 mRef.child("Address").setValue(Address);

                                 loadingDialog.dismissDialog();
                                 Toast.makeText(EditProfile.this, "Profile Updated Successfully ... ", Toast.LENGTH_SHORT).show();

                                 Intent intent = new Intent(getApplicationContext(), CustomerProfile.class);
                                 startActivity(intent);
                                 finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            } else {
                loadingDialog.dismissDialog();
                Toast.makeText(this, "Username is not Matching ...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateProfileDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_update_profile_dialog, null);
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