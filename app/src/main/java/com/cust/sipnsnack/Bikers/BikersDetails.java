package com.cust.sipnsnack.Bikers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BikersDetails extends AppCompatActivity {

    TextView usernameTV, nameTV, phoneNoTV, addressTV;
    Button okBTN;
    ImageView edit, delete,backBtn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    TextView starsTV, remarksTV;
    Button back;
    RatingBar ratingBar;

    LoadingDialog loadingDialog;
    String stars;
    float sts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikers_details);

        loadingDialog = new LoadingDialog(BikersDetails.this);

        usernameTV = findViewById(R.id.usernameRight);
        nameTV = findViewById(R.id.nameRight);
        phoneNoTV = findViewById(R.id.phoneNoRight);
        addressTV = findViewById(R.id.addressRight);
        okBTN = findViewById(R.id.okBikersButton);
        edit = findViewById(R.id.bikersDetailEditIV);
        delete = findViewById(R.id.bikersDetailDeleteIV);
        backBtn = findViewById(R.id.backBTN);
        starsTV = findViewById(R.id.starsCountTV);
        remarksTV = findViewById(R.id.remarksTV);
        back = findViewById(R.id.goBack);
        backBtn = findViewById(R.id.backBTN);
        ratingBar = findViewById(R.id.starsRating);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewBikers.class));
                finish();
            }
        });
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewBikers.class);
                startActivity(intent);
                finish();
            }
        });

        usernameTV.setText(MyBikersAdapter.username);
        nameTV.setText(MyBikersAdapter.name);
        phoneNoTV.setText(MyBikersAdapter.phone_no);
        addressTV.setText(MyBikersAdapter.address);


        loadingDialog.startLoadingDialog();

        FirebaseDatabase.getInstance().getReference().child("BikersStars").child(MyBikersAdapter.username)
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


        // EDIT IMAGE VIEW
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateBikers.class);
                startActivity(intent);
                finish();
            }
        });

        // DELETE IMAGE VIEW
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBikerDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ViewBikers.class);
        startActivity(intent);
        finish();
    }



    public void deleteBikerDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_delete_biker_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.getReference().child("Users").child("Bikers").child(MyBikersAdapter.username).removeValue();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Context context = v.getContext();
                Intent intent = new Intent(context, ViewBikers.class);
                context.startActivity(intent);
                Toast.makeText(context, "Biker Deleted Successfully", Toast.LENGTH_SHORT).show();
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