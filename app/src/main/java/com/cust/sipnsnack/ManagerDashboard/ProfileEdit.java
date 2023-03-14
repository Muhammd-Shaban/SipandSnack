package com.cust.sipnsnack.ManagerDashboard;

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

public class ProfileEdit extends AppCompatActivity {

    TextInputEditText customerNameET, customerUsernameET, customerPhoneNoET;
    String nam, user, phone, pass;
    Button updateBtn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();
    String Name, Username, Phone;
    LoadingDialog loadingDialog;
    ImageView infoIcon, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        loadingDialog = new LoadingDialog(ProfileEdit.this);

        customerNameET = findViewById(R.id.updateProfile_NameET);
        customerUsernameET = findViewById(R.id.updateProfile_UsernameET);
        customerPhoneNoET = findViewById(R.id.updateProfile_PhoneNoET);
        updateBtn = findViewById(R.id.profileUpdateButton);
        infoIcon = findViewById(R.id.infoiconIV);
        backBtn = findViewById(R.id.backBTN);

        Intent intent = getIntent();
        nam = intent.getStringExtra("Name");
        user = intent.getStringExtra("Username");
        phone = intent.getStringExtra("PhoneNo");
        pass = intent.getStringExtra("Password");

        customerNameET.setText(nam);
        customerUsernameET.setText(user);
        customerPhoneNoET.setText(phone);

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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), ManagerProfile.class);
                startActivity(it);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), ManagerProfile.class);
        startActivity(it);
        finish();
    }


    public void updateProfile() {
        loadingDialog.startLoadingDialog();

        Name = customerNameET.getText().toString();
        Username = customerUsernameET.getText().toString();
        Phone = customerPhoneNoET.getText().toString();

        if (Name.equals("")) {
            customerNameET.setError("Manager Name is Required");
        } else if (Username.equals("")) {
            customerUsernameET.setError("Manager Username is Required");
        } else if (Phone.equals("")) {
            customerPhoneNoET.setError("Manager Phone No is Required");
        } else {
            if (Username.equals(user)) {
                FirebaseDatabase.getInstance().getReference().child("Users").child("Managers")
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            private DataSnapshot dataSnapshot;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                this.dataSnapshot = snapshot;
                                mRef = mDatabase.getReference().child("Users").child("Managers").child(Username);
                                mRef.child("Username").setValue(Username);
                                mRef.child("Name").setValue(Name);
                                mRef.child("PhoneNo").setValue(Phone);

                                loadingDialog.dismissDialog();
                                Toast.makeText(ProfileEdit.this, "Profile Updated Successfully ... ", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), ManagerProfile.class);
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