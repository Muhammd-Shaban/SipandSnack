package com.cust.sipnsnack.Admin;

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

public class ViewManagers extends AppCompatActivity {

    Toolbar toolbar;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    int loopSize;
    SearchView searchView;
    MyManagersAdapter myAdapter;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<ManagersModel> data;
    ArrayList<ManagersModel> manager;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_managers);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Managers");

        recyclerView = (RecyclerView) findViewById(R.id.managers_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        backBtn = findViewById(R.id.backBTN);

        data = new ArrayList<ManagersModel>();

        manager = new ArrayList<ManagersModel>();

        ViewManagers();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), ManageManagers.class);
                startActivity(it);
                finish();
            }
        });
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


    public void ViewManagers() {
        loopSize = 0;
        mDatabase.getInstance().getReference().child("Users").child("Managers")
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
        mDatabase = null;

        mDatabase.getInstance().getReference().child("Users").child("Managers")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        MyManagersData.managerUsername = new String[loopSize];
                        MyManagersData.managerName = new String[loopSize];
                        MyManagersData.managerPhoneNo = new String[loopSize];

                        //progress.setVisibility(View.GONE);

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            MyManagersData.managerUsername[j] = snapshot.child("Username").getValue().toString();
                            MyManagersData.managerName[j] = snapshot.child("Name").getValue().toString();
                            MyManagersData.managerPhoneNo[j] = snapshot.child("PhoneNo").getValue().toString();

                            j++;
                        }


                        for (int i = 0; i < MyManagersData.managerUsername.length; i++) {
                            manager.add(new ManagersModel(
                                    MyManagersData.managerName[i],
                                    MyManagersData.managerUsername[i],
                                    MyManagersData.managerPhoneNo[i]
                            ));
                        }

                        if (loopSize == 0) {
                            Toast.makeText(ViewManagers.this, "No Data Founded ...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ManageManagers.class);
                            startActivity(intent);
                            finish();
                        } else {
                            myAdapter = new MyManagersAdapter(manager);
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
        Intent it = new Intent(getApplicationContext(), ManageManagers.class);
        startActivity(it);
        finish();
    }
}