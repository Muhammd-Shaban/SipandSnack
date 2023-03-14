package com.cust.sipnsnack.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerProfile extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView nameeTV, usernameTV, phoneNoTV, addressTV, welcomeNote;
    Button editBtn, logoutBtn;
    String name, username, address, phoneNo, password;
    SharedPreferences spr;
    NavigationView navView;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView infoIconIV, profileIV;
    LoadingDialog loadingDialog;

    TextView nameTV, emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        loadingDialog = new LoadingDialog(CustomerProfile.this);
        loadingDialog.startLoadingDialog();

        setUpToolbar();

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);

        drawer = findViewById(R.id.drawer_layoutCustomer);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        welcomeNote = findViewById(R.id.welcomeTV);

        bottomNavigationView.setSelectedItemId(R.id.profile);
        nameeTV = findViewById(R.id.nameTv);
        usernameTV = findViewById(R.id.usernameTv);
        phoneNoTV = findViewById(R.id.phoneNoTv);
        addressTV = findViewById(R.id.addressTv);
        editBtn = findViewById(R.id.editProfileBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        navView = findViewById(R.id.navViewCustomer);
        infoIconIV = findViewById(R.id.infoIconIVCustomerProfile);
        profileIV = findViewById(R.id.profileIV);

        View headerView = navView.getHeaderView(0);
        emailTV = headerView.findViewById(R.id.nameTV);
        nameTV = headerView.findViewById(R.id.emailTV);

        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerProfile.class));
                finish();
            }
        });

        if (drawer.isDrawerOpen(navView)) {
            drawer.closeDrawer(navView);
        }

        username = spr.getString("Username", "");

        readFromDB(username);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(getApplicationContext(), EditProfile.class);
                it.putExtra("Name", name);
                it.putExtra("Username", username);
                it.putExtra("PhoneNo", phoneNo);
                it.putExtra("Address", address);
                startActivity(it);
                finish();

            }
        });

        infoIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerProfileDialog();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog();
            }
        });


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

                    if(item.getItemId() == R.id.customer_nav_orders) {
                        if(drawer.isDrawerOpen(navView)) {
                            drawer.closeDrawer(navView);
                            Intent it = new Intent(getApplicationContext(), OrdersHistory.class);
                            startActivity(it);
                            finish();
                        }
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
                        startActivity(new Intent(getApplicationContext(), FoodCart.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
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


    public void readFromDB(String userName) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
                .child(userName).addListenerForSingleValueEvent(new ValueEventListener() {

            DataSnapshot dataSnapshot;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                this.dataSnapshot = snapshot;

                name = dataSnapshot.child("Name").getValue().toString();
                password = dataSnapshot.child("Password").getValue().toString();
                phoneNo = dataSnapshot.child("PhoneNo").getValue().toString();
                address = dataSnapshot.child("Address").getValue().toString();


                emailTV.setText(dataSnapshot.child("Email").getValue().toString());
                nameTV.setText(dataSnapshot.child("Name").getValue().toString());

                nameeTV.setText(name);
                usernameTV.setText(userName);
                phoneNoTV.setText(phoneNo);
                addressTV.setText(address);
                welcomeNote.setText("Welcome, "+ name);

                loadingDialog.dismissDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void customerProfileDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_customer_profile_dialog, null);
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(navView)) {
            drawer.closeDrawer(navView);
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
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

                FirebaseAuth.getInstance().signOut();

                SharedPreferences.Editor editor = spr.edit();
                editor.putString("User", "");
                editor.putString("Status", "");
                editor.putString("Username", "");
                editor.putString("Email", "");
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

}