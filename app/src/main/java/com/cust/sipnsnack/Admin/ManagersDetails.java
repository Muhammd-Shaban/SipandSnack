package com.cust.sipnsnack.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.FirebaseDatabase;

public class ManagersDetails extends AppCompatActivity {

    TextView usernameTV, nameTV, phoneNoTV;
    Button okBTN;
    ImageView edit, delete, backBtn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managers_details);

        usernameTV = findViewById(R.id.usernameRight);
        nameTV = findViewById(R.id.nameRight);
        phoneNoTV = findViewById(R.id.phoneNoRight);
        okBTN = findViewById(R.id.okManagersButton);
        edit = findViewById(R.id.managersDetailEditIV);
        delete = findViewById(R.id.managersDetailDeleteIV);
        backBtn = findViewById(R.id.backBTN);

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewManagers.class);
                startActivity(intent);
                finish();
            }
        });

        usernameTV.setText(MyManagersAdapter.username);
        nameTV.setText(MyManagersAdapter.name);
        phoneNoTV.setText(MyManagersAdapter.phone_no);


        // EDIT Image View
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), UpdateManager.class);
                startActivity(it);
                finish();
            }
        });

        // DELETE Image View
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               deleteManagerDialog();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), ViewManagers.class);
                startActivity(it);
                finish();
            }
        });
    }

    public void deleteManagerDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_delete_manager_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.getReference().child("Users").child("Managers").child(MyManagersAdapter.username).removeValue();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Context context = v.getContext();
                Intent intent = new Intent(context, ViewManagers.class);
                context.startActivity(intent);
                Toast.makeText(context, "Manager Deleted Successfully", Toast.LENGTH_SHORT).show();
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
        Intent it = new Intent(getApplicationContext(), ViewManagers.class);
        startActivity(it);
        finish();
    }
}