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

public class FoodFeedbacks extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    Button returnBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<FoodModel> myFood;
    TextView tv;
    FoodFeedbackAdapter myAdapter;
    int loopSize;

    public FoodFeedbacks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.food_feedbacks, container,
                false);

        returnBtn = parentHolder.findViewById(R.id.returnBTN);
        recyclerView = (RecyclerView) parentHolder.findViewById(R.id.feedback_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(referenceActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tv = parentHolder.findViewById(R.id.emptyTV);

        myFood = new ArrayList<FoodModel>();

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

        FirebaseDatabase.getInstance().getReference().child("Feedbacks")
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

        FirebaseDatabase.getInstance().getReference().child("Feedbacks")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        int j = 0;

                        FoodFeedbackData.customerName = new String[loopSize];
                        FoodFeedbackData.customerPhoneNo = new String[loopSize];
                        FoodFeedbackData.id = new String[loopSize];
                        FoodFeedbackData.feedback = new String[loopSize];

                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {


                            FoodFeedbackData.id[j] = snapshot1.getKey().toString();
                            FoodFeedbackData.feedback[j] = snapshot1.child("FeedbackText").getValue().toString();
                            FoodFeedbackData.customerName[j] = snapshot1.child("CustomerName").getValue().toString();
                            FoodFeedbackData.customerPhoneNo[j] = snapshot1.child("CustomerPhoneNo").getValue().toString();

                            j++;

                        }

                        for (int i = 0; i < FoodFeedbackData.id.length; i++) {
                            myFood.add(new FoodModel(
                                    FoodFeedbackData.id[i],
                                    FoodFeedbackData.feedback[i],
                                    FoodFeedbackData.customerName[i],
                                    FoodFeedbackData.customerPhoneNo[i]

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

                            myAdapter = new FoodFeedbackAdapter(myFood);
                            recyclerView.setAdapter(myAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}