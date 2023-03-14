package com.cust.sipnsnack.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TotalBill extends AppCompatActivity {

    SharedPreferences spr;
    String username;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    int loopSize;
    int dbPrice = 0, totalBill = 0, dbQty = 0, totalQty = 0;
    ArrayList<CartItems> myItem;
    LoadingDialog loadingDialog;
    Button proceedBtn;
    BillItemsAdapter billItemsAdapter;
    TextView billTV, qtyTV;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_bill);

        loadingDialog = new LoadingDialog(TotalBill.this);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        username = spr.getString("Username", "");
        backBtn = findViewById(R.id.backBTN);

        proceedBtn = findViewById(R.id.proceedBtn);
        billTV = findViewById(R.id.totalPriceTV);
        qtyTV = findViewById(R.id.totalQtyTV);

        recyclerView = (RecyclerView) findViewById(R.id.bill_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerView.class));
                finish();
            }
        });

        myItem = new ArrayList<CartItems>();

        ReadFromDB();

        CalculateBill();

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), PaymentProcess.class);
                it.putExtra("Qty", qtyTV.getText().toString());
                it.putExtra("Bill", billTV.getText().toString());
                startActivity(it);
                finish();
            }
        });

    }

    public void CalculateBill() {
        FirebaseDatabase.getInstance().getReference().child("temp_order").child(username).child("Items")

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            dbPrice = Integer.parseInt(snapshot.child("TotalPrice").getValue().toString());
                            dbQty = Integer.parseInt(snapshot.child("Quantity").getValue().toString());

                            totalBill += dbPrice;
                            totalQty += dbQty;
                        }
                        billTV.setText(String.valueOf(totalBill));
                        qtyTV.setText(String.valueOf(totalQty));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("temp_order").child(username).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            loopSize++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("temp_order").child(username).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        MyCartData.itemId = new String[loopSize];
                        MyCartData.itemName = new String[loopSize];
                        MyCartData.itemPrice = new String[loopSize];
                        MyCartData.itemCategory = new String[loopSize];
                        MyCartData.itemDescription = new String[loopSize];
                        MyCartData.itemSize = new String[loopSize];
                        MyCartData.itemURL = new String[loopSize];
                        MyCartData.itemTotalPrice = new String[loopSize];
                        MyCartData.itemQuantity = new String[loopSize];

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            MyCartData.itemId[j] = snapshot.child("Id").getValue().toString();
                            MyCartData.itemName[j] = snapshot.child("Name").getValue().toString();
                            MyCartData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                            MyCartData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                            MyCartData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                            MyCartData.itemSize[j] = snapshot.child("Size").getValue().toString();
                            MyCartData.itemURL[j] = snapshot.child("ImageUrl").getValue().toString();
                            MyCartData.itemQuantity[j] = snapshot.child("Quantity").getValue().toString();
                            MyCartData.itemTotalPrice[j] = snapshot.child("TotalPrice").getValue().toString();
                            j++;
                        }

                        for (int i = 0; i < MyCartData.itemId.length; i++) {
                            myItem.add(new CartItems(
                                    MyCartData.itemId[i],
                                    MyCartData.itemName[i],
                                    MyCartData.itemCategory[i],
                                    MyCartData.itemPrice[i],
                                    MyCartData.itemDescription[i],
                                    MyCartData.itemSize[i],
                                    MyCartData.itemURL[i],
                                    MyCartData.itemQuantity[i],
                                    MyCartData.itemTotalPrice[i]
                            ));
                        }

                        if (loopSize == 0) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(TotalBill.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();

                        } else {
                            loadingDialog.dismissDialog();

                            billItemsAdapter = new BillItemsAdapter(myItem);
                            recyclerView.setAdapter(billItemsAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), FoodCart.class));
        finish();
    }
}