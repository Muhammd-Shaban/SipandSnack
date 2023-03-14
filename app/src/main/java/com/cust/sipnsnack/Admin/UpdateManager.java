package com.cust.sipnsnack.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class UpdateManager extends AppCompatActivity {

    TextInputEditText usernameET, nameET, phoneNoET;
    String username, name, phone_no;
    Button updateBtn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_manager);

        usernameET = findViewById(R.id.updateManager_UsernameET);
        nameET = findViewById(R.id.updateManager_NameET);
        phoneNoET = findViewById(R.id.updateManager_phoneNoET);
        backBtn = findViewById(R.id.backBTN);

        updateBtn = findViewById(R.id.managersUpdateButton);

        usernameET.setText(MyManagersAdapter.username);
        nameET.setText(MyManagersAdapter.name);
        phoneNoET.setText(MyManagersAdapter.phone_no);

        usernameET.setEnabled(false);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateManager();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewManagers.class));
                finish();
            }
        });
    }

    public void UpdateManager() {
        username = usernameET.getText().toString();
        name = nameET.getText().toString();
        phone_no = phoneNoET.getText().toString();

        if (username.equals("")) {
            usernameET.setError("User Name is Required");
        } else if (name.equals("")) {
            nameET.setError("Manager Name is Required");
        } else if (phone_no.equals("")) {
            phoneNoET.setError("Phone No is Required");
        } else {
            if (username.equals(MyManagersAdapter.username)) {
                FirebaseDatabase.getInstance().getReference().child("Users").child("Managers")
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            private DataSnapshot dataSnapshot;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                this.dataSnapshot = snapshot;

                                mRef = mDatabase.getReference().child("Users").child("Managers").child(username);
                                mRef.child("Username").setValue(username);
                                mRef.child("Name").setValue(name);
                                mRef.child("PhoneNo").setValue(phone_no);

                                Toast.makeText(UpdateManager.this, "Manager Updated Successfully ... ", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), ViewManagers.class);
                                startActivity(intent);
                                finish();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            } else {
                Toast.makeText(UpdateManager.this, "Manager Username is not Matching ...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ViewManagers.class));
        finish();
    }
}