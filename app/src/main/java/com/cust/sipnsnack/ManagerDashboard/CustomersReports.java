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

public class CustomersReports extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    Button returnBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<ReportModel> myReport;
    TextView tv;
    CustomerReportAdapter myAdapter;
    int loopSize;

    public CustomersReports() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.customers_reports, container,
                false);

        returnBtn = parentHolder.findViewById(R.id.returnBTN);
        recyclerView = (RecyclerView) parentHolder.findViewById(R.id.report_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(referenceActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tv = parentHolder.findViewById(R.id.emptyTV);

        myReport = new ArrayList<ReportModel>();

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

        FirebaseDatabase.getInstance().getReference().child("ReportsForSystem").child("Customers")
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

        FirebaseDatabase.getInstance().getReference().child("ReportsForSystem").child("Customers")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        int j = 0;

                        SystemReportsData.id = new String[loopSize];
                        SystemReportsData.name = new String[loopSize];
                        SystemReportsData.phone = new String[loopSize];
                        SystemReportsData.report = new String[loopSize];
                        SystemReportsData.date = new String[loopSize];
                        SystemReportsData.time = new String[loopSize];

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                            SystemReportsData.id[j] = snapshot.getKey().toString();
                            SystemReportsData.name[j] = snapshot.child("CustomerName").getValue().toString();
                            SystemReportsData.phone[j] = snapshot.child("CustomerPhoneNo").getValue().toString();
                            SystemReportsData.report[j] = snapshot.child("ReportText").getValue().toString();
                            SystemReportsData.date[j] = snapshot.child("ReportDate").getValue().toString();
                            SystemReportsData.time[j] = snapshot.child("ReportTime").getValue().toString();

                            j++;

                        }

                        for (int i = 0; i < SystemReportsData.id.length; i++) {
                            myReport.add(new ReportModel(
                                    SystemReportsData.id[i],
                                    SystemReportsData.name[i],
                                    SystemReportsData.phone[i],
                                    SystemReportsData.id[i],
                                    SystemReportsData.report[i],
                                    SystemReportsData.time[i],
                                    SystemReportsData.date[i]

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

                            myAdapter = new CustomerReportAdapter(myReport);
                            recyclerView.setAdapter(myAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}