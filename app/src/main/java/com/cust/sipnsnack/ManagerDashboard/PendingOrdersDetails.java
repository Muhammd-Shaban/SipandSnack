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

public class PendingOrdersDetails extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    Button returnBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<MyOrders> myOrder;
    TextView tv;
    ImageView notFound;
    MyOrdersAdapter ordersAdapter;
    int loopSize;

    public PendingOrdersDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.pending_orders_details, container,
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

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending")
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

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        MyOrdersData.customerUsername = new String[loopSize];
                        MyOrdersData.customerName = new String[loopSize];
                        MyOrdersData.customerPhoneNo = new String[loopSize];
                        MyOrdersData.customerAddress = new String[loopSize];
                        MyOrdersData.paymentType = new String[loopSize];
                        MyOrdersData.receiptImage = new String[loopSize];
                        MyOrdersData.orderDate = new String[loopSize];
                        MyOrdersData.orderTime = new String[loopSize];
                        MyOrdersData.orderId = new String[loopSize];
                        MyOrdersData.addressType = new String[loopSize];
                        MyOrdersData.longitude = new String[loopSize];
                        MyOrdersData.latitude = new String[loopSize];

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            MyOrdersData.orderId[j] = snapshot.getKey().toString();
                            MyOrdersData.customerUsername[j] = snapshot.child("Username").getValue().toString();
                            MyOrdersData.customerName[j] = snapshot.child("Name").getValue().toString();
                            MyOrdersData.customerPhoneNo[j] = snapshot.child("PhoneNo").getValue().toString();
                            MyOrdersData.addressType[j] = snapshot.child("AddressType").getValue().toString();

                            if (MyOrdersData.addressType[j].equals("Manual")) {
                                MyOrdersData.customerAddress[j] = snapshot.child("Address").getValue().toString();
                                MyOrdersData.longitude[j] = "";
                                MyOrdersData.latitude[j] = "";
                            } else {
                                MyOrdersData.longitude[j] = snapshot.child("Longitude").getValue().toString();
                                MyOrdersData.latitude[j] = snapshot.child("Latitude").getValue().toString();
                                MyOrdersData.customerAddress[j] = "";
                            }
                            MyOrdersData.paymentType[j] = snapshot.child("PaymentType").getValue().toString();
                            MyOrdersData.receiptImage[j] = snapshot.child("ReceiptImage").getValue().toString();
                            MyOrdersData.orderDate[j] = snapshot.child("OrderDate").getValue().toString();
                            MyOrdersData.orderTime[j] = snapshot.child("OrderTime").getValue().toString();

                            j++;
                        }

                        for (int i = 0; i < MyOrdersData.orderId.length; i++) {
                            myOrder.add(new MyOrders(
                                    MyOrdersData.customerUsername[i],
                                    MyOrdersData.customerName[i],
                                    MyOrdersData.customerPhoneNo[i],
                                    MyOrdersData.customerAddress[i],
                                    MyOrdersData.paymentType[i],
                                    MyOrdersData.receiptImage[i],
                                    MyOrdersData.orderDate[i],
                                    MyOrdersData.orderTime[i],
                                    MyOrdersData.orderId[i],
                                    MyOrdersData.longitude[i],
                                    MyOrdersData.latitude[i],
                                    MyOrdersData.addressType[i],
                                    ""
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

                            ordersAdapter = new MyOrdersAdapter(myOrder);
                            recyclerView.setAdapter(ordersAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}