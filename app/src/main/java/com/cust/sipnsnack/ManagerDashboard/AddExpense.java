package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddExpense extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference();
    TextInputEditText expenseAmountET;
    Button addExpenseBTN;
    Spinner category;
    ImageView infoIcon, backBtn;
    String expenseAmount, expenseCategory;
    LoadingDialog loadingDialog;
    int cr, ki, bi, ma, ot, tl;
    String cy, kn, bs, me, os, total;
    String[] expenseNames = {"Crockery","Kitchen","Bikers","Maintenance","Others"};
    int expenseImages[] = {R.drawable.crockery, R.drawable.kitchen, R.drawable.biker, R.drawable.maintenance, R.drawable.expense};

    public static String getFormatDate() {
        Date date;
        DateFormat setDate;
        date = Calendar.getInstance().getTime();
        setDate = new SimpleDateFormat("dd_MM_yyyy");
        String current_date = setDate.format(date);
        return current_date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        loadingDialog = new LoadingDialog(AddExpense.this);
        expenseAmountET = findViewById(R.id.expense_AmountET);
        category = findViewById(R.id.categorySPN);
        infoIcon = findViewById(R.id.infoiconIV);
        backBtn = findViewById(R.id.backBTN);
        addExpenseBTN = findViewById(R.id.addExpenseBtn);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), expenseImages, expenseNames);
        category.setAdapter(customAdapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                expenseCategory = expenseNames[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ExpenseOption.class));
                finish();
            }
        });

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        addExpenseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                expenseAmount = expenseAmountET.getText().toString();

                if (expenseAmount.equals("")) {
                    expenseAmountET.setError("Expense Amount is Required");
                    loadingDialog.dismissDialog();
                } else if (Integer.parseInt(expenseAmount) <= 0) {
                    expenseAmountET.setError("Invalid Expense Amount");
                    loadingDialog.dismissDialog();
                } else {
                    String dt = getFormatDate();
                    getExpensesAmount(dt, expenseAmount);

                    loadingDialog.dismissDialog();
                    Toast.makeText(AddExpense.this, "Expense Added Successfully.", Toast.LENGTH_SHORT).show();

                    expenseAmountET.setText("");

                    expenseAmountET.requestFocus();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ExpenseOption.class));
        finish();
    }

    public void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_add_expense, null);
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


    public void getExpensesAmount(String dat, String exp) {
        FirebaseDatabase.getInstance().getReference().child("Expenses").child(dat)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        if (dataSnapshot.exists()) {
                            cy = dataSnapshot.child("Crockery").getValue().toString();
                            kn = dataSnapshot.child("Kitchen").getValue().toString();
                            bs = dataSnapshot.child("Bikers").getValue().toString();
                            me = dataSnapshot.child("Maintenance").getValue().toString();
                            os = dataSnapshot.child("Others").getValue().toString();

                            total = dataSnapshot.child("Total Expense").getValue().toString();

                        } else {
                            mDatabaseRef = mDatabase.getReference().child("Expenses").child(dat);

                            cy = kn = bs = me = os = total = "0";
                            mDatabaseRef.child("Crockery").setValue("0");
                            mDatabaseRef.child("Kitchen").setValue("0");
                            mDatabaseRef.child("Bikers").setValue("0");
                            mDatabaseRef.child("Maintenance").setValue("0");
                            mDatabaseRef.child("Others").setValue("0");
                            mDatabaseRef.child("Total Expense").setValue("0");
                            mDatabaseRef.child("Date").setValue(dat);

                        }

                        saveToDB(cy, kn, bs, me, os, total, exp);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public void saveToDB(String ss, String pa, String bs, String fs, String sn, String cs, String am) {

        cr = Integer.parseInt(ss);
        ki = Integer.parseInt(pa);
        bi = Integer.parseInt(bs);
        ma = Integer.parseInt(fs);
        ot = Integer.parseInt(sn);
        tl = Integer.parseInt(cs);


        if (expenseCategory.equals("Crockery")) {
            cr = cr + Integer.parseInt(am);
            tl = tl + Integer.parseInt(am);
        } else if (expenseCategory.equals("Kitchen")) {
            ki = ki + Integer.parseInt(am);
            tl = tl + Integer.parseInt(am);
        } else if (expenseCategory.equals("Bikers")) {
            bi = bi + Integer.parseInt(am);
            tl = tl + Integer.parseInt(am);
        } else if (expenseCategory.equals("Maintenance")) {
            ma = ma + Integer.parseInt(am);
            tl = tl + Integer.parseInt(am);
        } else if (expenseCategory.equals("Others")) {
            ot = ot + Integer.parseInt(am);
            tl = tl + Integer.parseInt(am);
        }


        mDatabaseRef = mDatabase.getReference().child("Expenses").child(getFormatDate());

        mDatabaseRef.child("Crockery").setValue(String.valueOf(cr));
        mDatabaseRef.child("Kitchen").setValue(String.valueOf(ki));
        mDatabaseRef.child("Bikers").setValue(String.valueOf(bi));
        mDatabaseRef.child("Maintenance").setValue(String.valueOf(ma));
        mDatabaseRef.child("Others").setValue(String.valueOf(ot));
        mDatabaseRef.child("Total Expense").setValue(String.valueOf(tl));
        mDatabaseRef.child("Date").setValue(getFormatDate());

    }
}