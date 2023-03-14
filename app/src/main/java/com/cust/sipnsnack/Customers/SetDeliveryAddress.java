package com.cust.sipnsnack.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetDeliveryAddress extends AppCompatActivity {

    String qty, bill, address, username;
    EditText addressET;
    Button continueBtn, locationBtn;
    ImageView backBtn;

    String latitude, longitude;
    LoadingDialog loadingDialog;

    SharedPreferences spr;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_delivery_address);

        loadingDialog = new LoadingDialog(SetDeliveryAddress.this);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        username = spr.getString("Username", "");

        addressET = findViewById(R.id.addressET);
        continueBtn = findViewById(R.id.continueBTN);
        locationBtn = findViewById(R.id.currentBTN);
        backBtn = findViewById(R.id.backBTN);

        Intent it = getIntent();
        qty = it.getStringExtra("Qty");
        bill = it.getStringExtra("Bill");


        readFromDB(username);

        // Get Current Location ...
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SetDeliveryAddress.this);



        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                address = addressET.getText().toString();

                if (address.isEmpty()) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(SetDeliveryAddress.this, "Address must be filled...", Toast.LENGTH_SHORT).show();
                    addressET.setError("Address is Required !");
                    addressET.requestFocus();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CustomerOrderInfo.class);
                    intent.putExtra("Qty", qty);
                    intent.putExtra("Bill", bill);
                    intent.putExtra("Address", address);
                    intent.putExtra("Type", "Manual");
                    startActivity(intent);
                    finish();
                }
            }
        });


        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    getCurrentLocation();

                    Toast.makeText(SetDeliveryAddress.this, "Your Current Location has been Set !!", Toast.LENGTH_SHORT).show();

                    loadingDialog.dismissDialog();

                    Intent it = new Intent(getApplicationContext(), CustomerOrderInfo.class);
                    it.putExtra("Qty", qty);
                    it.putExtra("Bill", bill);
                    it.putExtra("Type", "CurrentLocation");
                    it.putExtra("Longitude", longitude);
                    it.putExtra("Latitude", latitude);

                    startActivity(it);
                    finish();

                } else {
                    loadingDialog.dismissDialog();

                    Toast.makeText(SetDeliveryAddress.this, "NOT PERMITTED", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(SetDeliveryAddress.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                    Toast.makeText(SetDeliveryAddress.this, "Now Try Again ...", Toast.LENGTH_SHORT).show();
                }



            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TotalBill.class));
                finish();
            }
        });

    }


    public void readFromDB(String userName) {
        loadingDialog.startLoadingDialog();

        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
                .child(userName).addListenerForSingleValueEvent(new ValueEventListener() {

            DataSnapshot dataSnapshot;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                this.dataSnapshot = snapshot;

                address = dataSnapshot.child("Address").getValue().toString();
                addressET.setText(address);

                loadingDialog.dismissDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission Required !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();

                    if (location != null) {
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());
                    } else {
                        LocationRequest locationRequest = new LocationRequest().
                                setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                Location location1 = locationResult.getLastLocation();
                                latitude = String.valueOf(location1.getLatitude());
                                longitude = String.valueOf(location1.getLongitude());
                            }
                        };

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), TotalBill.class));
        finish();
    }
}