package com.cust.sipnsnack.Admin;

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

import com.cust.sipnsnack.LoginActivity;
import com.example.sipnsnack.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class AdminView extends AppCompatActivity {

    NavigationView navView;
    private int[] images;
    private AdminSliderAdapter adapter;
    private SliderView sliderView;
    TextView usernameTV;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    CardView managersCard, logoutCard;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    SharedPreferences sharedPreferences;
    ImageView infoIcon, profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        setUpToolbar();

        drawer = findViewById(R.id.drawer_layout);
        usernameTV = findViewById(R.id.usernameTV);

        sharedPreferences = getSharedPreferences("LoginSPR", MODE_PRIVATE);

        managersCard = findViewById(R.id.managersCard);
        logoutCard = findViewById(R.id.logoutCard);

        navView = findViewById(R.id.navView);
        infoIcon = findViewById(R.id.infoIconIVDashBoard);
        profileIcon = findViewById(R.id.profileIconIV);

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
                        Intent it = new Intent(getApplicationContext(), AdminProfile.class);
                        startActivity(it);
                        finish();
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

                if(item.getItemId() == R.id.nav_change_password) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), AdminPasswordChange.class);
                        startActivity(it);
                        finish();
                    }
                }


                return false;
            }
        });


        // Managers Card
        managersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), ManageManagers.class);
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
                Intent it = new Intent(getApplicationContext(), AdminProfile.class);
                startActivity(it);
                finish();
            }
        });


        images = new int[]{R.drawable.slider1, R.drawable.slider2, R.drawable.slider3
                , R.drawable.slider4, R.drawable.slider5};
        sliderView = findViewById(R.id.sliderView);

        adapter = new AdminSliderAdapter(images);
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(navView)) {
            drawer.closeDrawer(navView);
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}