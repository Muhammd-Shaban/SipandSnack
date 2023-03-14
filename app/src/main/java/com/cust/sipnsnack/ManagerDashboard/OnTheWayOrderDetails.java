package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
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

public class OnTheWayOrderDetails extends AppCompatActivity {

    TextView customerNameTV, customerPhoneNoTV, customerAddressTV, paymentTypeTV, totalQtyTV,
            acceptedByTV, totalPriceTV, dateTV, timeTV, bikerNameTV, bikerPhoneNoTV;
    LoadingDialog loadingDialog;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    int loopSize;
    String orderId;
    ArrayList<AcceptedOrderItems> myItem;
    AcceptedOrderItemsAdapter acceptedOrderItemsAdapter;
    ImageView backBtn, locationIV, phoneIV;
    Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_the_way_order_details);

        loadingDialog = new LoadingDialog(OnTheWayOrderDetails.this);

        customerNameTV = findViewById(R.id.customerNameTV);
        customerPhoneNoTV = findViewById(R.id.customerPhoneNoTV);
        customerAddressTV = findViewById(R.id.customerAddressTV);
        paymentTypeTV = findViewById(R.id.paymentTypeTV);
        totalQtyTV = findViewById(R.id.totalQtyTV);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        dateTV = findViewById(R.id.dateTV);
        timeTV = findViewById(R.id.timeTV);
        bikerNameTV = findViewById(R.id.bikerNameTV);
        bikerPhoneNoTV = findViewById(R.id.bikerPhoneTV);
        acceptedByTV = findViewById(R.id.acceptedByTV);
        goBack = findViewById(R.id.goBackBtn);
        backBtn = findViewById(R.id.backBTN);
        locationIV = findViewById(R.id.locationIV);
        phoneIV = findViewById(R.id.phoneIV);

        recyclerView = (RecyclerView) findViewById(R.id.ontheway_items_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myItem = new ArrayList<AcceptedOrderItems>();

        setTextViews();
        ReadFromDB();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), DashBoard.class);
                startActivity(it);
                finish();
            }
        });

        phoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + OnTheWayOrdersAdapter.bikerPhoneNo));
                startActivity(intent);
            }
        });

        locationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (OnTheWayOrdersAdapter.addressType.equals("Manual")) {
                    intent = new Intent(getApplicationContext(), TrackOrderByAddress.class);
                    intent.putExtra("Address", OnTheWayOrdersAdapter.address);
                } else {
                    intent = new Intent(getApplicationContext(), TrackOrderByDirections.class);
                    intent.putExtra("Longitude", OnTheWayOrdersAdapter.lon);
                    intent.putExtra("Latitude", OnTheWayOrdersAdapter.lat);
                }
                intent.putExtra("PhoneNo", OnTheWayOrdersAdapter.bikerPhoneNo);
                intent.putExtra("OrderId", OnTheWayOrdersAdapter.orderId);
                startActivity(intent);
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
        Intent it = new Intent(getApplicationContext(), OnTheWayOrders.class);
        startActivity(it);
        finish();
    }

    public void setTextViews() {

        customerNameTV.setText(OnTheWayOrdersAdapter.name);
        customerPhoneNoTV.setText(OnTheWayOrdersAdapter.phoneNo);

        if (OnTheWayOrdersAdapter.addressType.equals("Manual")) {
            customerAddressTV.setText(OnTheWayOrdersAdapter.address);
        } else {
            customerAddressTV.setText("(On Maps)");
        }

        paymentTypeTV.setText(OnTheWayOrdersAdapter.paymentType);
        acceptedByTV.setText(OnTheWayOrdersAdapter.acceptedBy);
        totalQtyTV.setText(OnTheWayOrdersAdapter.totalQty);
        totalPriceTV.setText(OnTheWayOrdersAdapter.totalBill);
        dateTV.setText(OnTheWayOrdersAdapter.orderDate);
        timeTV.setText(OnTheWayOrdersAdapter.orderTime);
        bikerNameTV.setText(OnTheWayOrdersAdapter.bikerName);
        bikerPhoneNoTV.setText(OnTheWayOrdersAdapter.bikerPhoneNo);
        orderId = OnTheWayOrdersAdapter.orderId;

    }

    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way").
                child(OnTheWayOrdersAdapter.orderId).child("Items")
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

        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .child(OnTheWayOrdersAdapter.orderId).child("Items")
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
                            Toast.makeText(OnTheWayOrderDetails.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();

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
}