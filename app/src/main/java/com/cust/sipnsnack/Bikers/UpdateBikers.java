package com.cust.sipnsnack.Bikers;

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

public class UpdateBikers extends AppCompatActivity {

    TextInputEditText usernameET, nameET, phoneNoET, addressET;
    String username, name, phone_no, address;
    Button updateBtn;
    ImageView backBtn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bikers);

        usernameET = findViewById(R.id.updateBiker_UsernameET);
        nameET = findViewById(R.id.updateBiker_NameET);
        phoneNoET = findViewById(R.id.updateBiker_phoneNoET);
        addressET = findViewById(R.id.updateBiker_AddressET);
        backBtn = findViewById(R.id.backBTN);
        updateBtn = findViewById(R.id.bikersUpdateButton);

        usernameET.setText(MyBikersAdapter.username);
        nameET.setText(MyBikersAdapter.name);
        phoneNoET.setText(MyBikersAdapter.phone_no);
        addressET.setText(MyBikersAdapter.address);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewBikers.class));
                finish();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateBiker();

            }
        });
    }

    public void UpdateBiker() {
        username = usernameET.getText().toString();
        name = nameET.getText().toString();
        phone_no = phoneNoET.getText().toString();
        address = addressET.getText().toString();

        if (username.equals("")) {
            usernameET.setError("User Name is Required");
        } else if (name.equals("")) {
            nameET.setError("Biker Name is Required");
        } else if (phone_no.equals("")) {
            phoneNoET.setError("Phone No is Required");
        } else if (address.equals("")) {
            addressET.setError("Biker Address is Required");
        } else {
            if (username.equals(MyBikersAdapter.username)) {
                mDatabase.getInstance().getReference().child("Users").child("Bikers")
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            private DataSnapshot dataSnapshot;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                this.dataSnapshot = snapshot;

                                mRef = mDatabase.getReference().child("Users").child("Bikers").child(username);
                                mRef.child("Username").setValue(username);
                                mRef.child("Name").setValue(name);
                                mRef.child("PhoneNo").setValue(phone_no);
                                mRef.child("Address").setValue(address);

                                Toast.makeText(UpdateBikers.this, "Biker Updated Successfully ... ", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), ViewBikers.class);
                                startActivity(intent);
                                finish();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            } else {
                Toast.makeText(UpdateBikers.this, "Biker Username is not Matching ...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ViewBikers.class);
        startActivity(intent);
        finish();
    }
}