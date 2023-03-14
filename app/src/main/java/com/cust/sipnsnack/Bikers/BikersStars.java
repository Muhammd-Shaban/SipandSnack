package com.cust.sipnsnack.Bikers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BikersStars extends AppCompatActivity {

    TextView starsTV, remarksTV;
    Button back;
    ImageView backBtn;
    RatingBar ratingBar;

    LoadingDialog loadingDialog;

    SharedPreferences spr;
    String username, stars;
    float sts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikers_stars);

        loadingDialog = new LoadingDialog(BikersStars.this);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        username = spr.getString("Username", "");

        starsTV = findViewById(R.id.starsCountTV);
        remarksTV = findViewById(R.id.remarksTV);
        back = findViewById(R.id.goBack);
        backBtn = findViewById(R.id.backBTN);
        ratingBar = findViewById(R.id.starsRating);

        loadingDialog.startLoadingDialog();

        FirebaseDatabase.getInstance().getReference().child("BikersStars").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {

            DataSnapshot dataSnapshot;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                this.dataSnapshot = snapshot;

                stars = dataSnapshot.child("TotalRating").getValue().toString();

                sts = Float.parseFloat(stars);

                if (sts <= 2.0) {
                    starsTV.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    remarksTV.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    remarksTV.setText("(Poor)");
                } else if (sts > 2.0 && sts <= 3.5) {
                    starsTV.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_green));
                    remarksTV.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_green));
                    remarksTV.setText("(Normal)");
                } else {
                    starsTV.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    remarksTV.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    remarksTV.setText("(Excellent)");
                }

                starsTV.setText(stars);
                ratingBar.setIsIndicator(true);
                ratingBar.setRating(sts);

                loadingDialog.dismissDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BikersView.class));
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BikersView.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), BikersView.class));
        finish();
    }
}