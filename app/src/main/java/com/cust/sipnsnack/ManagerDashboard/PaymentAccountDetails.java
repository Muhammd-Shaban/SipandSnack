package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentAccountDetails extends AppCompatActivity {

    TextInputEditText et1, et2, et3, et4;
    String name1, num1, name2, num2;
    Button updateBtn;
    ImageView backBtn, infoIcon;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();

    private FirebaseDatabase mDatabase2 = FirebaseDatabase.getInstance();
    private DatabaseReference mRef2 = mDatabase2.getReference();

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_account_details);

        loadingDialog = new LoadingDialog(PaymentAccountDetails.this);

        et1 = findViewById(R.id.bankAccNameET);
        et2 = findViewById(R.id.bankAccNumberET);
        et3 = findViewById(R.id.EasyPaisaAccNameET);
        et4 = findViewById(R.id.EasypaisaNumberET);
        updateBtn = findViewById(R.id.updateBTN);
        infoIcon = findViewById(R.id.infoIconIV);
        backBtn = findViewById(R.id.backBTN);

        loadingDialog.startLoadingDialog();
        getBankInfo();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();

                name1 = et1.getText().toString();
                name2 = et3.getText().toString();
                num1 = et2.getText().toString();
                num2 = et4.getText().toString();

                if (name1.equals("")) {
                    loadingDialog.dismissDialog();
                    et1.setError("Acc/Name is required !");
                    Toast.makeText(PaymentAccountDetails.this, "Please Fill all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (name2.equals("")) {
                    loadingDialog.dismissDialog();
                    et3.setError("Acc/Name is required !");
                    Toast.makeText(PaymentAccountDetails.this, "Please Fill all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (num1.equals("")) {
                    loadingDialog.dismissDialog();
                    et2.setError("Acc/Number is required !");
                    Toast.makeText(PaymentAccountDetails.this, "Please Fill all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (num2.equals("")) {
                    loadingDialog.dismissDialog();
                    et4.setError("Acc/Number is required !");
                    Toast.makeText(PaymentAccountDetails.this, "Please Fill all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    mRef = mDatabase.getReference().child("OnlinePaymentDetails").child("Bank");
                    mRef.child("AccountName").setValue(name1);
                    mRef.child("AccountNumber").setValue(num1);


                    mRef2 = mDatabase2.getReference().child("OnlinePaymentDetails").child("EasyPaisa");
                    mRef2.child("AccountName").setValue(name2);
                    mRef2.child("AccountPhoneNo").setValue(num2);

                    loadingDialog.dismissDialog();
                    Toast.makeText(PaymentAccountDetails.this, "Accounts Information Upadated !!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), DashBoard.class));
                    finish();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
                finish();
            }
        });

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccountsDialog();
            }
        });

    }

    public void getBankInfo() {

        FirebaseDatabase.getInstance().getReference().child("OnlinePaymentDetails").child("Bank")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        et1.setText(dataSnapshot.child("AccountName").getValue().toString());
                        et2.setText(dataSnapshot.child("AccountNumber").getValue().toString());

                        getEasypaisaInfo();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getEasypaisaInfo() {
        FirebaseDatabase.getInstance().getReference().child("OnlinePaymentDetails").child("EasyPaisa")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        et3.setText(dataSnapshot.child("AccountName").getValue().toString());
                        et4.setText(dataSnapshot.child("AccountPhoneNo").getValue().toString());

                        loadingDialog.dismissDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void updateAccountsDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_accounts_dialog, null);
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
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
        finish();
    }
}