package com.cust.sipnsnack.Customers;

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

public class Accepted_fragment extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    Button returnBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<MyOrdersModel> myOrder;
    TextView tv;
    ImageView notFound;
    CustomerOrdersAdapter ordersAdapter;
    int loopSize;
    String dbUsername;

    public Accepted_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.accepted_fragment, container,
                false);

        returnBtn = parentHolder.findViewById(R.id.returnBTN);
        recyclerView = (RecyclerView) parentHolder.findViewById(R.id.orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(referenceActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tv = parentHolder.findViewById(R.id.emptyTV);
        notFound = parentHolder.findViewById(R.id.notFoundIV);

        myOrder = new ArrayList<MyOrdersModel>();

        ReadFromDB();

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), CustomerView.class));
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
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                dbUsername = snapshot.child("Username").getValue().toString();

                                if (dbUsername.equals(OrdersHistory.customerUsername)) {
                                    loopSize++;
                                }
                            }
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
                        CustomerOrdersData.customerName = new String[loopSize];
                        CustomerOrdersData.paymentType = new String[loopSize];
                        CustomerOrdersData.orderQty = new String[loopSize];
                        CustomerOrdersData.orderBill = new String[loopSize];
                        CustomerOrdersData.orderTime = new String[loopSize];
                        CustomerOrdersData.orderDate = new String[loopSize];
                        CustomerOrdersData.orderId = new String[loopSize];


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            if (snapshot.exists()) {
                                dbUsername = snapshot.child("Username").getValue().toString();

                                if (dbUsername.equals(OrdersHistory.customerUsername)) {
                                    CustomerOrdersData.orderId[j] = snapshot.getKey().toString();
                                    CustomerOrdersData.customerName[j] = snapshot.child("Name").getValue().toString();
                                    CustomerOrdersData.paymentType[j] = snapshot.child("PaymentType").getValue().toString();
                                    CustomerOrdersData.orderQty[j] = snapshot.child("TotalQuantity").getValue().toString();
                                    CustomerOrdersData.orderBill[j] = snapshot.child("TotalBill").getValue().toString();
                                    CustomerOrdersData.orderTime[j] = snapshot.child("OrderTime").getValue().toString();
                                    CustomerOrdersData.orderDate[j] = snapshot.child("OrderDate").getValue().toString();

                                    j++;
                                }
                            }
                        }

                        for (int i = 0; i < CustomerOrdersData.customerName.length; i++) {
                            myOrder.add(new MyOrdersModel(
                                    CustomerOrdersData.customerName[i],
                                    CustomerOrdersData.paymentType[i],
                                    CustomerOrdersData.orderQty[i],
                                    CustomerOrdersData.orderBill[i],
                                    CustomerOrdersData.orderTime[i],
                                    CustomerOrdersData.orderDate[i],
                                    CustomerOrdersData.orderId[i]

                            ));
                        }

                        if (loopSize == 0) {
                            recyclerView.setVisibility(View.GONE);
                            returnBtn.setVisibility(View.GONE);
                            tv.setVisibility(View.VISIBLE);
                            notFound.setVisibility(View.VISIBLE);
                        } else {
                            tv.setVisibility(View.GONE);
                            notFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            returnBtn.setVisibility(View.VISIBLE);

                            ordersAdapter = new CustomerOrdersAdapter(myOrder);
                            recyclerView.setAdapter(ordersAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}