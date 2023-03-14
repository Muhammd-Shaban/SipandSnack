package com.cust.sipnsnack.Bikers;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShareLiveLocation extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    ImageView backBtn, callBtn;
    String lon, lat, phoneNo, orderId;
    double longitude, latitude;

    TextView distanceTV;

    String src, dir;

    LatLng customerLocation, bikerLocation;
    GoogleMap map;
    SupportMapFragment mapFragment;
    private LocationManager manager;

    private DatabaseReference reference;
    Marker myMarker;

    Button track;

    private final int min_time = 1000;
    private final int min_distance = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_live_location);

        backBtn = findViewById(R.id.backIV);
        callBtn = findViewById(R.id.callIV);
        distanceTV = findViewById(R.id.distanceTV);
        track = findViewById(R.id.getDirectionBTN);

        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.google_map);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        lon = getIntent().getStringExtra("Longitude");
        lat = getIntent().getStringExtra("Latitude");
        phoneNo = getIntent().getStringExtra("PhoneNo");
        orderId = getIntent().getStringExtra("OrderId");

        reference = FirebaseDatabase.getInstance().getReference().child("LiveOrders").child(orderId);

        longitude = Double.parseDouble(lon);
        latitude = Double.parseDouble(lat);

        customerLocation = new LatLng(latitude, longitude);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                int height = 85;
                int width = 85;
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_home);
                Bitmap b = bitmapDraw.getBitmap();
                Bitmap customerMarker = Bitmap.createScaledBitmap(b, width, height, false);

                map.addMarker(new MarkerOptions().position(customerLocation).anchor(0.5f, 0.5f)
                        .title("Customer Location").icon(BitmapDescriptorFactory.fromBitmap(customerMarker)));

                bikerLocation = new LatLng(33.6438629, 73.163703);

                map.moveCamera(CameraUpdateFactory.newLatLng(bikerLocation));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(bikerLocation,16));

                double distanceRem = CalculationByDistance(bikerLocation, customerLocation);
                double roundOff = Math.round(distanceRem * 100.0) / 100.0;

                distanceTV.setText(roundOff + " km away.");
            }
        }, 3200);

        mapFragment.getMapAsync(this);

        getLocationUpdates();

        readChanges();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BikersView.class));
                finish();
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNo));
                startActivity(intent);
            }
        });

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder;
                List<Address> addresses = new ArrayList<Address>();
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(bikerLocation.latitude, bikerLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                src = addresses.get(0).getAddressLine(0);


                List<Address> addresses2 = new ArrayList<Address>();
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    addresses2 = geocoder.getFromLocation(customerLocation.latitude, customerLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                dir = addresses2.get(0).getAddressLine(0);

                Toast.makeText(ShareLiveLocation.this, "SRC = " + src + "\nDIR = " + dir, Toast.LENGTH_LONG).show();

                DisplayTrack(src, dir);
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

        bikerLocation = new LatLng(33.6438629, 73.163703);
        myMarker = map.addMarker(new MarkerOptions().position(bikerLocation).title("Biker Current Location")
                .icon(BitmapDescriptorFactory.fromBitmap(bikerMarker)));
        map.setMinZoomPreference(14);
        map.setMaxZoomPreference(20);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng()));
        map.moveCamera(CameraUpdateFactory.newLatLng(bikerLocation));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(bikerLocation,16));
    }


    public void readChanges() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {

                        MyLocation myLocation = snapshot.getValue(MyLocation.class);

                        if (myLocation != null) {
                            myMarker.setPosition(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 16));
                        }
                    } catch (Exception e) {
                        Toast.makeText(ShareLiveLocation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getLocationUpdates() {
        if (manager != null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, min_time, min_distance, this);

                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, min_time, min_distance, this);

                } else {

                    Toast.makeText(this, "No Provider Enabled !", Toast.LENGTH_SHORT).show();

                }

            } else {
                Toast.makeText(this, "NOT PERMITTED", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null) {
            saveLocation(location);
        } else {
            Toast.makeText(this, "No Location ...", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveLocation(Location location) {
        double distanceRem = CalculationByDistance(new LatLng(location.getLatitude(), location.getLongitude()), customerLocation);
        double roundOff = Math.round(distanceRem * 100.0) / 100.0;

        distanceTV.setText(roundOff + " km away.");

        reference.setValue(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), BikersView.class));
        finish();
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

        System.out.println("************ ====> Radius Value " + valueResult + "     KM   " + kmInDec + "   Meter   " + meterInDec);

        return Radius * c;
    }

    private void DisplayTrack(String sSource, String sDestination) {
        try {

            Uri uri=Uri.parse("https://www.google.co.in./maps/dir/"+sSource+"/"+sDestination);
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Uri uri=Uri.parse("https://www.google.co.in./maps/dir/"+sSource+"/"+sDestination);

            //Uri uri=Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}