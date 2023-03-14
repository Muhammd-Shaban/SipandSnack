package com.cust.sipnsnack;

import android.content.Intent;
import android.content.SharedPreferences;
import com.cust.sipnsnack.ManagerDashboard.DashBoard;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.cust.sipnsnack.Admin.AdminView;
import com.cust.sipnsnack.Bikers.BikersView;
import com.cust.sipnsnack.Customers.CustomerView;

import com.example.sipnsnack.R;

public class SplashScreen extends AppCompatActivity {

    TextView tv_AppName;
    LottieAnimationView lottie;
    SharedPreferences mySPR;
    String status = "", user = "", verified = "", email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mySPR = getSharedPreferences("LoginSPR", MODE_PRIVATE);

        tv_AppName = findViewById(R.id.splashScreenAppName);
        lottie = findViewById(R.id.animationTextView);

        tv_AppName.animate().translationY(-1430).setDuration(2700).setStartDelay(0);
        lottie.animate().translationX(2000).setDuration(2000).setStartDelay(2900);

        status = mySPR.getString("Status", "");
        user  = mySPR.getString("User", "");
        verified = mySPR.getString("CustomerVerified", "");
        email = mySPR.getString("Email", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!CheckInternetConnectivity.isConnected(SplashScreen.this)) {

                    noInternetDialog();
                    Toast.makeText(SplashScreen.this, "NO INTERNET", Toast.LENGTH_SHORT).show();

                } else {
                    Intent it;
                    if (status.equals("")) {
                        it = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(it);
                        finish();
                    } else {
                        if (user.equals("Customer")) {

                            if (verified.equals("true")) {
                                it = new Intent(getApplicationContext(), CustomerView.class);
                            } else {
                                it = new Intent(getApplicationContext(), SendEmail.class);
                                it.putExtra("Email", email);
                            }
                            startActivity(it);
                            finish();
                        } else if (user.equals("Biker")) {
                            it = new Intent(getApplicationContext(), BikersView.class);
                            startActivity(it);
                            finish();
                        } else if (user.equals("Manager")) {
                            it = new Intent(getApplicationContext(), DashBoard.class);
                            startActivity(it);
                            finish();
                        } else if (user.equals("Admin")) {
                            it = new Intent(getApplicationContext(), AdminView.class);
                            startActivity(it);
                            finish();
                        }
                    }
                }
            }
        }, 5000);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void noInternetDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.no_internet_dialog, null);
        Button enterBTN = view.findViewById(R.id.enterBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        enterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
    }
}