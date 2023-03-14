package com.cust.sipnsnack.Customers;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cust.sipnsnack.LoginActivity;

import com.example.sipnsnack.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CustomerView extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    LoadingDialog loadingDialog;
    ImageView img1, img2, img3, img4;
    String name, username, phoneNo, password, address;
    TextView welcomeNote;
    SharedPreferences spr;
    NavigationView navView;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView infoIconIV, profileIV;
    TextView nameTV, emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view);

        setUpToolbar();

        loadingDialog = new LoadingDialog(CustomerView.this);
        loadingDialog.startLoadingDialog();

        drawer = findViewById(R.id.drawer_layoutCustomer);

        img1 = findViewById(R.id.image1);
        img2 = findViewById(R.id.image2);
        img3 = findViewById(R.id.image3);
        img4 = findViewById(R.id.image4);
        navView = findViewById(R.id.navViewCustomer);
        infoIconIV = findViewById(R.id.infoIconIVCustomerView);
        profileIV = findViewById(R.id.profileIV);

        View headerView = navView.getHeaderView(0);
        emailTV = headerView.findViewById(R.id.nameTV);
        nameTV = headerView.findViewById(R.id.emailTV);


        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        username = spr.getString("Username", "");
        getValues();

        final DatabaseReference orderPlaceNode = FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").push();

        welcomeNote = findViewById(R.id.welcomeTV);

        infoIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerViewDialog();
            }
        });

        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerProfile.class));
                finish();
            }
        });

        setImage1();
        setImage2();
        setImage3();
        setImage4();

        if (drawer.isDrawerOpen(navView)) {
            drawer.closeDrawer(navView);
        }

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

                if(item.getItemId() == R.id.customer_nav_report) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), CustomerReport.class);
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

        readFromDB(username);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.popular);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popular:
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


    public void setImage1() {
        FirebaseDatabase.getInstance().getReference().child("Popular").child("Image1")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        String url = "";

                        try {
                            url = dataSnapshot.child("ImageUrl").getValue().toString();
                        } catch (Exception e) { }

                        if (url.equals("") || url.equals(null)) {
                            loadingDialog.dismissDialog();
                        } else {
                            loadPicture1(url);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void loadPicture1(String URL) {
        Picasso.get()
                .load(URL)
                .placeholder(R.drawable.logo)
                .into(img1);
        loadingDialog.dismissDialog();
    }

    public void setImage2() {
        FirebaseDatabase.getInstance().getReference().child("Popular").child("Image2")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        String url = "";

                        try {
                            url = dataSnapshot.child("ImageUrl").getValue().toString();
                        } catch (Exception e) { }

                        if (url.equals("") || url.equals(null)) {
                            loadingDialog.dismissDialog();
                        } else {
                            loadPicture2(url);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void loadPicture2(String URL) {
        Picasso.get()
                .load(URL)
                .placeholder(R.drawable.logo)
                .into(img2);
        loadingDialog.dismissDialog();
    }

    public void setImage3() {
        FirebaseDatabase.getInstance().getReference().child("Popular").child("Image3")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        String url = "";

                        try {
                            url = dataSnapshot.child("ImageUrl").getValue().toString();
                        } catch (Exception e) { }

                        if (url.equals("") || url.equals(null)) {
                            loadingDialog.dismissDialog();
                        } else {
                            loadPicture3(url);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void loadPicture3(String URL) {
        Picasso.get()
                .load(URL)
                .placeholder(R.drawable.logo)
                .into(img3);
        loadingDialog.dismissDialog();
    }

    public void setImage4() {
        FirebaseDatabase.getInstance().getReference().child("Popular").child("Image4")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        String url = "";

                        try {
                            url = dataSnapshot.child("ImageUrl").getValue().toString();
                        } catch (Exception e) { }

                        if (url.equals("") || url.equals(null)) {
                            loadingDialog.dismissDialog();
                        } else {
                            loadPicture4(url);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void loadPicture4(String URL) {
        Picasso.get()
                .load(URL)
                .placeholder(R.drawable.logo)
                .into(img4);
        loadingDialog.dismissDialog();
    }

    public void readFromDB(String userName) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
                .child(userName).addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;
                        System.out.println("Username : "+userName);
                        name = dataSnapshot.child("Name").getValue().toString();
                        password = dataSnapshot.child("Password").getValue().toString();
                        phoneNo = dataSnapshot.child("PhoneNo").getValue().toString();
                        address = dataSnapshot.child("Address").getValue().toString();

                        welcomeNote.setText("Welcome, " + name);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void customerViewDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_customer_view_dialog, null);
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


    public void getValues() {

        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        emailTV.setText(dataSnapshot.child("Email").getValue().toString());
                        nameTV.setText(dataSnapshot.child("Name").getValue().toString());

                        loadingDialog.dismissDialog();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}