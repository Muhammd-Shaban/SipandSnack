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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

public class FoodMenu extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    NavigationView navView;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView infoIconIV, profileIV;
    ListView list;
    String name;
    TextView categoryTV, welcome;
    SharedPreferences spr;
    LoadingDialog loadingDialog;
    String username;

    TextView nameTV, emailTV;

    String[] title = { "All", "Specials", "Pizza", "Burgers", "Fries", "Snacks", "Chilled Drinks",
            "Sea Foods", "Coffees" };
    Integer[] img = {R.drawable.ic_all, R.drawable.ic_special, R.drawable.pizza, R.drawable.hamburger,
    R.drawable.fries, R.drawable.nachos, R.drawable.champagne, R.drawable.seafood, R.drawable.coffee};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        loadingDialog = new LoadingDialog(FoodMenu.this);

        setUpToolbar();

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        username = spr.getString("Username", "");

        drawer = findViewById(R.id.drawer_layoutCustomer);
        navView = findViewById(R.id.navViewCustomer);
        infoIconIV = findViewById(R.id.infoIconIVFoodMenu);
        list = findViewById(R.id.menuList);
        welcome = findViewById(R.id.welcomeTV);
        profileIV = findViewById(R.id.profileIV);

        View headerView = navView.getHeaderView(0);
        emailTV = headerView.findViewById(R.id.nameTV);
        nameTV = headerView.findViewById(R.id.emailTV);

        getValues();

        welcome.setText("Food Menu");

        MenuAdapter adapter=new MenuAdapter(this, title, img);
        list.setAdapter(adapter);

        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerProfile.class));
                finish();
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                categoryTV = view.findViewById(R.id.title);
                name = categoryTV.getText().toString();

                Intent it = new Intent(getApplicationContext(), MenuItems.class);
                it.putExtra("CategoryName", name);
                startActivity(it);
                finish();
            }
        });

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

                if(item.getItemId() == R.id.customer_nav_logout) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        logoutDialog();
                    }
                }

                return false;
            }
        });

        infoIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodMenuDialog();
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.menu);

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

    public void foodMenuDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_food_menu_dialog, null);
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


    public void getValues() {

        loadingDialog.startLoadingDialog();

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