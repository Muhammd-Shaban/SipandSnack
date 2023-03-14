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

public class ViewExpense extends AppCompatActivity {

    Toolbar toolbar;
    int loopSize;
    SearchView searchView;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    LoadingDialog loadingDialog;
    ImageView backBtn;
    TextView emptyTV;
    ExpenseMonthAdapter myAdapter;
    ArrayList<ExpenseMonth> monthExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Date");

        loadingDialog = new LoadingDialog(ViewExpense.this);

        recyclerView = (RecyclerView) findViewById(R.id.expense_months_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        backBtn = findViewById(R.id.backBTN);
        emptyTV = findViewById(R.id.emptyTV);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ExpenseOption.class));
                finish();
            }
        });

        monthExpenses = new ArrayList<ExpenseMonth>();

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
        Intent it = new Intent(getApplicationContext(), ExpenseOption.class);
        startActivity(it);
        finish();
    }

    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("Expenses")
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

        FirebaseDatabase.getInstance().getReference().child("Expenses")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        ExpenseMonthData.date = new String[loopSize];
                        ExpenseMonthData.totalExpense = new String[loopSize];
                        ExpenseMonthData.crockery = new String[loopSize];
                        ExpenseMonthData.kitchen = new String[loopSize];
                        ExpenseMonthData.bikers = new String[loopSize];
                        ExpenseMonthData.maintenance = new String[loopSize];
                        ExpenseMonthData.others = new String[loopSize];

                        loadingDialog.dismissDialog();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ExpenseMonthData.date[j] = snapshot.child("Date").getValue().toString();
                            ExpenseMonthData.totalExpense[j] = snapshot.child("Total Expense").getValue().toString();
                            ExpenseMonthData.crockery[j] = snapshot.child("Crockery").getValue().toString();
                            ExpenseMonthData.kitchen[j] = snapshot.child("Kitchen").getValue().toString();
                            ExpenseMonthData.bikers[j] = snapshot.child("Bikers").getValue().toString();
                            ExpenseMonthData.maintenance[j] = snapshot.child("Maintenance").getValue().toString();
                            ExpenseMonthData.others[j] = snapshot.child("Others").getValue().toString();

                            j++;
                        }


                        for (int i = 0; i < ExpenseMonthData.date.length; i++) {
                            monthExpenses.add(new ExpenseMonth(
                                    ExpenseMonthData.date[i],
                                    ExpenseMonthData.totalExpense[i],
                                    ExpenseMonthData.crockery[i],
                                    ExpenseMonthData.kitchen[i],
                                    ExpenseMonthData.bikers[i],
                                    ExpenseMonthData.maintenance[i],
                                    ExpenseMonthData.others[i]
                            ));
                        }

                        if (loopSize == 0) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(ViewExpense.this, "No Data Founded ...", Toast.LENGTH_SHORT).show();
                            emptyTV.setVisibility(View.VISIBLE);
                            toolbar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            loadingDialog.dismissDialog();
                            emptyTV.setVisibility(View.GONE);
                            toolbar.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);

                            myAdapter = new ExpenseMonthAdapter(monthExpenses);
                            recyclerView.setAdapter(myAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}