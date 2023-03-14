package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DeliveredOrderDetails extends AppCompatActivity {

    LoadingDialog loadingDialog;
    TextView customerNameTV, customerPhoneNoTV, customerAddressTV, paymentTypeTV, totalQtyTV,
            totalPriceTV, acceptedByTV, textViewDate, textViewTime, bikerNameTV, bikerPhoneNoTV;
    ImageView receiptImageIV;
    Button deleteBTN, backBTN;
    String username, name, phone, address, payment, qty, price, receipt, acceptedBy, bName,
            bPhone, dbKey;

    String remarks, stars;
    View view;
    float sts;
    int opt = 1;

    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    int loopSize;
    ArrayList<AcceptedOrderItems> myItem;
    AcceptedOrderItemsAdapter acceptedOrderItemsAdapter;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_order_details);

        loadingDialog = new LoadingDialog(DeliveredOrderDetails.this);

        customerNameTV = findViewById(R.id.customerNameTV);
        customerPhoneNoTV = findViewById(R.id.customerPhoneNoTV);
        view = findViewById(R.id.view4);
        customerAddressTV = findViewById(R.id.customerAddressTV);
        paymentTypeTV = findViewById(R.id.paymentTypeTV);
        totalQtyTV = findViewById(R.id.totalQtyTV);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        acceptedByTV = findViewById(R.id.acceptedByTV);
        textViewDate = findViewById(R.id.dateTV);
        textViewTime = findViewById(R.id.timeTV);
        receiptImageIV = findViewById(R.id.receiptImageIV);
        bikerNameTV = findViewById(R.id.bikerNameTV);
        bikerPhoneNoTV = findViewById(R.id.bikerPhoneTV);
        deleteBTN = findViewById(R.id.deleteBtn);
        backBTN = findViewById(R.id.backBtn);
        backBtn = findViewById(R.id.backBTN);

        setTextViews();

        recyclerView = (RecyclerView) findViewById(R.id.delivered_items_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myItem = new ArrayList<AcceptedOrderItems>();

        receipt = DeliveredOrdersAdapter.receiptImage;
        Picasso.get()
                .load(receipt)
                .placeholder(R.drawable.logo)
                .into(receiptImageIV);

        ReadFromDB();

        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOrderDialog();
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AllOrdersDetails.class));
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AllOrdersDetails.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AllOrdersDetails.class));
        finish();
    }

    public void setTextViews() {
        username = DeliveredOrdersAdapter.username;
        name = DeliveredOrdersAdapter.name;
        phone = DeliveredOrdersAdapter.phoneNo;

        if (DeliveredOrdersAdapter.addressType.equals("Manual")) {
            address = DeliveredOrdersAdapter.address;
        } else {
            address = "(On Maps)";
        }
        payment = DeliveredOrdersAdapter.paymentType;

        if (!payment.equals("Online")) {
            receiptImageIV.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }

        acceptedBy = DeliveredOrdersAdapter.acceptedBy;
        qty = DeliveredOrdersAdapter.totalQty;
        price = DeliveredOrdersAdapter.totalBill;
        bName = DeliveredOrdersAdapter.bikerName;
        bPhone = DeliveredOrdersAdapter.bikerPhoneNo;
        dbKey = DeliveredOrdersAdapter.dbKey;

        customerNameTV.setText(name);
        customerPhoneNoTV.setText(phone);
        customerAddressTV.setText(address);
        paymentTypeTV.setText(payment);
        acceptedByTV.setText(acceptedBy);
        totalQtyTV.setText(qty);
        totalPriceTV.setText(price);
        bikerNameTV.setText(bName);
        bikerPhoneNoTV.setText(bPhone);
        textViewDate.setText(DeliveredOrdersAdapter.orderDate);
        textViewTime.setText(DeliveredOrdersAdapter.orderTime);

    }

    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("Orders").child("Delivered").
                child(dbKey).child("Items")
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

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Delivered")
                .child(dbKey).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        MyAcceptedOrderData.itemId = new String[loopSize];
                        MyAcceptedOrderData.itemName = new String[loopSize];
                        MyAcceptedOrderData.itemPrice = new String[loopSize];
                        MyAcceptedOrderData.itemCategory = new String[loopSize];
                        MyAcceptedOrderData.itemDescription = new String[loopSize];
                        MyAcceptedOrderData.itemSize = new String[loopSize];
                        MyAcceptedOrderData.itemURL = new String[loopSize];
                        MyAcceptedOrderData.itemTotalPrice = new String[loopSize];
                        MyAcceptedOrderData.itemQuantity = new String[loopSize];

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            MyAcceptedOrderData.itemId[j] = snapshot.child("Id").getValue().toString();
                            MyAcceptedOrderData.itemName[j] = snapshot.child("Name").getValue().toString();
                            MyAcceptedOrderData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                            MyAcceptedOrderData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                            MyAcceptedOrderData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                            MyAcceptedOrderData.itemSize[j] = snapshot.child("Size").getValue().toString();
                            MyAcceptedOrderData.itemURL[j] = snapshot.child("ImageUrl").getValue().toString();
                            MyAcceptedOrderData.itemQuantity[j] = snapshot.child("Quantity").getValue().toString();
                            MyAcceptedOrderData.itemTotalPrice[j] = snapshot.child("TotalPrice").getValue().toString();

                            j++;
                        }

                        for (int i = 0; i < MyAcceptedOrderData.itemId.length; i++) {
                            myItem.add(new AcceptedOrderItems(
                                    MyAcceptedOrderData.itemId[i],
                                    MyAcceptedOrderData.itemName[i],
                                    MyAcceptedOrderData.itemCategory[i],
                                    MyAcceptedOrderData.itemPrice[i],
                                    MyAcceptedOrderData.itemDescription[i],
                                    MyAcceptedOrderData.itemSize[i],
                                    MyAcceptedOrderData.itemURL[i],
                                    MyAcceptedOrderData.itemQuantity[i],
                                    MyAcceptedOrderData.itemTotalPrice[i]
                            ));
                        }

                        if (loopSize == 0) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(DeliveredOrderDetails.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();

                        } else {
                            loadingDialog.dismissDialog();

                            acceptedOrderItemsAdapter = new AcceptedOrderItemsAdapter(myItem);
                            recyclerView.setAdapter(acceptedOrderItemsAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getOrderRating() {
        loadingDialog.startLoadingDialog();

        FirebaseDatabase.getInstance().getReference().child("OrdersStars").child(DeliveredOrdersAdapter.dbKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        stars = dataSnapshot.child("Stars").getValue().toString();

                        loadingDialog.dismissDialog();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void deleteOrderDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_delete_order_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Orders").child("Delivered")
                        .child(dbKey).removeValue();
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Context context = v.getContext();
                Intent intent = new Intent(context, ManageOrders.class);
                context.startActivity(intent);
                Toast.makeText(context, "Order Deleted Successfully", Toast.LENGTH_SHORT).show();
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


    public void aboutRatingDialog(String str, int op) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_food_rating, null);
        TextView tv1, tv2;
        tv1 = view.findViewById(R.id.starsCountTV);
        tv2 = view.findViewById(R.id.remarksTV);
        RatingBar rB = findViewById(R.id.starsRating);

        if (op == 1) {
            tv1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
            tv2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
            tv2.setText("(Poor)");
        } else if (op == 2) {
            tv1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_green));
            tv2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_green));
            tv2.setText("(Normal)");
        } else {
            tv1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
            tv2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
            tv2.setText("(Excellent)");
        }

        tv1.setText(str);

        sts = Float.parseFloat(str);
        rB.setIsIndicator(true);
        rB.setRating(sts);


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
}