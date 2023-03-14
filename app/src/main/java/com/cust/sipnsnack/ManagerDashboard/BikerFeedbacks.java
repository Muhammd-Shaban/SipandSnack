package com.cust.sipnsnack.ManagerDashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class BikerFeedbacks extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    Button returnBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<BikerFeedbackModel> myBiker;
    TextView tv;
    BikerFeedbackAdapter myAdapter2;
    int loopSize;

    public BikerFeedbacks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.biker_feedbacks, container,
                false);

        returnBtn = parentHolder.findViewById(R.id.returnBTN);
        recyclerView = (RecyclerView) parentHolder.findViewById(R.id.feedback_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(referenceActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tv = parentHolder.findViewById(R.id.emptyTV);

        myBiker = new ArrayList<BikerFeedbackModel>();

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

        System.out.println("HELoooooooooooooo");

        FirebaseDatabase.getInstance().getReference().child("BikersFeedback")
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

        FirebaseDatabase.getInstance().getReference().child("BikersFeedback")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;

                        BikerFeedbackData.id = new String[loopSize];
                        BikerFeedbackData.customerName = new String[loopSize];
                        BikerFeedbackData.customerPhoneNo = new String[loopSize];
                        BikerFeedbackData.bikerName = new String[loopSize];
                        BikerFeedbackData.bikerPhoneNo = new String[loopSize];
                        BikerFeedbackData.feedback = new String[loopSize];

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            BikerFeedbackData.id[j] = snapshot.getKey().toString();
                            BikerFeedbackData.customerName[j] = snapshot.child("CustomerName").getValue().toString();
                            BikerFeedbackData.customerPhoneNo[j] = snapshot.child("CustomerPhoneNo").getValue().toString();
                            BikerFeedbackData.bikerName[j] = snapshot.child("BikerName").getValue().toString();
                            BikerFeedbackData.bikerPhoneNo[j] = snapshot.child("BikerPhoneNo").getValue().toString();
                            BikerFeedbackData.feedback[j] = snapshot.child("FeedbackText").getValue().toString();
                            
                            j++;
                        }

                        for (int i = 0; i < BikerFeedbackData.id.length; i++) {
                            myBiker.add(new BikerFeedbackModel(
                                    BikerFeedbackData.id[i],
                                    BikerFeedbackData.feedback[i],
                                    BikerFeedbackData.customerName[i],
                                    BikerFeedbackData.customerPhoneNo[i],
                                    BikerFeedbackData.bikerName[i],
                                    BikerFeedbackData.bikerPhoneNo[i]
                            ));
                        }

                        if (loopSize == 0) {
                            tv.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            returnBtn.setVisibility(View.INVISIBLE);
                        } else {
                            tv.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            returnBtn.setVisibility(View.VISIBLE);

                            myAdapter2 = new BikerFeedbackAdapter(myBiker);
                            recyclerView.setAdapter(myAdapter2);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}