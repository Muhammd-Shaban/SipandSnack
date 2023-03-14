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

public class OntheWayOrdersDetails extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    Button returnBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<MyOnTheWayOrders> myOrder;
    TextView tv;
    ImageView notFound;
    OnTheWayOrdersAdapter ordersAdapter;
    int loopSize;

    public OntheWayOrdersDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.ontheway_orders_details, container,
                false);

        returnBtn = parentHolder.findViewById(R.id.returnBTN);
        recyclerView = (RecyclerView) parentHolder.findViewById(R.id.orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(referenceActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tv = parentHolder.findViewById(R.id.emptyTV);
        notFound = parentHolder.findViewById(R.id.notFoundIV);

        myOrder = new ArrayList<MyOnTheWayOrders>();

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

        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                loopSize++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            int j = 0;

                            OntheWayOrdersData.customerUsername = new String[loopSize];
                            OntheWayOrdersData.customerName = new String[loopSize];
                            OntheWayOrdersData.customerPhoneNo = new String[loopSize];
                            OntheWayOrdersData.customerAddress = new String[loopSize];
                            OntheWayOrdersData.paymentType = new String[loopSize];
                            OntheWayOrdersData.quantity = new String[loopSize];
                            OntheWayOrdersData.bill = new String[loopSize];
                            OntheWayOrdersData.acceptedBy = new String[loopSize];
                            OntheWayOrdersData.bikerUsername = new String[loopSize];
                            OntheWayOrdersData.bikerName = new String[loopSize];
                            OntheWayOrdersData.bikerPhoneNo = new String[loopSize];
                            OntheWayOrdersData.orderDate = new String[loopSize];
                            OntheWayOrdersData.orderTime = new String[loopSize];
                            OntheWayOrdersData.orderId = new String[loopSize];
                            OntheWayOrdersData.addressType = new String[loopSize];
                            OntheWayOrdersData.longitude = new String[loopSize];
                            OntheWayOrdersData.latitude = new String[loopSize];


                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                OntheWayOrdersData.orderId[j] = snapshot.getKey().toString();
                                OntheWayOrdersData.customerUsername[j] = snapshot.child("CustomerUsername").getValue().toString();
                                OntheWayOrdersData.customerName[j] = snapshot.child("CustomerName").getValue().toString();
                                OntheWayOrdersData.customerPhoneNo[j] = snapshot.child("CustomerPhoneNo").getValue().toString();
                                OntheWayOrdersData.addressType[j] = snapshot.child("AddressType").getValue().toString();

                                if (OntheWayOrdersData.addressType[j].equals("Manual")) {
                                    OntheWayOrdersData.customerAddress[j] = snapshot.child("CustomerAddress").getValue().toString();
                                    OntheWayOrdersData.longitude[j] = "";
                                    OntheWayOrdersData.latitude[j] = "";
                                } else {
                                    OntheWayOrdersData.longitude[j] = snapshot.child("Longitude").getValue().toString();
                                    OntheWayOrdersData.latitude[j] = snapshot.child("Latitude").getValue().toString();
                                    OntheWayOrdersData.customerAddress[j] = "";
                                }

                                OntheWayOrdersData.paymentType[j] = snapshot.child("CustomerPaymentType").getValue().toString();
                                OntheWayOrdersData.quantity[j] = snapshot.child("CustomerTotalQuantity").getValue().toString();
                                OntheWayOrdersData.bill[j] = snapshot.child("CustomerTotalBill").getValue().toString();
                                OntheWayOrdersData.acceptedBy[j] = snapshot.child("AcceptedBy").getValue().toString();
                                OntheWayOrdersData.bikerUsername[j] = snapshot.child("BikerUsername").getValue().toString();
                                OntheWayOrdersData.bikerName[j] = snapshot.child("BikerName").getValue().toString();
                                OntheWayOrdersData.bikerPhoneNo[j] = snapshot.child("BikerPhoneNo").getValue().toString();
                                OntheWayOrdersData.orderDate[j] = snapshot.child("OrderDate").getValue().toString();
                                OntheWayOrdersData.orderTime[j] = snapshot.child("OrderTime").getValue().toString();

                                j++;
                            }

                            for (int i = 0; i < OntheWayOrdersData.orderId.length; i++) {
                                myOrder.add(new MyOnTheWayOrders(
                                        OntheWayOrdersData.customerUsername[i],
                                        OntheWayOrdersData.customerName[i],
                                        OntheWayOrdersData.customerPhoneNo[i],
                                        OntheWayOrdersData.customerAddress[i],
                                        OntheWayOrdersData.paymentType[i],
                                        OntheWayOrdersData.quantity[i],
                                        OntheWayOrdersData.bill[i],
                                        OntheWayOrdersData.acceptedBy[i],
                                        OntheWayOrdersData.bikerUsername[i],
                                        OntheWayOrdersData.bikerName[i],
                                        OntheWayOrdersData.bikerPhoneNo[i],
                                        OntheWayOrdersData.orderDate[i],
                                        OntheWayOrdersData.orderTime[i],
                                        OntheWayOrdersData.orderId[i],
                                        OntheWayOrdersData.addressType[i],
                                        OntheWayOrdersData.longitude[i],
                                        OntheWayOrdersData.latitude[i]

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

                                ordersAdapter = new OnTheWayOrdersAdapter(myOrder);
                                recyclerView.setAdapter(ordersAdapter);
                            }

                        } else {
                            tv.setVisibility(View.VISIBLE);
                            notFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            returnBtn.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}