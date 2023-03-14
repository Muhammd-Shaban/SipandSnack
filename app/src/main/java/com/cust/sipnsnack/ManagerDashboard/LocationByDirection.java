package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.sipnsnack.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationByDirection extends FragmentActivity implements OnMapReadyCallback {

    ImageView backBtn, callBtn;
    String phone;

    GoogleMap map;
    SupportMapFragment mapFragment;

    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_by_direction);

        backBtn = findViewById(R.id.backIV);
        callBtn = findViewById(R.id.callIV);

        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.google_map);

        phone = getIntent().getStringExtra("PhoneNo");
        lat = Double.parseDouble(getIntent().getStringExtra("Latitude"));
        lon = Double.parseDouble(getIntent().getStringExtra("Longitude"));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                int height = 85;
                int width = 85;
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_home);
                Bitmap b = bitmapDraw.getBitmap();
                Bitmap customerMarker = Bitmap.createScaledBitmap(b, width, height, false);

                LatLng latLng = new LatLng(lat, lon);
                map.addMarker(new MarkerOptions().position(latLng).title("Customer Location").anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromBitmap(customerMarker)));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));

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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
        finish();
    }
}