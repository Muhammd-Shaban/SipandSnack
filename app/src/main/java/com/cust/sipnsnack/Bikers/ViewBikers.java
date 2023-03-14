package com.cust.sipnsnack.Bikers;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewBikers extends AppCompatActivity {

    Toolbar toolbar;
    int loopSize;
    SearchView searchView;
    MyBikersAdapter myAdapter;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<BikersModel> biker;
    LoadingDialog loadingDialog;
    ImageView backBtn, notFound;
    TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bikers);

        toolbar = findViewById(R.id.toolbar);
        backBtn = findViewById(R.id.backBTN);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Bikers");

        loadingDialog = new LoadingDialog(ViewBikers.this);

        notFound = findViewById(R.id.notFoundIV);
        empty = findViewById(R.id.emptyTV);

        recyclerView = (RecyclerView) findViewById(R.id.bikers_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        biker = new ArrayList<BikersModel>();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ManageBikers.class));
                finish();
            }
        });

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
                            loopSize++;
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
                        MyBikersData.bikerUsername = new String[loopSize];
                        MyBikersData.bikerName = new String[loopSize];
                        MyBikersData.bikerPhoneNo = new String[loopSize];
                        MyBikersData.bikerAddress = new String[loopSize];
                        MyBikersData.bikerAvailability = new String[loopSize];

                        loadingDialog.dismissDialog();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            MyBikersData.bikerUsername[j] = snapshot.child("Username").getValue().toString();
                            MyBikersData.bikerName[j] = snapshot.child("Name").getValue().toString();
                            MyBikersData.bikerPhoneNo[j] = snapshot.child("PhoneNo").getValue().toString();
                            MyBikersData.bikerAddress[j] = snapshot.child("Address").getValue().toString();
                            MyBikersData.bikerAvailability[j] = snapshot.child("AvailabilityStatus").getValue().toString();

                            j++;
                        }


                        for (int i = 0; i < MyBikersData.bikerUsername.length; i++) {
                            biker.add(new BikersModel(
                                    MyBikersData.bikerUsername[i], //ali
                                    MyBikersData.bikerName[i],
                                    MyBikersData.bikerPhoneNo[i],
                                    MyBikersData.bikerAddress[i],
                                    MyBikersData.bikerAvailability[i]
                            ));
                        }

                        if (loopSize == 0) {
                            Toast.makeText(ViewBikers.this, "No Biker Founded ...", Toast.LENGTH_SHORT).show();
                            empty.setVisibility(View.VISIBLE);
                            notFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        } else {
                            empty.setVisibility(View.GONE);
                            notFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            myAdapter = new MyBikersAdapter(biker);
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
        loadingDialog.dismissDialog();
        Intent it = new Intent(getApplicationContext(), ManageBikers.class);
        startActivity(it);
        finish();
    }
}