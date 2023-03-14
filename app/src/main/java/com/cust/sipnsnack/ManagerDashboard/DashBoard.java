package com.cust.sipnsnack.ManagerDashboard;

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
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cust.sipnsnack.Bikers.ManageBikers;
import com.cust.sipnsnack.Items.ManageItems;
import com.cust.sipnsnack.LoginActivity;
import com.example.sipnsnack.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


public class DashBoard extends AppCompatActivity {

    NavigationView navView;
    private int[] images;
    private SliderAdapter adapter;
    private SliderView sliderView;
    TextView usernameTV;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    CardView productsCard, bikersCard, ordersCard, expenseCard, reportsCard, profileCard,
            customersCard, logoutCard;
    SharedPreferences sharedPreferences;
    ImageView infoIcon, profileIcon;
    static String pd, ac, ot, dl;
    int p, a, o, d;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        loadingDialog = new LoadingDialog(DashBoard.this);

        setUpToolbar();

        drawer = findViewById(R.id.drawer_layout);
        usernameTV = findViewById(R.id.usernameTV);

        sharedPreferences = getSharedPreferences("LoginSPR", MODE_PRIVATE);

        loadingDialog.startLoadingDialog();
        getOrdersCount();

        productsCard = findViewById(R.id.productCard);
        bikersCard = findViewById(R.id.bikersCard);
        ordersCard = findViewById(R.id.ordersCard);
        expenseCard = findViewById(R.id.expenseCard);
        reportsCard = findViewById(R.id.reportsCard);
        profileCard = findViewById(R.id.profileCard);
        customersCard = findViewById(R.id.customersCard);
        logoutCard = findViewById(R.id.logoutCard);
        navView = findViewById(R.id.navView);
        infoIcon = findViewById(R.id.infoIconIVDashBoard);
        profileIcon = findViewById(R.id.imageiconIV);

        if (drawer.isDrawerOpen(navView)) {
            drawer.closeDrawer(navView);
        }

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.nav_profile) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        showProfileDialog();
                    }
                }

                if(item.getItemId() == R.id.nav_about_app) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        aboutAppDialog();
                    }
                }

                if(item.getItemId() == R.id.nav_contactDev) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        developerDialog();
                    }
                }

                if(item.getItemId() == R.id.nav_logout) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        logoutDialog();
                    }
                }


                if(item.getItemId() == R.id.nav_popular) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), PopularItems.class);
                        startActivity(it);
                        finish();
                    }
                }


                if(item.getItemId() == R.id.nav_change_password) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), ManagerPasswordChange.class);
                        startActivity(it);
                        finish();
                    }
                }

                if(item.getItemId() == R.id.nav_reports) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), SystemReports.class);
                        startActivity(it);
                        finish();
                    }
                }

                if(item.getItemId() == R.id.nav_feedbacks) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), UserFeedbacks.class);
                        startActivity(it);
                        finish();
                    }
                }

                if(item.getItemId() == R.id.nav_online_payments) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), PaymentAccountDetails.class);
                        startActivity(it);
                        finish();
                    }
                }

                return false;
            }
        });


        // Product Card
        productsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), ManageItems.class);
                startActivity(it);
                finish();
            }
        });

        // Bikers Card
        bikersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), ManageBikers.class);
                startActivity(it);
                finish();
            }
        });

        // Orders Card
        ordersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), AllOrdersDetails.class);
                startActivity(it);
                finish();
            }
        });


        // Expense Card
        expenseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ExpenseOption.class));
                finish();
            }
        });


        // Reports Card
        reportsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewReports.class));
                finish();
            }
        });


        // Profile Card
        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfileDialog();
            }
        });


        // Customers Card
        customersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(getApplicationContext(), ManageCustomers.class);
                startActivity(it);
                finish();

            }
        });


        // Logout Card
        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logoutDialog();
            }
        });

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfileDialog();
            }
        });

        images = new int[]{R.drawable.slider1, R.drawable.slider2, R.drawable.slider3
                , R.drawable.slider4, R.drawable.slider5};
        sliderView = findViewById(R.id.sliderView);

        adapter = new SliderAdapter(images);
        sliderView.setSliderAdapter(adapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.DROP);
        sliderView.startAutoCycle();

    }

    public void setUpToolbar() {
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        actionBarDrawerToggle.syncState();

    }


    public void showProfileDialog() {
        Intent it = new Intent(getApplicationContext(), ManagerProfile.class);
        startActivity(it);
        finish();
    }


    public void developerDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.developer_dialog, null);
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


    public void aboutAppDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_app_dialog, null);
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

                SharedPreferences.Editor editor = sharedPreferences.edit();
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


    public void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_info_dashboard, null);
        Button okBTN = view.findViewById(R.id.okBTN);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void wipDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.work_in_process, null);
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


    public void getOrdersCount() {

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                p++;
                            }
                            pd = "PND ("+p+")";
                        } else {
                            pd = "PND (0)";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        FirebaseDatabase.getInstance().getReference().child("Orders").child("Accepted")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                a++;
                            }
                            ac = "ACT ("+a+")";
                        } else {
                            ac = "ACT (0)";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                o++;
                            }
                            ot = "OTW ("+o+")";
                        } else {
                            ot = "OTW (0)";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        FirebaseDatabase.getInstance().getReference().child("Orders").child("Delivered")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                d++;
                            }
                            dl = "DLV ("+d+")";
                        } else {
                            dl = "DLV (0)";
                        }

                        loadingDialog.dismissDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}