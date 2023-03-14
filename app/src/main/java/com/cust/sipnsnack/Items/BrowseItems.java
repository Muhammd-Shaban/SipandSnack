package com.cust.sipnsnack.Items;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BrowseItems extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    int loopSize, loopSize2, loopSize3, loopSize4, loopSize5, loopSize6, loopSize7, loopSize8, loopSize9;
    MyBrowseItemsAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<Items> item;
    Spinner categorySpinner;
    String itemCategory, ctg;
    LoadingDialog loadingDialog;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_items);

        categorySpinner = findViewById(R.id.categorySPN);
        backBtn = findViewById(R.id.backBTN);

        loadingDialog = new LoadingDialog(BrowseItems.this);

        recyclerView = (RecyclerView) findViewById(R.id.items_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        item = new ArrayList<Items>();

        List<String> listCategory = new ArrayList<String>();
        listCategory.add("All");
        listCategory.add("Fries");
        listCategory.add("Snacks");
        listCategory.add("Pizza");
        listCategory.add("Burgers");
        listCategory.add("Chilled Drinks");
        listCategory.add("Sea Foods");
        listCategory.add("Coffees");
        listCategory.add("Specials");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ManageItems.class));
                finish();
            }
        });

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listCategory);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter1);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    itemCategory = "All";
                    getItems(itemCategory);
                } else if (i == 1) {
                    itemCategory = "Fries";
                    getItems(itemCategory);
                } else if (i == 2) {
                    itemCategory = "Snacks";
                    getItems(itemCategory);
                } else if (i == 3) {
                    itemCategory = "Pizza";
                    getItems(itemCategory);
                } else if (i == 4) {
                    itemCategory = "Burgers";
                    getItems(itemCategory);
                } else if (i == 5) {
                    itemCategory = "Chilled Drinks";
                    getItems(itemCategory);
                } else if (i == 6) {
                    itemCategory = "Sea Foods";
                    getItems(itemCategory);
                } else if (i == 7) {
                    itemCategory = "Coffees";
                    getItems(itemCategory);
                } else if (i == 8) {
                    itemCategory = "Specials";
                    getItems(itemCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), ManageItems.class);
        startActivity(it);
        finish();
    }


    public void getItems(String category) {
        loadingDialog.startLoadingDialog();

        myAdapter = new MyBrowseItemsAdapter(item);
        if (category.equals("All")) {

            item.clear();
            myAdapter.notifyDataSetChanged();

            loopSize = 0;
            mDatabase.getInstance().getReference().child("Items")
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

            mDatabase.getInstance().getReference().child("Items")
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
                                Toast.makeText(BrowseItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                            }
                            myAdapter = new MyBrowseItemsAdapter(item);
                            recyclerView.setAdapter(myAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else {

            item.clear();
            myAdapter.notifyDataSetChanged();

            loopSize2 = 0;
            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ctg = snapshot.child("Category").getValue().toString();
                                if (ctg.equals(category)) {
                                    loopSize2++;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            mDatabase = null;

            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int j = 0;
                            MyItemsData.itemId = new String[loopSize2];
                            MyItemsData.itemName = new String[loopSize2];
                            MyItemsData.itemPrice = new String[loopSize2];
                            MyItemsData.itemCategory = new String[loopSize2];
                            MyItemsData.itemDescription = new String[loopSize2];
                            MyItemsData.itemSize = new String[loopSize2];
                            MyItemsData.itemURL = new String[loopSize2];

                            loadingDialog.dismissDialog();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                ctg = snapshot.child("Category").getValue().toString();

                                if (ctg.equals(category)) {

                                    MyItemsData.itemId[j] = snapshot.child("Id").getValue().toString();
                                    MyItemsData.itemName[j] = snapshot.child("Name").getValue().toString();
                                    MyItemsData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                                    MyItemsData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                                    MyItemsData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                                    MyItemsData.itemSize[j] = snapshot.child("Size").getValue().toString();
                                    MyItemsData.itemURL[j] = snapshot.child("ImageUrl").getValue().toString();

                                    j++;
                                }
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

                            if (loopSize2 == 0) {
                                Toast.makeText(BrowseItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                            }
                            myAdapter = new MyBrowseItemsAdapter(item);
                            recyclerView.setAdapter(myAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }


    void ReadFromDB(String category) {
        loadingDialog.startLoadingDialog();

        myAdapter = new MyBrowseItemsAdapter(item);
        if (category.equals("All")) {

            item.clear();
            myAdapter.notifyDataSetChanged();

            loopSize = 0;
            mDatabase.getInstance().getReference().child("Items")
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

            mDatabase.getInstance().getReference().child("Items")
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
                                Toast.makeText(BrowseItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                            }
                            myAdapter = new MyBrowseItemsAdapter(item);
                            recyclerView.setAdapter(myAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else if (category.equals("Fries")) {

            item.clear();
            myAdapter.notifyDataSetChanged();

            loopSize2 = 0;
            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ctg = snapshot.child("Category").getValue().toString();
                                if (ctg.equals("Fries")) {
                                    loopSize2++;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            mDatabase = null;

            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int j = 0;
                            MyItemsData.itemId = new String[loopSize2];
                            MyItemsData.itemName = new String[loopSize2];
                            MyItemsData.itemPrice = new String[loopSize2];
                            MyItemsData.itemCategory = new String[loopSize2];
                            MyItemsData.itemDescription = new String[loopSize2];
                            MyItemsData.itemSize = new String[loopSize2];

                            loadingDialog.dismissDialog();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                ctg = snapshot.child("Category").getValue().toString();

                                if (ctg.equals("Fries")) {

                                    MyItemsData.itemId[j] = snapshot.child("Id").getValue().toString();
                                    MyItemsData.itemName[j] = snapshot.child("Name").getValue().toString();
                                    MyItemsData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                                    MyItemsData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                                    MyItemsData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                                    MyItemsData.itemSize[j] = snapshot.child("Size").getValue().toString();
                                    j++;
                                }
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

                            if (loopSize2 == 0) {
                                Toast.makeText(BrowseItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                            }
                            myAdapter = new MyBrowseItemsAdapter(item);
                            recyclerView.setAdapter(myAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        else if (category.equals("Snacks")) {

            item.clear();
            myAdapter.notifyDataSetChanged();

            loopSize3 = 0;
            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ctg = snapshot.child("Category").getValue().toString();
                                if (ctg.equals("Snacks")) {
                                    loopSize3++;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            mDatabase = null;

            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int j = 0;
                            MyItemsData.itemId = new String[loopSize3];
                            MyItemsData.itemName = new String[loopSize3];
                            MyItemsData.itemPrice = new String[loopSize3];
                            MyItemsData.itemCategory = new String[loopSize3];
                            MyItemsData.itemDescription = new String[loopSize3];
                            MyItemsData.itemSize = new String[loopSize3];

                            loadingDialog.dismissDialog();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                ctg = snapshot.child("Category").getValue().toString();

                                if (ctg.equals("Snacks")) {

                                    MyItemsData.itemId[j] = snapshot.child("Id").getValue().toString();
                                    MyItemsData.itemName[j] = snapshot.child("Name").getValue().toString();
                                    MyItemsData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                                    MyItemsData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                                    MyItemsData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                                    MyItemsData.itemSize[j] = snapshot.child("Size").getValue().toString();
                                    j++;

                                }
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

                            if (loopSize3 == 0) {
                                Toast.makeText(BrowseItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                            }
                            myAdapter = new MyBrowseItemsAdapter(item);
                            recyclerView.setAdapter(myAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        else if (category.equals("Pizza")) {

            item.clear();
            myAdapter.notifyDataSetChanged();

            loopSize4 = 0;
            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ctg = snapshot.child("Category").getValue().toString();
                                if (ctg.equals("Pizza")) {
                                    loopSize4++;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            mDatabase = null;

            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int j = 0;
                            MyItemsData.itemId = new String[loopSize4];
                            MyItemsData.itemName = new String[loopSize4];
                            MyItemsData.itemPrice = new String[loopSize4];
                            MyItemsData.itemCategory = new String[loopSize4];
                            MyItemsData.itemDescription = new String[loopSize4];
                            MyItemsData.itemSize = new String[loopSize4];

                            loadingDialog.dismissDialog();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                ctg = snapshot.child("Category").getValue().toString();

                                if (ctg.equals("Pizza")) {

                                    MyItemsData.itemId[j] = snapshot.child("Id").getValue().toString();
                                    MyItemsData.itemName[j] = snapshot.child("Name").getValue().toString();
                                    MyItemsData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                                    MyItemsData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                                    MyItemsData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                                    MyItemsData.itemSize[j] = snapshot.child("Size").getValue().toString();
                                    j++;

                                }
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

                            if (loopSize4 == 0) {
                                Toast.makeText(BrowseItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                            }
                            myAdapter = new MyBrowseItemsAdapter(item);
                            recyclerView.setAdapter(myAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        else if (category.equals("Burgers")) {

            item.clear();
            myAdapter.notifyDataSetChanged();

            loopSize5 = 0;
            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ctg = snapshot.child("Category").getValue().toString();
                                if (ctg.equals("Burgers")) {
                                    loopSize5++;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            mDatabase = null;

            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int j = 0;
                            MyItemsData.itemId = new String[loopSize5];
                            MyItemsData.itemName = new String[loopSize5];
                            MyItemsData.itemPrice = new String[loopSize5];
                            MyItemsData.itemCategory = new String[loopSize5];
                            MyItemsData.itemDescription = new String[loopSize5];
                            MyItemsData.itemSize = new String[loopSize5];

                            loadingDialog.dismissDialog();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                ctg = snapshot.child("Category").getValue().toString();

                                if (ctg.equals("Burgers")) {

                                    MyItemsData.itemId[j] = snapshot.child("Id").getValue().toString();
                                    MyItemsData.itemName[j] = snapshot.child("Name").getValue().toString();
                                    MyItemsData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                                    MyItemsData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                                    MyItemsData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                                    MyItemsData.itemSize[j] = snapshot.child("Size").getValue().toString();
                                    j++;

                                }
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

                            if (loopSize5 == 0) {
                                Toast.makeText(BrowseItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                            }
                            myAdapter = new MyBrowseItemsAdapter(item);
                            recyclerView.setAdapter(myAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        else if (category.equals("Chilled Drinks")) {

            item.clear();
            myAdapter.notifyDataSetChanged();

            loopSize6 = 0;
            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ctg = snapshot.child("Category").getValue().toString();
                                if (ctg.equals("Chilled Drinks")) {
                                    loopSize6++;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            mDatabase = null;

            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int j = 0;
                            MyItemsData.itemId = new String[loopSize6];
                            MyItemsData.itemName = new String[loopSize6];
                            MyItemsData.itemPrice = new String[loopSize6];
                            MyItemsData.itemCategory = new String[loopSize6];
                            MyItemsData.itemDescription = new String[loopSize6];
                            MyItemsData.itemSize = new String[loopSize6];

                            loadingDialog.dismissDialog();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                ctg = snapshot.child("Category").getValue().toString();

                                if (ctg.equals("Chilled Drinks")) {

                                    MyItemsData.itemId[j] = snapshot.child("Id").getValue().toString();
                                    MyItemsData.itemName[j] = snapshot.child("Name").getValue().toString();
                                    MyItemsData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                                    MyItemsData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                                    MyItemsData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                                    MyItemsData.itemSize[j] = snapshot.child("Size").getValue().toString();
                                    j++;

                                }
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

                            if (loopSize6 == 0) {
                                Toast.makeText(BrowseItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                            }
                            myAdapter = new MyBrowseItemsAdapter(item);
                            recyclerView.setAdapter(myAdapter);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        else if (category.equals("Sea Foods")) {

            item.clear();
            myAdapter.notifyDataSetChanged();

            loopSize7 = 0;
            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ctg = snapshot.child("Category").getValue().toString();
                                if (ctg.equals("Sea Foods")) {
                                    loopSize7++;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            mDatabase = null;

            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int j = 0;
                            MyItemsData.itemId = new String[loopSize7];
                            MyItemsData.itemName = new String[loopSize7];
                            MyItemsData.itemPrice = new String[loopSize7];
                            MyItemsData.itemCategory = new String[loopSize7];
                            MyItemsData.itemDescription = new String[loopSize7];
                            MyItemsData.itemSize = new String[loopSize7];

                            loadingDialog.dismissDialog();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                ctg = snapshot.child("Category").getValue().toString();

                                if (ctg.equals("Sea Foods")) {

                                    MyItemsData.itemId[j] = snapshot.child("Id").getValue().toString();
                                    MyItemsData.itemName[j] = snapshot.child("Name").getValue().toString();
                                    MyItemsData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                                    MyItemsData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                                    MyItemsData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                                    MyItemsData.itemSize[j] = snapshot.child("Size").getValue().toString();
                                    j++;

                                }
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

                            if (loopSize7 == 0) {
                                Toast.makeText(BrowseItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                            }
                                myAdapter = new MyBrowseItemsAdapter(item);
                                recyclerView.setAdapter(myAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        else if (category.equals("Coffees")) {

            item.clear();
            myAdapter.notifyDataSetChanged();

            loopSize8 = 0;
            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ctg = snapshot.child("Category").getValue().toString();
                                if (ctg.equals("Coffees")) {
                                    loopSize8++;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            mDatabase = null;

            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int j = 0;
                            MyItemsData.itemId = new String[loopSize8];
                            MyItemsData.itemName = new String[loopSize8];
                            MyItemsData.itemPrice = new String[loopSize8];
                            MyItemsData.itemCategory = new String[loopSize8];
                            MyItemsData.itemDescription = new String[loopSize8];
                            MyItemsData.itemSize = new String[loopSize8];

                            loadingDialog.dismissDialog();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                ctg = snapshot.child("Category").getValue().toString();

                                if (ctg.equals("Coffees")) {

                                    MyItemsData.itemId[j] = snapshot.child("Id").getValue().toString();
                                    MyItemsData.itemName[j] = snapshot.child("Name").getValue().toString();
                                    MyItemsData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                                    MyItemsData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                                    MyItemsData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                                    MyItemsData.itemSize[j] = snapshot.child("Size").getValue().toString();
                                    j++;

                                }
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

                            if (loopSize8 == 0) {
                                Toast.makeText(BrowseItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                            }
                            myAdapter = new MyBrowseItemsAdapter(item);
                            recyclerView.setAdapter(myAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        else if (category.equals("Specials")) {

            item.clear();
            myAdapter.notifyDataSetChanged();

            loopSize9 = 0;
            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ctg = snapshot.child("Category").getValue().toString();
                                if (ctg.equals("Specials")) {
                                    loopSize9++;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            mDatabase = null;

            mDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int j = 0;
                            MyItemsData.itemId = new String[loopSize9];
                            MyItemsData.itemName = new String[loopSize9];
                            MyItemsData.itemPrice = new String[loopSize9];
                            MyItemsData.itemCategory = new String[loopSize9];
                            MyItemsData.itemDescription = new String[loopSize9];
                            MyItemsData.itemSize = new String[loopSize9];

                            loadingDialog.dismissDialog();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                ctg = snapshot.child("Category").getValue().toString();

                                if (ctg.equals("Specials")) {

                                    MyItemsData.itemId[j] = snapshot.child("Id").getValue().toString();
                                    MyItemsData.itemName[j] = snapshot.child("Name").getValue().toString();
                                    MyItemsData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                                    MyItemsData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                                    MyItemsData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                                    MyItemsData.itemSize[j] = snapshot.child("Size").getValue().toString();
                                    j++;

                                }
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

                            if (loopSize9 == 0) {
                                Toast.makeText(BrowseItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                            }
                                myAdapter = new MyBrowseItemsAdapter(item);
                                recyclerView.setAdapter(myAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }
}