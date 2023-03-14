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
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DayWiseReports extends AppCompatActivity {

    Toolbar toolbar;
    int loopSize;
    SearchView searchView;
    DatesReportAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<DateWise> dateWises;
    LoadingDialog loadingDialog;
    ImageView backBtn;
    TextView emptyTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_wise_reports);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Date");

        loadingDialog = new LoadingDialog(DayWiseReports.this);

        recyclerView = (RecyclerView) findViewById(R.id.dailyReports_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        backBtn = findViewById(R.id.backBTN);
        emptyTV = findViewById(R.id.emptyTV);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewReports.class));
                finish();
            }
        });

        dateWises = new ArrayList<DateWise>();

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
        Intent it = new Intent(getApplicationContext(), ViewReports.class);
        startActivity(it);
        finish();
    }

    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("Reports").child("date_wise")
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

        FirebaseDatabase.getInstance().getReference().child("Reports").child("date_wise")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        DayWiseData.specials = new String[loopSize];
                        DayWiseData.pizza = new String[loopSize];
                        DayWiseData.burgers = new String[loopSize];
                        DayWiseData.fries = new String[loopSize];
                        DayWiseData.snacks = new String[loopSize];
                        DayWiseData.chilledDrinks = new String[loopSize];
                        DayWiseData.seaFoods = new String[loopSize];
                        DayWiseData.coffees = new String[loopSize];
                        DayWiseData.netSale = new String[loopSize];
                        DayWiseData.date = new String[loopSize];
                        DayWiseData.specials2 = new String[loopSize];
                        DayWiseData.pizza2 = new String[loopSize];
                        DayWiseData.burgers2 = new String[loopSize];
                        DayWiseData.fries2 = new String[loopSize];
                        DayWiseData.snacks2 = new String[loopSize];
                        DayWiseData.chilledDrinks2 = new String[loopSize];
                        DayWiseData.seaFoods2 = new String[loopSize];
                        DayWiseData.coffees2 = new String[loopSize];

                        loadingDialog.dismissDialog();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            DayWiseData.pizza[j] = snapshot.child("Pizza").getValue().toString();
                            DayWiseData.specials[j] = snapshot.child("Specials").getValue().toString();
                            DayWiseData.burgers[j] = snapshot.child("Burgers").getValue().toString();
                            DayWiseData.fries[j] = snapshot.child("Fries").getValue().toString();
                            DayWiseData.snacks[j] = snapshot.child("Snacks").getValue().toString();
                            DayWiseData.chilledDrinks[j] = snapshot.child("Chilled Drinks").getValue().toString();
                            DayWiseData.seaFoods[j] = snapshot.child("Sea Foods").getValue().toString();
                            DayWiseData.coffees[j] = snapshot.child("Coffees").getValue().toString();
                            DayWiseData.netSale[j] = snapshot.child("Net Sale").getValue().toString();
                            DayWiseData.date[j] = snapshot.child("Date").getValue().toString();

                            DayWiseData.pizza2[j] = snapshot.child("PizzaAmount").getValue().toString();
                            DayWiseData.specials2[j] = snapshot.child("SpecialsAmount").getValue().toString();
                            DayWiseData.burgers2[j] = snapshot.child("BurgersAmount").getValue().toString();
                            DayWiseData.fries2[j] = snapshot.child("FriesAmount").getValue().toString();
                            DayWiseData.snacks2[j] = snapshot.child("SnacksAmount").getValue().toString();
                            DayWiseData.chilledDrinks2[j] = snapshot.child("Chilled DrinksAmount").getValue().toString();
                            DayWiseData.seaFoods2[j] = snapshot.child("Sea FoodsAmount").getValue().toString();
                            DayWiseData.coffees2[j] = snapshot.child("CoffeesAmount").getValue().toString();

                            j++;
                        }


                        for (int i = 0; i < DayWiseData.date.length; i++) {
                            dateWises.add(new DateWise(
                                    DayWiseData.specials[i],
                                    DayWiseData.pizza[i],
                                    DayWiseData.burgers[i],
                                    DayWiseData.fries[i],
                                    DayWiseData.snacks[i],
                                    DayWiseData.chilledDrinks[i],
                                    DayWiseData.seaFoods[i],
                                    DayWiseData.coffees[i],
                                    DayWiseData.netSale[i],
                                    DayWiseData.date[i],
                                    DayWiseData.specials2[i],
                                    DayWiseData.pizza2[i],
                                    DayWiseData.burgers2[i],
                                    DayWiseData.fries2[i],
                                    DayWiseData.snacks2[i],
                                    DayWiseData.chilledDrinks2[i],
                                    DayWiseData.seaFoods2[i],
                                    DayWiseData.coffees2[i]

                            ));
                        }

                        if (loopSize == 0) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(DayWiseReports.this, "No Data Founded ...", Toast.LENGTH_SHORT).show();
                            emptyTV.setVisibility(View.VISIBLE);
                            toolbar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            loadingDialog.dismissDialog();
                            emptyTV.setVisibility(View.GONE);
                            toolbar.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);

                            myAdapter = new DatesReportAdapter(dateWises);
                            recyclerView.setAdapter(myAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}