package com.cust.sipnsnack.Items;

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

public class ViewItems extends AppCompatActivity {

    Toolbar toolbar;
    int loopSize;
    SearchView searchView;
    MyItemsAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<Items> item;
    LoadingDialog loadingDialog;
    ImageView backBtn, notFound;
    TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        backBtn = findViewById(R.id.backBTN);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Items");

        notFound = findViewById(R.id.notFoundIV);
        empty = findViewById(R.id.emptyTV);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getApplicationContext(), ManageItems.class);
                startActivity(it);
                finish();
            }
        });

        loadingDialog = new LoadingDialog(ViewItems.this);

        recyclerView = (RecyclerView) findViewById(R.id.items_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        item = new ArrayList<Items>();

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
        Intent it = new Intent(getApplicationContext(), ManageItems.class);
        startActivity(it);
        finish();
    }


    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("Items")
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

        FirebaseDatabase.getInstance().getReference().child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        MyItemsData.itemId = new String[loopSize];
                        MyItemsData.itemName = new String[loopSize];
                        MyItemsData.itemPrice = new String[loopSize];
                        MyItemsData.itemCategory = new String[loopSize];
                        MyItemsData.itemDescription = new String[loopSize];
                        MyItemsData.itemSize = new String[loopSize];
                        MyItemsData.itemURL = new String[loopSize];

                        loadingDialog.dismissDialog();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            MyItemsData.itemId[j] = snapshot.child("Id").getValue().toString();
                            MyItemsData.itemName[j] = snapshot.child("Name").getValue().toString();
                            MyItemsData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                            MyItemsData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                            MyItemsData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                            MyItemsData.itemSize[j] = snapshot.child("Size").getValue().toString();
                            MyItemsData.itemURL[j] = snapshot.child("ImageUrl").getValue().toString();
                            j++;
                        }

                        for (int i = 0; i < MyItemsData.itemId.length; i++) {
                            item.add(new Items(
                                    MyItemsData.itemId[i],
                                    MyItemsData.itemName[i],
                                    MyItemsData.itemCategory[i],
                                    MyItemsData.itemPrice[i],
                                    MyItemsData.itemDescription[i],
                                    MyItemsData.itemSize[i],
                                    MyItemsData.itemURL[i]
                            ));
                        }

                        if (loopSize == 0) {
                            Toast.makeText(ViewItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                            notFound.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        } else {
                            notFound.setVisibility(View.GONE);
                            empty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            myAdapter = new MyItemsAdapter(item);
                            recyclerView.setAdapter(myAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}