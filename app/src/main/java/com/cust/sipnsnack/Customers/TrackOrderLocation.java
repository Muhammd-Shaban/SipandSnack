package com.cust.sipnsnack.Customers;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class TrackOrderLocation extends FragmentActivity implements OnMapReadyCallback {

    ImageView backBtn, callBtn;
    String phone, orderId, l1, l2;

    Double lon, lat;

    LatLng bikerLocation, customerLocation;
    GoogleMap map;
    SupportMapFragment mapFragment;

    private DatabaseReference reference;
    Marker myMarker;

    TextView distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order_location);

        backBtn = findViewById(R.id.backIV);
        callBtn = findViewById(R.id.callIV);

        distance = findViewById(R.id.distanceTV);

        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.google_map);

        l1 = CustomerOTWAdapter.longitude;
        l2 = CustomerOTWAdapter.latitude;
        phone = CustomerOTWAdapter.phoneNo;
        orderId = CustomerOTWAdapter.orderID;

        lon = Double.parseDouble(l1);
        lat = Double.parseDouble(l2);

        reference = FirebaseDatabase.getInstance().getReference().child("LiveOrders").child(orderId);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (lon != 0 || lat != 0) {
                    int height = 85;
                    int width = 85;
                    @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_home);
                    Bitmap b = bitmapDraw.getBitmap();
                    Bitmap customerMarker = Bitmap.createScaledBitmap(b, width, height, false);

                    customerLocation = new LatLng(lat, lon);
                    map.addMarker(new MarkerOptions().position(customerLocation).title("Your Location").anchor(0.5f, 0.5f)
                            .icon(BitmapDescriptorFactory.fromBitmap(customerMarker)));
                } else {
                    Toast.makeText(TrackOrderLocation.this, "Something wrong with directions !", Toast.LENGTH_LONG).show();
                }
            }
        }, 3200);

        mapFragment.getMapAsync(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerView.class));
                finish();
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        int height = 85;
        int width = 85;
        BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.loc_ico);
        Bitmap b = bitmapDraw.getBitmap();
        Bitmap bikerMarker = Bitmap.createScaledBitmap(b, width, height, false);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {

                        String l1 = snapshot.child("longitude").getValue().toString();
                        String l2 = snapshot.child("latitude").getValue().toString();

                        MyLocation myLocation = new MyLocation(Double.parseDouble(l1), Double.parseDouble(l2));

                        double distanceRem = CalculationByDistance(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), customerLocation);
                        double roundOff = Math.round(distanceRem * 100.0) / 100.0;

                        distance.setText(roundOff + " km away.");

                        bikerLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());


                        if (myLocation != null) {
                            myMarker = map.addMarker(new MarkerOptions().position(bikerLocation).title("Biker Current Location")
                                    .icon(BitmapDescriptorFactory.fromBitmap(bikerMarker)));
                            map.setMinZoomPreference(14);
                            map.setMaxZoomPreference(20);
                            map.getUiSettings().setZoomControlsEnabled(true);
                            map.getUiSettings().setAllGesturesEnabled(true);
                            // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng()));
                            map.moveCamera(CameraUpdateFactory.newLatLng(bikerLocation));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(bikerLocation,12));
                            //myMarker.setPosition(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
                        }
                    } catch (Exception e) {
                        System.out.println("Message : " + e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371; // radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));

        return Radius * c;
    }
}