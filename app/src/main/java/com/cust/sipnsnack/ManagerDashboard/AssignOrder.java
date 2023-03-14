package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssignOrder extends AppCompatActivity {

    static String username, name, address, phoneNo, paymentType, acceptedBy, quantity, receipt,
            totalBill, date, time, ordId, adrType, lon, lat;
    Toolbar toolbar;
    int loopSize;
    SearchView searchView;
    BikersAdapter myAdapter;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<BikerModel> data;
    ArrayList<BikerModel> biker;
    LoadingDialog loadingDialog;
    String availabilityStatus;
    ImageView backBtn;
    Button backBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_order);

        loadingDialog = new LoadingDialog(AssignOrder.this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Bikers");

        backBtn = findViewById(R.id.backBTN);
        backBTN = findViewById(R.id.goBackBTN);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AcceptedOrderDetails.class));
                finish();
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AcceptedOrderDetails.class));
                finish();
            }
        });

        Intent it = getIntent();
        username = it.getStringExtra("Username");
        name = it.getStringExtra("Name");
        adrType = it.getStringExtra("AddressType");
        lon = it.getStringExtra("Longitude");
        lat = it.getStringExtra("Latitude");
        address = it.getStringExtra("Address");
        phoneNo = it.getStringExtra("Phone");
        paymentType = it.getStringExtra("Payment");
        acceptedBy = it.getStringExtra("AcceptedBy");
        quantity = it.getStringExtra("Quantity");
        totalBill = it.getStringExtra("TotalBill");
        receipt = it.getStringExtra("ReceiptImage");
        date = it.getStringExtra("Date");
        time = it.getStringExtra("Time");
        ordId = it.getStringExtra("OrderId");


        recyclerView = (RecyclerView) findViewById(R.id.bikers_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<BikerModel>();

        biker = new ArrayList<BikerModel>();

        ViewBikers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getProductFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public void ViewBikers() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("Users").child("Bikers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            availabilityStatus = snapshot.child("AvailabilityStatus").getValue().toString();
                            if (availabilityStatus.equals("Available")) {
                                loopSize++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Users").child("Bikers")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        BikersData.bikerUsername = new String[loopSize];
                        BikersData.bikerName = new String[loopSize];
                        BikersData.bikerPhoneNo = new String[loopSize];
                        BikersData.bikerAddress = new String[loopSize];
                        BikersData.bikerAvailability = new String[loopSize];

                        loadingDialog.dismissDialog();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            availabilityStatus = snapshot.child("AvailabilityStatus").getValue().toString();
                            if (availabilityStatus.equals("Available")) {
                                BikersData.bikerUsername[j] = snapshot.child("Username").getValue().toString();
                                BikersData.bikerName[j] = snapshot.child("Name").getValue().toString();
                                BikersData.bikerPhoneNo[j] = snapshot.child("PhoneNo").getValue().toString();
                                BikersData.bikerAddress[j] = snapshot.child("Address").getValue().toString();
                                BikersData.bikerAvailability[j] = snapshot.child("AvailabilityStatus").getValue().toString();

                                j++;
                            }
                        }


                        for (int i = 0; i < BikersData.bikerUsername.length; i++) {
                            biker.add(new BikerModel(
                                    BikersData.bikerUsername[i],
                                    BikersData.bikerName[i],
                                    BikersData.bikerPhoneNo[i],
                                    BikersData.bikerAddress[i],
                                    BikersData.bikerAvailability[i]
                            ));
                        }

                        if (loopSize == 0) {
                            Toast.makeText(AssignOrder.this, "No Biker Available at the Moment ...", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), AcceptedOrderDetails.class);
                            startActivity(intent);
                            finish();
                        } else {
                            myAdapter = new BikersAdapter(biker);
                            recyclerView.setAdapter(myAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AcceptedOrderDetails.class);
        startActivity(intent);
        finish();
    }
}