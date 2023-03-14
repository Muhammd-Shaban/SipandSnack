package com.cust.sipnsnack.ManagerDashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AcceptedOrdersDetails extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    Button returnBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<MyOrders> myOrder;
    TextView tv;
    ImageView notFound;
    AcceptedOrdersAdapter ordersAdapter;
    int loopSize;

    public AcceptedOrdersDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.accepted_orders_details, container,
                false);

        returnBtn = parentHolder.findViewById(R.id.returnBTN);
        recyclerView = (RecyclerView) parentHolder.findViewById(R.id.orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(referenceActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tv = parentHolder.findViewById(R.id.emptyTV);
        notFound = parentHolder.findViewById(R.id.notFoundIV);

        myOrder = new ArrayList<MyOrders>();

        ReadFromDB();

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), DashBoard.class));
            }
        });


        return parentHolder;
    }


    void ReadFromDB() {
        loopSize = 0;

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Accepted")
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

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Accepted")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        AcceptedOrdersData.customerUsername = new String[loopSize];
                        AcceptedOrdersData.customerName = new String[loopSize];
                        AcceptedOrdersData.customerPhoneNo = new String[loopSize];
                        AcceptedOrdersData.customerAddress = new String[loopSize];
                        AcceptedOrdersData.paymentType = new String[loopSize];
                        AcceptedOrdersData.receiptImage = new String[loopSize];
                        AcceptedOrdersData.quantity = new String[loopSize];
                        AcceptedOrdersData.bill = new String[loopSize];
                        AcceptedOrdersData.acceptedBy = new String[loopSize];
                        AcceptedOrdersData.orderDate = new String[loopSize];
                        AcceptedOrdersData.orderTime = new String[loopSize];
                        AcceptedOrdersData.orderId = new String[loopSize];
                        AcceptedOrdersData.addressType = new String[loopSize];
                        AcceptedOrdersData.longitude = new String[loopSize];
                        AcceptedOrdersData.latitude = new String[loopSize];

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            AcceptedOrdersData.orderId[j] = snapshot.getKey().toString();
                            AcceptedOrdersData.customerUsername[j] = snapshot.child("Username").getValue().toString();
                            AcceptedOrdersData.customerName[j] = snapshot.child("Name").getValue().toString();
                            AcceptedOrdersData.customerPhoneNo[j] = snapshot.child("PhoneNo").getValue().toString();
                            AcceptedOrdersData.addressType[j] = snapshot.child("AddressType").getValue().toString();

                            if (AcceptedOrdersData.addressType[j].equals("Manual")) {
                                AcceptedOrdersData.customerAddress[j] = snapshot.child("Address").getValue().toString();
                                AcceptedOrdersData.latitude[j] = "";
                                AcceptedOrdersData.longitude[j] = "";
                            } else {
                                AcceptedOrdersData.customerAddress[j] = "";
                                AcceptedOrdersData.latitude[j] = snapshot.child("Latitude").getValue().toString();
                                AcceptedOrdersData.longitude[j] = snapshot.child("Longitude").getValue().toString();
                            }

                            AcceptedOrdersData.paymentType[j] = snapshot.child("PaymentType").getValue().toString();
                            AcceptedOrdersData.receiptImage[j] = snapshot.child("ReceiptImage").getValue().toString();
                            AcceptedOrdersData.quantity[j] = snapshot.child("TotalQuantity").getValue().toString();
                            AcceptedOrdersData.bill[j] = snapshot.child("TotalBill").getValue().toString();
                            AcceptedOrdersData.acceptedBy[j] = snapshot.child("AcceptedBy").getValue().toString();
                            AcceptedOrdersData.orderDate[j] = snapshot.child("OrderDate").getValue().toString();
                            AcceptedOrdersData.orderTime[j] = snapshot.child("OrderTime").getValue().toString();

                            j++;
                        }

                        for (int i = 0; i < AcceptedOrdersData.customerUsername.length; i++) {
                            myOrder.add(new MyOrders(
                                    AcceptedOrdersData.customerUsername[i],
                                    AcceptedOrdersData.customerName[i],
                                    AcceptedOrdersData.customerPhoneNo[i],
                                    AcceptedOrdersData.customerAddress[i],
                                    AcceptedOrdersData.paymentType[i],
                                    AcceptedOrdersData.receiptImage[i],
                                    AcceptedOrdersData.quantity[i],
                                    AcceptedOrdersData.bill[i],
                                    AcceptedOrdersData.acceptedBy[i],
                                    AcceptedOrdersData.orderDate[i],
                                    AcceptedOrdersData.orderTime[i],
                                    AcceptedOrdersData.orderId[i],
                                    AcceptedOrdersData.addressType[i],
                                    AcceptedOrdersData.latitude[i],
                                    AcceptedOrdersData.longitude[i]

                            ));
                        }

                        if (loopSize == 0) {
                            tv.setVisibility(View.VISIBLE);
                            notFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            returnBtn.setVisibility(View.INVISIBLE);
                        } else {
                            tv.setVisibility(View.GONE);
                            notFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            returnBtn.setVisibility(View.VISIBLE);

                            ordersAdapter = new AcceptedOrdersAdapter(myOrder);
                            recyclerView.setAdapter(ordersAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}