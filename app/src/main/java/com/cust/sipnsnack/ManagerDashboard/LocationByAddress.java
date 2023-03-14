package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class LocationByAddress extends FragmentActivity implements OnMapReadyCallback {

    ImageView backBtn, callBtn;
    String location, phone;

    GoogleMap map;
    SupportMapFragment mapFragment;
    List<Address> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_by_address);

        backBtn = findViewById(R.id.backIV);
        callBtn = findViewById(R.id.callIV);

        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.google_map);

        location = getIntent().getStringExtra("Address");
        phone = getIntent().getStringExtra("PhoneNo");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(location != null || !location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(LocationByAddress.this);
                    try
                    {
                        addressList = geocoder.getFromLocationName(location,1);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                    if (addressList.size() == 0) {
                        failedDialog();
                    } else {

                        int height = 85;
                        int width = 85;
                        BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_home);
                        Bitmap b = bitmapDraw.getBitmap();
                        Bitmap customerMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                        map.addMarker(new MarkerOptions().position(latLng).title(location).anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromBitmap(customerMarker)));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
                    }
                }
            }
        }, 3200);

        mapFragment.getMapAsync(this);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
        finish();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }


    public void failedDialog() {

        Toast.makeText(LocationByAddress.this, "Invalid Address by Customer ...\nPlease Call Customer for Address !!", Toast.LENGTH_LONG).show();

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.op_failed_dialog, null);
        Button enterBTN = view.findViewById(R.id.enterBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        enterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}