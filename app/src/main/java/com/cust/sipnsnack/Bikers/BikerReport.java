package com.cust.sipnsnack.Bikers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BikerReport extends AppCompatActivity {

    EditText issueET;
    Button submitBtn;
    String issue, username, name, phone;
    SharedPreferences spr;
    ImageView backBtn;

    LoadingDialog loadingDialog;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference();

    public static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        String dateString = dateFormat.format(new Date()).toString();
        return dateString;
    }

    public static String getTodayDate() {
        Date date;
        DateFormat setDate;
        date = Calendar.getInstance().getTime();
        setDate = new SimpleDateFormat("dd/MM/yyyy");
        String current_date = setDate.format(date);
        return current_date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biker_report);

        loadingDialog = new LoadingDialog(BikerReport.this);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        username = spr.getString("Username", "");

        getBikerInfo(username);

        issueET = findViewById(R.id.issueTXT);
        submitBtn = findViewById(R.id.submitBTN);
        backBtn = findViewById(R.id.backBTN);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();

                issue = issueET.getText().toString();

                if (issue.equals("")) {
                    loadingDialog.dismissDialog();

                    issueET.setError("Could not be empty");
                    issueET.requestFocus();
                } else {
                    mDatabaseRef = mDatabase.getReference().child("ReportsForSystem").child("Bikers").push();
                    mDatabaseRef.child("ReportText").setValue(issue);
                    mDatabaseRef.child("BikerUsername").setValue(username);
                    mDatabaseRef.child("BikerName").setValue(name);
                    mDatabaseRef.child("BikerPhoneNo").setValue(phone);
                    mDatabaseRef.child("ReportDate").setValue(getTodayDate());
                    mDatabaseRef.child("ReportTime").setValue(getTime());

                    loadingDialog.dismissDialog();

                    Toast.makeText(BikerReport.this, "Issue Reported Successfully", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), BikersView.class));
                    finish();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BikersView.class));
                finish();
            }
        });
    }

    public void getBikerInfo(String usn) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Bikers").child(usn)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        name = dataSnapshot.child("Name").getValue().toString();
                        phone = dataSnapshot.child("PhoneNo").getValue().toString();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), BikersView.class));
        finish();
    }
}