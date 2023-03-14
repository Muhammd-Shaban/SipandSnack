package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

public class BikerFeedbackDetails extends AppCompatActivity {

    LoadingDialog loadingDialog;
    TextView customerNameTV, customerPhoneNoTV, feedbackTV, bikerNameTV, bikerPhoneNoTV;
    Button backBTN;
    String name, phone, name2, phone2, feedback, dbKey;

    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    int loopSize;
    ArrayList<AcceptedOrderItems> myItem;
    AcceptedOrderItemsAdapter acceptedOrderItemsAdapter;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biker_feedback_details);

        loadingDialog = new LoadingDialog(BikerFeedbackDetails.this);

        customerNameTV = findViewById(R.id.customerNameTV);
        customerPhoneNoTV = findViewById(R.id.customerPhoneTV);
        bikerNameTV = findViewById(R.id.bikerNameTV);
        bikerPhoneNoTV = findViewById(R.id.bikerPhoneTV);
        feedbackTV = findViewById(R.id.feedbackTV);
        backBTN = findViewById(R.id.backBtn);
        backBtn = findViewById(R.id.backBTN);

        setTextViews();

        recyclerView = (RecyclerView) findViewById(R.id.delivered_items_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myItem = new ArrayList<AcceptedOrderItems>();


        ReadFromDB();

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserFeedbacks.class));
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserFeedbacks.class));
                finish();
            }
        });
    }

    public void setTextViews() {
        name = BikerFeedbackAdapter.name1;
        phone = BikerFeedbackAdapter.phone1;
        feedback = BikerFeedbackAdapter.feedback;
        dbKey = BikerFeedbackAdapter.id;
        name2 = BikerFeedbackAdapter.name2;
        phone2 = BikerFeedbackAdapter.phone2;

        customerNameTV.setText(name);
        customerPhoneNoTV.setText(phone);
        feedbackTV.setText(feedback);
        bikerNameTV.setText(name2);
        bikerPhoneNoTV.setText(phone2);

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
                            Toast.makeText(BikerFeedbackDetails.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), UserFeedbacks.class));
        finish();
    }
}