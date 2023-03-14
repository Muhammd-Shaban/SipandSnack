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

public class EditAdminProfile extends AppCompatActivity {

    TextInputEditText customerNameET, customerUsernameET;
    String nam, user, pass;
    Button updateBtn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();
    String Name, Username;
    ImageView infoIcon, backBtn;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_admin_profile);

        loadingDialog = new LoadingDialog(EditAdminProfile.this);

        customerNameET = findViewById(R.id.updateProfile_NameET);
        customerUsernameET = findViewById(R.id.updateProfile_UsernameET);
        updateBtn = findViewById(R.id.profileUpdateButton);
        infoIcon = findViewById(R.id.infoiconIV);
        backBtn = findViewById(R.id.backBTN);

        Intent intent = getIntent();
        nam = intent.getStringExtra("Name");
        user = intent.getStringExtra("Username");
        pass = intent.getStringExtra("Password");

        customerNameET.setText(nam);
        customerUsernameET.setText(user);

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
                Intent it = new Intent(getApplicationContext(), AdminProfile.class);
                startActivity(it);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), AdminProfile.class);
        startActivity(it);
        finish();
    }

    public void updateProfile() {
        loadingDialog.startLoadingDialog();

        Name = customerNameET.getText().toString();
        Username = customerUsernameET.getText().toString();

        if (Name.equals("")) {
            customerNameET.setError("Admin Name is Required");
        } else if (Username.equals("")) {
            customerUsernameET.setError("Admin Username is Required");
        } else {
            if (Username.equals(user)) {
                FirebaseDatabase.getInstance().getReference().child("Users").child("Admin")
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            private DataSnapshot dataSnapshot;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                this.dataSnapshot = snapshot;
                                mRef = mDatabase.getReference().child("Users").child("Admin").child(Username);
                                mRef.child("Username").setValue(Username);
                                mRef.child("Name").setValue(Name);

                                loadingDialog.dismissDialog();
                                Toast.makeText(EditAdminProfile.this, "Profile Updated Successfully ... ", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), AdminProfile.class);
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