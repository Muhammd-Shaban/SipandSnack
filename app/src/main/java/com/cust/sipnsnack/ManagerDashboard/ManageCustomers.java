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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageCustomers extends AppCompatActivity {

    Toolbar toolbar;
    int loopSize;
    SearchView searchView;
    MyCustomersAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<Customers> customer;
    LoadingDialog loadingDialog;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customers);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Customer");

        loadingDialog = new LoadingDialog(ManageCustomers.this);

        recyclerView = (RecyclerView) findViewById(R.id.customers_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        backBtn = findViewById(R.id.backBTN);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
                finish();
            }
        });

        customer = new ArrayList<Customers>();

        ReadFromDB();
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


    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), DashBoard.class);
        startActivity(it);
        finish();
    }


    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
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

        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        MyCustomersData.customerUsername = new String[loopSize];
                        MyCustomersData.customerName = new String[loopSize];
                        MyCustomersData.customerCancelledOrders = new String[loopSize];
                        MyCustomersData.customerStatus = new String[loopSize];

                        loadingDialog.dismissDialog();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            MyCustomersData.customerUsername[j] = snapshot.child("Username").getValue().toString();
                            MyCustomersData.customerName[j] = snapshot.child("Name").getValue().toString();
                            MyCustomersData.customerCancelledOrders[j] = snapshot.child("CancelledOrders").getValue().toString();
                            MyCustomersData.customerStatus[j] = snapshot.child("Status").getValue().toString();

                            j++;
                        }


                        for (int i = 0; i < MyCustomersData.customerUsername.length; i++) {
                            customer.add(new Customers(
                                    MyCustomersData.customerUsername[i],
                                    MyCustomersData.customerName[i],
                                    MyCustomersData.customerCancelledOrders[i],
                                    MyCustomersData.customerStatus[i]
                            ));
                        }

                        if (loopSize == 0) {
                            Toast.makeText(ManageCustomers.this, "No Customers Founded ...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            myAdapter = new MyCustomersAdapter(customer);
                            recyclerView.setAdapter(myAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}