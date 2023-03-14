package com.cust.sipnsnack.Bikers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sipnsnack.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditBikerProfile extends AppCompatActivity {

    TextInputEditText NameET, UsernameET, PhoneNoET, AddressET;
    String nam, user, address, phone, pass;
    Button updateBtn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();
    String Name, Username, Address, Phone;
    ImageView infoIcon;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_biker_profile);

        loadingDialog = new LoadingDialog(EditBikerProfile.this);

        NameET = findViewById(R.id.updateProfile_NameET);
        UsernameET = findViewById(R.id.updateProfile_UsernameET);
        PhoneNoET = findViewById(R.id.updateProfile_PhoneNoET);
        AddressET = findViewById(R.id.updateProfile_AddressET);
        updateBtn = findViewById(R.id.profileUpdateButton);
        infoIcon = findViewById(R.id.infoiconIV);

        Intent intent = getIntent();
        nam = intent.getStringExtra("Name");
        user = intent.getStringExtra("Username");
        address = intent.getStringExtra("Address");
        phone = intent.getStringExtra("PhoneNo");
        pass = intent.getStringExtra("Password");

        NameET.setText(nam);
        UsernameET.setText(user);
        PhoneNoET.setText(phone);
        AddressET.setText(address);

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
        Intent it = new Intent(getApplicationContext(), BikerProfile.class);
        startActivity(it);
        finish();
    }

    public void updateProfile() {
        loadingDialog.startLoadingDialog();

        Name = NameET.getText().toString();
        Username = UsernameET.getText().toString();
        Phone = PhoneNoET.getText().toString();
        Address = AddressET.getText().toString();

        if (Name.equals("")) {
            NameET.setError("Name is Required");
        } else if (Username.equals("")) {
            UsernameET.setError("Username is Required");
        } else if (Phone.equals("")) {
            PhoneNoET.setError("Phone No is Required");
        } else if (Address.equals("")) {
            AddressET.setError("Address is Required");
        } else {
            if (Username.equals(user)) {
                mDatabase.getInstance().getReference().child("Users").child("Bikers")
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            private DataSnapshot dataSnapshot;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                this.dataSnapshot = snapshot;
                                mRef = mDatabase.getReference().child("Users").child("Bikers").child(Username);
                                mRef.child("Username").setValue(Username);
                                mRef.child("Name").setValue(Name);
                                mRef.child("PhoneNo").setValue(Phone);
                                mRef.child("Address").setValue(Address);

                                loadingDialog.dismissDialog();
                                Toast.makeText(EditBikerProfile.this, "Profile Updated Successfully ... ", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), BikerProfile.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            } else {
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