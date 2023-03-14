package com.cust.sipnsnack.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class MenuItems extends AppCompatActivity {

    MenuItemsAdapter myAdapter;
    int loopSize;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<MyItems> myItem;
    LoadingDialog loadingDialog;
    String category, dbCtg;
    TextView catNameTV;
    public static String username;
    SharedPreferences spr;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);

        catNameTV = findViewById(R.id.categoryNameTV);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        username = spr.getString("Username", "");

        loadingDialog = new LoadingDialog(MenuItems.this);

        Intent it = getIntent();
        category = it.getStringExtra("CategoryName");

        catNameTV.setText(category);

        recyclerView = (RecyclerView) findViewById(R.id.menu_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        backBtn = findViewById(R.id.backBTN);

        myItem = new ArrayList<MyItems>();

        ReadFromDB();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FoodMenu.class));
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), FoodMenu.class);
        startActivity(it);
        finish();
    }

    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;

        if (category.equals("All")) {
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
                                myItem.add(new MyItems(
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
                                Toast.makeText(MenuItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), FoodMenu.class);
                                startActivity(intent);
                                finish();
                            } else {
                                myAdapter = new MenuItemsAdapter(myItem);
                                recyclerView.setAdapter(myAdapter);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else {

            FirebaseDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                dbCtg = snapshot.child("Category").getValue().toString();

                                if (dbCtg.equals(category)) {
                                    loopSize++;
                                }
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

                                dbCtg = snapshot.child("Category").getValue().toString();

                                if (category.equals(dbCtg)) {
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
                                myItem.add(new MyItems(
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
                                Toast.makeText(MenuItems.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), FoodMenu.class);
                                startActivity(intent);
                                finish();
                            } else {
                                myAdapter = new MenuItemsAdapter(myItem);
                                recyclerView.setAdapter(myAdapter);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }
}