package com.cust.sipnsnack.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cust.sipnsnack.LoginActivity;
import com.example.sipnsnack.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodCart extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    NavigationView navView;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView infoIconIV, profileIV, emptyIV;
    TextView welcomeTV;
    SharedPreferences spr;
    int loopSize = 0;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList <CartItems> myItem;
    LoadingDialog loadingDialog;
    public static String username;
    TextView tv;
    CartItemsAdapter myAdapter;
    Button confirmBtn, ClearBtn;
    TextView nameTV, emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cart);

        setUpToolbar();

        loadingDialog = new LoadingDialog(FoodCart.this);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);

        username = spr.getString("Username", "");

        drawer = findViewById(R.id.drawer_layoutCustomer);
        navView = findViewById(R.id.navViewCustomer);
        infoIconIV = findViewById(R.id.infoIconIVFoodCart);
        welcomeTV = findViewById(R.id.welcomeTV);
        tv = findViewById(R.id.emptyCartTV);
        confirmBtn = findViewById(R.id.confirmBTN);
        ClearBtn = findViewById(R.id.clearBTN);
        profileIV = findViewById(R.id.profileIV);
        emptyIV = findViewById(R.id.emptyCartIV);

        View headerView = navView.getHeaderView(0);
        emailTV = headerView.findViewById(R.id.nameTV);
        nameTV = headerView.findViewById(R.id.emailTV);

        getValues();

        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerProfile.class));
                finish();
            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), TotalBill.class);
                startActivity(it);
                finish();
            }
        });

        ClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCart();
            }
        });

        welcomeTV.setText("Cart");

        if (drawer.isDrawerOpen(navView)) {
            drawer.closeDrawer(navView);
        }

        infoIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodCartDialog();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.cart_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myItem = new ArrayList<CartItems>();

        ReadFromDB();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.customer_nav_profile) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), CustomerProfile.class);
                        startActivity(it);
                        finish();
                    }
                }

                if(item.getItemId() == R.id.customer_nav_popular) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), CustomerView.class);
                        startActivity(it);
                        finish();
                    }
                }

                if(item.getItemId() == R.id.customer_nav_changePWD) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), ChangePassword.class);
                        startActivity(it);
                        finish();
                    }
                }

                if(item.getItemId() == R.id.customer_nav_orders) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), OrdersHistory.class);
                        startActivity(it);
                        finish();
                    }
                }

                if(item.getItemId() == R.id.customer_nav_logout) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        logoutDialog();
                    }
                }

                return false;
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.cart);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popular:
                        startActivity(new Intent(getApplicationContext(), CustomerView.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu:
                        startActivity(new Intent(getApplicationContext(), FoodMenu.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.cart:
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), CustomerProfile.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    public void setUpToolbar() {
        drawer = findViewById(R.id.drawer_layoutCustomer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        actionBarDrawerToggle.syncState();

    }

    public void foodCartDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_food_cart_dialog, null);
        Button okBTN = view.findViewById(R.id.okBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void logoutDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_logout_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = spr.edit();
                editor.putString("User", "");
                editor.putString("Status", "");
                editor.putString("Username", "");
                editor.apply();


                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "Logout Successful ...", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        noBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), CustomerView.class);
        startActivity(it);
        finish();
    }


    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;
            FirebaseDatabase.getInstance().getReference().child("temp_order").child(username).child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.exists()) {
                                    loopSize++;
                                } else {
                                    loadingDialog.dismissDialog();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            FirebaseDatabase.getInstance().getReference().child("temp_order").child(username).child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int j = 0;
                                MyCartData.itemId = new String[loopSize];
                                MyCartData.itemName = new String[loopSize];
                                MyCartData.itemPrice = new String[loopSize];
                                MyCartData.itemCategory = new String[loopSize];
                                MyCartData.itemDescription = new String[loopSize];
                                MyCartData.itemSize = new String[loopSize];
                                MyCartData.itemURL = new String[loopSize];
                                MyCartData.itemTotalPrice = new String[loopSize];
                                MyCartData.itemQuantity = new String[loopSize];

                                tv.setVisibility(View.GONE);
                                emptyIV.setVisibility(View.GONE);

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    MyCartData.itemId[j] = snapshot.child("Id").getValue().toString();
                                    MyCartData.itemName[j] = snapshot.child("Name").getValue().toString();
                                    MyCartData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                                    MyCartData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                                    MyCartData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                                    MyCartData.itemSize[j] = snapshot.child("Size").getValue().toString();
                                    MyCartData.itemURL[j] = snapshot.child("ImageUrl").getValue().toString();
                                    MyCartData.itemQuantity[j] = snapshot.child("Quantity").getValue().toString();
                                    MyCartData.itemTotalPrice[j] = snapshot.child("TotalPrice").getValue().toString();
                                    j++;
                                }

                                for (int i = 0; i < MyCartData.itemId.length; i++) {
                                    myItem.add(new CartItems(
                                            MyCartData.itemId[i],
                                            MyCartData.itemName[i],
                                            MyCartData.itemCategory[i],
                                            MyCartData.itemPrice[i],
                                            MyCartData.itemDescription[i],
                                            MyCartData.itemSize[i],
                                            MyCartData.itemURL[i],
                                            MyCartData.itemQuantity[i],
                                            MyCartData.itemTotalPrice[i]
                                    ));
                                }

                                if (loopSize == 0) {
                                    loadingDialog.dismissDialog();
                                    Toast.makeText(FoodCart.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();

                                    tv.setVisibility(View.VISIBLE);
                                    emptyIV.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                    confirmBtn.setVisibility(View.GONE);
                                    ClearBtn.setVisibility(View.GONE);

                                } else {
                                    loadingDialog.dismissDialog();

                                    tv.setVisibility(View.GONE);
                                    emptyIV.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    confirmBtn.setVisibility(View.VISIBLE);
                                    ClearBtn.setVisibility(View.VISIBLE);

                                    myAdapter = new CartItemsAdapter(myItem);
                                    recyclerView.setAdapter(myAdapter);
                                }
                            } else {
                                loadingDialog.dismissDialog();
                                Toast.makeText(FoodCart.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();

                                tv.setVisibility(View.VISIBLE);
                                emptyIV.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                confirmBtn.setVisibility(View.GONE);
                                ClearBtn.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
    }

    public void clearCart() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_clear_cart_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("temp_order").
                        child(username).removeValue();
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), FoodCart.class);
                startActivity(intent);
                Toast.makeText(FoodCart.this, "Cart Cleared Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        noBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void getValues() {

        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        emailTV.setText(dataSnapshot.child("Email").getValue().toString());
                        nameTV.setText(dataSnapshot.child("Name").getValue().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}