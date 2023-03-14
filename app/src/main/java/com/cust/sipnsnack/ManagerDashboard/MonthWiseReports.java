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

public class MonthWiseReports extends AppCompatActivity {

    Toolbar toolbar;
    int loopSize;
    SearchView searchView;
    MonthsReportAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList <ReportsModel> monthWises;
    LoadingDialog loadingDialog;
    ImageView backBtn;
    TextView emptyTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_wise_reports);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Month");

        loadingDialog = new LoadingDialog(MonthWiseReports.this);

        recyclerView = (RecyclerView) findViewById(R.id.monthlyReports_recycler_view);
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

        monthWises = new ArrayList<ReportsModel>();

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
        FirebaseDatabase.getInstance().getReference().child("Reports").child("month_wise")
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

        FirebaseDatabase.getInstance().getReference().child("Reports").child("month_wise")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        MonthWiseData.specials = new String[loopSize];
                        MonthWiseData.pizza = new String[loopSize];
                        MonthWiseData.burgers = new String[loopSize];
                        MonthWiseData.fries = new String[loopSize];
                        MonthWiseData.snacks = new String[loopSize];
                        MonthWiseData.chilledDrinks = new String[loopSize];
                        MonthWiseData.seaFoods = new String[loopSize];
                        MonthWiseData.coffees = new String[loopSize];
                        MonthWiseData.netSale = new String[loopSize];
                        MonthWiseData.month = new String[loopSize];
                        MonthWiseData.specials2 = new String[loopSize];
                        MonthWiseData.pizza2 = new String[loopSize];
                        MonthWiseData.burgers2 = new String[loopSize];
                        MonthWiseData.fries2 = new String[loopSize];
                        MonthWiseData.snacks2 = new String[loopSize];
                        MonthWiseData.chilledDrinks2 = new String[loopSize];
                        MonthWiseData.seaFoods2 = new String[loopSize];
                        MonthWiseData.coffees2 = new String[loopSize];

                        loadingDialog.dismissDialog();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            MonthWiseData.pizza[j] = snapshot.child("Pizza").getValue().toString();
                            MonthWiseData.specials[j] = snapshot.child("Specials").getValue().toString();
                            MonthWiseData.burgers[j] = snapshot.child("Burgers").getValue().toString();
                            MonthWiseData.fries[j] = snapshot.child("Fries").getValue().toString();
                            MonthWiseData.snacks[j] = snapshot.child("Snacks").getValue().toString();
                            MonthWiseData.chilledDrinks[j] = snapshot.child("Chilled Drinks").getValue().toString();
                            MonthWiseData.seaFoods[j] = snapshot.child("Sea Foods").getValue().toString();
                            MonthWiseData.coffees[j] = snapshot.child("Coffees").getValue().toString();
                            MonthWiseData.netSale[j] = snapshot.child("Net Sale").getValue().toString();
                            MonthWiseData.month[j] = snapshot.child("Month").getValue().toString();

                            MonthWiseData.pizza2[j] = snapshot.child("PizzaAmount").getValue().toString();
                            MonthWiseData.specials2[j] = snapshot.child("SpecialsAmount").getValue().toString();
                            MonthWiseData.burgers2[j] = snapshot.child("BurgersAmount").getValue().toString();
                            MonthWiseData.fries2[j] = snapshot.child("FriesAmount").getValue().toString();
                            MonthWiseData.snacks2[j] = snapshot.child("SnacksAmount").getValue().toString();
                            MonthWiseData.chilledDrinks2[j] = snapshot.child("Chilled DrinksAmount").getValue().toString();
                            MonthWiseData.seaFoods2[j] = snapshot.child("Sea FoodsAmount").getValue().toString();
                            MonthWiseData.coffees2[j] = snapshot.child("CoffeesAmount").getValue().toString();

                            j++;
                        }


                        for (int i = 0; i < MonthWiseData.month.length; i++) {
                            monthWises.add(new ReportsModel(
                                    MonthWiseData.specials[i],
                                    MonthWiseData.pizza[i],
                                    MonthWiseData.burgers[i],
                                    MonthWiseData.fries[i],
                                    MonthWiseData.snacks[i],
                                    MonthWiseData.chilledDrinks[i],
                                    MonthWiseData.seaFoods[i],
                                    MonthWiseData.coffees[i],
                                    MonthWiseData.netSale[i],
                                    MonthWiseData.month[i],
                                    MonthWiseData.specials2[i],
                                    MonthWiseData.pizza2[i],
                                    MonthWiseData.burgers2[i],
                                    MonthWiseData.fries2[i],
                                    MonthWiseData.snacks2[i],
                                    MonthWiseData.chilledDrinks2[i],
                                    MonthWiseData.seaFoods2[i],
                                    MonthWiseData.coffees2[i]
                            ));
                        }

                        if (loopSize == 0) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(MonthWiseReports.this, "No Data Founded ...", Toast.LENGTH_SHORT).show();
                            emptyTV.setVisibility(View.VISIBLE);
                            toolbar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            loadingDialog.dismissDialog();
                            emptyTV.setVisibility(View.GONE);
                            toolbar.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);

                            myAdapter = new MonthsReportAdapter(monthWises);
                            recyclerView.setAdapter(myAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}